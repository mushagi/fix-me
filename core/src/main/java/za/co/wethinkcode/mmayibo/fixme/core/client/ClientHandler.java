package za.co.wethinkcode.mmayibo.fixme.core.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import za.co.wethinkcode.mmayibo.fixme.core.ResponseFuture;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.*;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

@ChannelHandler.Sharable
class ClientHandler extends SimpleChannelInboundHandler<String> {
    private final Logger logger = Logger.getLogger(getClass().getName());
    private final Client client;
    private final ResponseFuture responseFuture;
    private Channel channel;
    private FixMessageBuilder existingBuilder = new FixMessageBuilder();
    private final ConcurrentHashMap<String, String> unSentMessages = new ConcurrentHashMap<>();

    ClientHandler(Client client) {
        this.client = client;
        this.responseFuture = client.responseFuture;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String rawFixMessage){
        if (FixMessageTools.isValidMessage(rawFixMessage)) {
            FixMessage decodeFixMessage = FixDecoder.decode(rawFixMessage);
            updateUnsentMessages(decodeFixMessage);
            responseFuture.set(decodeFixMessage.getMessageId(), decodeFixMessage);
            client.messageRead(decodeFixMessage, rawFixMessage);
        }
        else
            sendRejectedInvalidFixMessage(ctx, rawFixMessage);
    }

    private void updateUnsentMessages(FixMessage message) {
        if (message.getMessageId() != null){
            String unsentMessage = unSentMessages.get(message.getMessageId());
            if (unsentMessage != null && "true".equals(message.getTestReqId())){
                unSentMessages.remove(message.getMessageId());
            }

        }
    }

    private void sendRejectedInvalidFixMessage(ChannelHandlerContext ctx, String rawFixMessage) {
        FixMessage rejectedMessage = new FixMessageBuilder()
                .withText(rawFixMessage)
                .getFixMessage();

        String encodedFix = FixEncode.encode(rejectedMessage);
        ctx.channel().writeAndFlush(encodedFix + "\r\n");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        client.isActive = true;
        client.channelActive();
        channel = ctx.channel();
        channel.closeFuture();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        logger.info("Channel is no longer connected with the server. Trying to connect again...");
        if (!client.isBeingShutdown)
            client.doConnect();
    }

    void sendRequest(FixMessage message, boolean logging, boolean withTracking){
        if (channel != null && channel.isActive()){
            existingBuilder
                    .existingMessage(message)
                    .withSenderCompId(client.networkId);

            String encodedFixMessage = FixEncode.encode(message);
            String messageId = FixMessageTools.getTagValueByRegex(encodedFixMessage, FixTags.MSG_ID.tag);

            if (withTracking && messageId != null)
                unSentMessages.put(messageId, encodedFixMessage);

            sendRawFixMessage(encodedFixMessage, logging);

        }
        else
        if (logging)
            logger.info("Cannot send request. Server can't be reached");

    }

    void sendResponse(FixMessage responseMessage, boolean withLogging, boolean withTracking) {
        if (channel != null && channel.isActive()) {

            String encodedFixMessage = FixEncode.encode(responseMessage);
            sendRawFixMessage(encodedFixMessage, withLogging);

            if (withLogging)
                logger.info("Fix Message response : " + encodedFixMessage);

            String messageId = FixMessageTools.getTagValueByRegex(encodedFixMessage, FixTags.MSG_ID.tag);

            if (withTracking && messageId != null)
                unSentMessages.put(messageId, encodedFixMessage);

        }
        else
            logger.info("Cannot send response. Server can't be reached");
    }

    public void sendUnsentMessages(String targetCompId) {
        for (String message: unSentMessages.values()) {
            String messageTargetCompId = FixMessageTools.getTagValueByRegex(message, FixTags.TARGET_COMP_ID.tag);
            if (targetCompId.equals(messageTargetCompId))
                sendRawFixMessage(message, true);
        }
    }

    private void sendRawFixMessage(String rawFixMessage, boolean withLogging) {
        channel.writeAndFlush(rawFixMessage + "\r\n");
        if (withLogging)
            logger.info("Fix Message request : " + rawFixMessage);
    }
}