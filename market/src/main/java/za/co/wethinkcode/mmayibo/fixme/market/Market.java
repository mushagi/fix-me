package za.co.wethinkcode.mmayibo.fixme.market;

import io.netty.channel.ChannelHandlerContext;
import za.co.wethinkcode.mmayibo.fixme.core.client.Client;

public class Market extends Client {

    public Market(String host, int port) {
        super(host, port);
    }

    @Override
    public void messageRead(ChannelHandlerContext ctx, String message) {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(message);
        ctx.writeAndFlush("I got it bro" + message + "\r\n") ;
    }
}
