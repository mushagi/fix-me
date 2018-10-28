package za.co.wethinkcode.mmayibo.fixme.core.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import za.co.wethinkcode.mmayibo.fixme.core.ChannelGroupHashed;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.*;

import java.util.logging.Logger;

class ServerHandler extends SimpleChannelInboundHandler<String> {
    protected final Logger logger = Logger.getLogger(getClass().getName());
    private final ChannelGroupHashed channels;
    private final Server server;

    ServerHandler(ChannelGroupHashed channels, Server server) {
        this.channels = channels;
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
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
