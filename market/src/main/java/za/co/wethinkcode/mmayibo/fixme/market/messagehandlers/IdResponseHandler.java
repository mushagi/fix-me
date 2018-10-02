package za.co.wethinkcode.mmayibo.fixme.market.messagehandlers;

import io.netty.channel.ChannelHandlerContext;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessageHandler;
import za.co.wethinkcode.mmayibo.fixme.market.Market;

public class IdResponseHandler implements FixMessageHandlerResponse {
    @Override
    public void next(FixMessageHandler next) {

    }

    @Override
    public void handleMessage(ChannelHandlerContext ctx, FixMessage fixMessage, Market market) {
        market.marketData.setId(fixMessage.getMDReqID());
        System.out.println("Market Id : " + market.marketData.getId());
        market.startTimer();
    }
}
