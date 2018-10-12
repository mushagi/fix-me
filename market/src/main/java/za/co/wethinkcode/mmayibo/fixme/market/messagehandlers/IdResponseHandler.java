package za.co.wethinkcode.mmayibo.fixme.market.messagehandlers;

import io.netty.channel.ChannelHandlerContext;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessageHandler;
import za.co.wethinkcode.mmayibo.fixme.market.MarketClient;

public class IdResponseHandler implements FixMessageHandlerResponse {
    @Override
    public void next(FixMessageHandler next) {

    }

    @Override
    public void handleMessage(ChannelHandlerContext ctx, FixMessage fixMessage, MarketClient marketClient) {
        marketClient.marketModel.setUserName(fixMessage.getMDReqID());
        System.out.println("MarketClient Id : " + marketClient.marketModel.getUserName());
        marketClient.startTimer();
    }
}
