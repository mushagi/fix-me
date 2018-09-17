package za.co.wethinkcode.mmayibo.fixme.core.router;

import io.netty.channel.ChannelHandlerContext;
import za.co.wethinkcode.mmayibo.fixme.core.server.Server;

public class MarketRouter extends Server {

    MarketRouter(String host, int port) {
        super(host, port);
        State.marketChannels = getChannels();
    }

    @Override
    public void messageRead(ChannelHandlerContext ctx, String message) {
        System.out.println(message);
    }

}
