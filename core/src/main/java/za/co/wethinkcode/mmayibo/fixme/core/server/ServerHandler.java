package za.co.wethinkcode.mmayibo.fixme.core.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.util.concurrent.Promise;
import lombok.Getter;
import lombok.Setter;
import za.co.wethinkcode.mmayibo.fixme.core.ResponseFuture;

import java.util.concurrent.SynchronousQueue;

public class ServerHandler extends SimpleChannelInboundHandler<String> {
    private final ChannelGroup channels;
    private final Server server;

    @Getter @Setter
    private  ResponseFuture responseFuture;

    ServerHandler(ChannelGroup channels, Server server) {
        this.channels = channels;
        this.server = server;
        this.responseFuture = server.getResponseFuture();
    }

    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
        ctx.writeAndFlush("Welcome client\r\n");
        channels.add(ctx.channel());
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) {
        if (responseFuture != null)
            responseFuture.set(msg);
        server.messageRead(ctx, msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }


}
