package za.co.wethinkcode.mmayibo.fixme.market.messagehandlers;

import io.netty.channel.ChannelHandlerContext;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessageHandler;
import za.co.wethinkcode.mmayibo.fixme.market.MarketClient;

public interface FixMessageHandlerResponse {
    void next(FixMessageHandler next);
     void handleMessage(ChannelHandlerContext ctx, FixMessage fixMessage, MarketClient marketClient);
}
