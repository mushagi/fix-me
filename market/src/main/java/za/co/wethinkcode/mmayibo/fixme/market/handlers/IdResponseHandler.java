package za.co.wethinkcode.mmayibo.fixme.market.handlers;

import io.netty.channel.ChannelHandlerContext;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessageHandler;
import za.co.wethinkcode.mmayibo.fixme.market.MarketClient;

public class IdResponseHandler implements FixMessageHandlerResponse {
    private final MarketClient client;

    public IdResponseHandler(MarketClient client) {
        this.client = client;
    }

    @Override
    public void next(FixMessageHandler next) {

    }

    @Override
    public void handleMessage(FixMessage fixMessage) {
        client.networkId = fixMessage.getMessage();
        System.out.println("MarketClient Id : " + client.networkId);
    }
}
