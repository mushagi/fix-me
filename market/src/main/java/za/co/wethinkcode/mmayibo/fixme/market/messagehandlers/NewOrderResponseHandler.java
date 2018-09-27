package za.co.wethinkcode.mmayibo.fixme.market.messagehandlers;

import io.netty.channel.ChannelHandlerContext;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessageHandler;
import za.co.wethinkcode.mmayibo.fixme.core.model.MarketData;

public class NewOrderResponseHandler implements FixMessageHandlerResponse {

    @Override
    public void next(FixMessageHandler next) {

    }

    @Override
    public void handleMessage(ChannelHandlerContext ctx, FixMessage fixMessage, MarketData marketData) {

    }
}
