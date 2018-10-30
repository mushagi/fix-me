package za.co.wethinkcode.mmayibo.fixme.core.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import za.co.wethinkcode.mmayibo.fixme.core.ResponseFuture;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.*;

import java.util.logging.Logger;

class ClientHandler extends SimpleChannelInboundHandler<String> {
    private final Logger logger = Logger.getLogger(getClass().getName());
    private final Client client;
    private final ResponseFuture responseFuture;

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
            String unsentMessage = client.unSentMessages.get(message.getMessageId());
            if (unsentMessage != null){
                client.unSentMessages.remove(message.getMessageId());
                logger.info("removed message" + message.getMessageId());
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
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        logger.info("Channel is no longer connected with the server. Trying to connect again...");
        if (!client.isBeingShutdown)
            client.doConnect();
    }
}