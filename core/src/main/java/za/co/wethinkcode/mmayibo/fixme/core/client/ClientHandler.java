package za.co.wethinkcode.mmayibo.fixme.core.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientHandler extends SimpleChannelInboundHandler<String> {
    private final Client client;

    ClientHandler(Client client) {
        this.client = client;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) {
        client.messageRead(ctx, msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
