package za.co.wethinkcode.mmayibo.fixme.market.messagehandlers;

import io.netty.channel.ChannelHandlerContext;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixEncode;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessageBuilder;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessageHandler;
import za.co.wethinkcode.mmayibo.fixme.core.model.Instrument;
import za.co.wethinkcode.mmayibo.fixme.core.model.MarketData;
import za.co.wethinkcode.mmayibo.fixme.market.Market;

import java.util.Collection;

public class MarketDataSnapShotRequestHandler implements FixMessageHandlerResponse{

    @Override
    public void next(FixMessageHandler next) {

    }

    @Override
    public void handleMessage(ChannelHandlerContext ctx, FixMessage fixMessage, Market market) {
        market.sendMarketDataSnapShot(fixMessage.getSenderCompId(), ctx.channel());
    }


}
