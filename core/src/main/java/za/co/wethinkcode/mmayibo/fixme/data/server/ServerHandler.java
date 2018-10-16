package za.co.wethinkcode.mmayibo.fixme.data.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import za.co.wethinkcode.mmayibo.fixme.data.ChannelGroupHashed;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixDecoder;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixEncode;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessageBuilder;

import java.util.logging.Logger;

class ServerHandler extends SimpleChannelInboundHandler<String> {
    private final Logger logger = Logger.getLogger(getClass().getName());
    private final ChannelGroupHashed channels;
    private final Server server;

    ServerHandler(ChannelGroupHashed channels, Server server) {
        this.channels = channels;
        this.server = server;
    }

    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
        channels.add(ctx.channel());
        server.channelActive(ctx);
        sendBackIdToClient(ctx);
    }

    private void sendBackIdToClient(ChannelHandlerContext ctx) {
        FixMessage responseIdBackToChannelFixMessage = new FixMessageBuilder()
                .newFixMessage()
                .withMessageType("1")
                .withMessage(ctx.channel().id().toString())
                .getFixMessage();

        String fixStringResponseBackToChannel = FixEncode.encode(responseIdBackToChannelFixMessage);
        ctx.channel().writeAndFlush(fixStringResponseBackToChannel + "\r\n");
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String message) {
        logger.info("Fix Message read: " + message);
        FixMessage fixMessage = FixDecoder.decode(message);
        server.messageRead(ctx, fixMessage);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}
