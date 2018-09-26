package za.co.wethinkcode.mmayibo.fixme.core.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import za.co.wethinkcode.mmayibo.fixme.core.ChannelGroupHashed;

class ServerHandler extends SimpleChannelInboundHandler<String> {
    private final ChannelGroupHashed channels;
    private final Server server;

    ServerHandler(ChannelGroupHashed channels, Server server) {
        this.channels = channels;
        this.server = server;
    }

    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
        System.out.println(ctx.channel().id().toString());
        channels.add(ctx.channel());
        server.channelActive(ctx);
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) {
        server.messageRead(ctx, msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
