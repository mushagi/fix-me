package za.co.wethinkcode.mmayibo.fixme.core.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import za.co.wethinkcode.mmayibo.fixme.core.ChannelGroupHashed;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.*;

import java.util.logging.Logger;

@ChannelHandler.Sharable
class ServerHandler extends SimpleChannelInboundHandler<String> {
    protected final Logger logger = Logger.getLogger(getClass().getName());
    private final ChannelGroupHashed channels;
    private final Server server;

    ServerHandler(Server server) {
        this.channels = server.channels;
        this.server = server;
    }

    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
        int id = server.generateId();
        channels.add(server.generateId(), ctx.channel());
        server.channelActive(ctx, id);
        server.sendIdToClient(ctx.channel(), id);
    }



    @Override
    public void channelRead0(ChannelHandlerContext ctx, String rawFixMessage) {
        if (FixMessageTools.isValidMessage(rawFixMessage))
            server.messageRead(rawFixMessage, ctx);
        else
            sendRejectedInvalidFixMessage(ctx, rawFixMessage);
    }

    private void sendRejectedInvalidFixMessage(ChannelHandlerContext ctx, String rawFixMessage) {
        FixMessage rejectedMessage = new FixMessageBuilder()
                .newFixMessage()
                .withMessageType("3")
                .withText(rawFixMessage)
                .getFixMessage();
        String encodedFix = FixEncode.encode(rejectedMessage);
        ctx.channel().writeAndFlush(encodedFix + "\r\n");

        logger.info("Invalid fix message : " + rawFixMessage);
    }

    void sendMessage(Channel senderChannel, Channel targetChannel, String rawFixMessage) {
        if (targetChannel != null)
            targetChannel.writeAndFlush(  rawFixMessage + "\r\n")
                    .addListener(future -> sendMessageStatus(senderChannel, future.isSuccess(), rawFixMessage));
        else
            logger.info("Target channel doesn't exist ");
    }

    void sendMessage(Channel senderChannel, ChannelGroup channels, String rawFixMessage) {
        channels.writeAndFlush(  rawFixMessage + "\r\n");
    }

    private FixMessageBuilder routedStatusMessageBuilder = new FixMessageBuilder()
            .newFixMessage()
            .withMessageType("400");

    public void sendMessageStatus(Channel channel, boolean status, String rawFixMessage) {
        if (channel != null){
            String messageId = FixMessageTools.getTagValueByRegex(rawFixMessage, FixTags.MSG_ID.tag);
            String senderCompId = FixMessageTools.getTagValueByRegex(rawFixMessage, FixTags.SENDER_COMP_ID.tag);
            String targetCompId = FixMessageTools.getTagValueByRegex(rawFixMessage, FixTags.TARGET_COMP_ID.tag);

            routedStatusMessageBuilder.withTestReqId(String.valueOf(status));
            routedStatusMessageBuilder.withMessageId(messageId);
            routedStatusMessageBuilder.withSenderCompId(senderCompId);
            routedStatusMessageBuilder.withTargetCompId(targetCompId);
            String offlineResponse = FixEncode.encode(routedStatusMessageBuilder.getFixMessage());
            channel.writeAndFlush(offlineResponse+"\r\n");
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
