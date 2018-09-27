package za.co.wethinkcode.mmayibo.fixme.market;

import io.netty.channel.ChannelHandlerContext;
import za.co.wethinkcode.mmayibo.fixme.core.client.Client;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixDecoder;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.core.model.Instrument;
import za.co.wethinkcode.mmayibo.fixme.core.model.MarketData;
import za.co.wethinkcode.mmayibo.fixme.market.messagehandlers.FixMessageHandlerResponse;
import za.co.wethinkcode.mmayibo.fixme.market.messagehandlers.MarketMessageHandlerTool;

public class Market extends Client {

    Market(String host, int port) {
        super(host, port);
    }
    MarketData marketData = new MarketData();
    @Override
    public void messageRead(ChannelHandlerContext ctx, String message) {
        FixMessage fixMessage = FixDecoder.decode(message);
        FixMessageHandlerResponse messageHandler = MarketMessageHandlerTool.getMessageHandler(fixMessage);
        assert messageHandler != null;

        marketData.setId("4689");
        marketData.setName("dgdsg");
        marketData.getInstruments().add(new Instrument("bdfgd", 1));
        marketData.getInstruments().add(new Instrument("dsaddass", 2));
        marketData.getInstruments().add(new Instrument("sdassadsad", 3));
        marketData.getInstruments().add(new Instrument("dasdasddsaasd", 4));

        messageHandler.handleMessage(ctx, fixMessage, marketData);

        System.out.println(message);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {

    }
}
