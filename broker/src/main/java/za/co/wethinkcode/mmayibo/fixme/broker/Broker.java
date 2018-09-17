package za.co.wethinkcode.mmayibo.fixme.broker;

import io.netty.channel.ChannelHandlerContext;
import za.co.wethinkcode.mmayibo.fixme.core.client.Client;

public class Broker extends Client {
    Broker(String host, int port) {
        super(host, port);
    }

    @Override
    public void messageRead(ChannelHandlerContext ctx, String message) {

        System.out.println(message);
    }
}
