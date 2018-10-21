package za.co.wethinkcode.mmayibo.fixme.core.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import za.co.wethinkcode.mmayibo.fixme.core.ChannelGroupHashed;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixEncode;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessageBuilder;

class ServerHandler extends SimpleChannelInboundHandler<String> {
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
        sendIdToClient(ctx);
    }

    private void sendIdToClient(ChannelHandlerContext ctx) {
        FixMessage responseIdBackToChannelFixMessage = new FixMessageBuilder()
                .newFixMessage()
                .withMessageType("1")
                .withText(ctx.channel().id().toString())
                .getFixMessage();

        String fixStringResponseBackToChannel = FixEncode.encode(responseIdBackToChannelFixMessage);
        ctx.channel().writeAndFlush(fixStringResponseBackToChannel + "\r\n");
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String rawFixMessage) {
        server.messageRead(rawFixMessage, ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}