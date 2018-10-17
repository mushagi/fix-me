package za.co.wethinkcode.mmayibo.fixme.market.handlers;

import io.netty.channel.ChannelHandlerContext;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessageHandler;
import za.co.wethinkcode.mmayibo.fixme.market.MarketClient;

public interface FixMessageHandlerResponse {
    void next(FixMessageHandler next);
     void handleMessage(FixMessage message);
}
