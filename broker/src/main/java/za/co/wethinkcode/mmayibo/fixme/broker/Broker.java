package za.co.wethinkcode.mmayibo.fixme.broker;

import io.netty.channel.ChannelHandlerContext;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import za.co.wethinkcode.mmayibo.fixme.broker.messagehandlers.BrokerMessageHandlerTool;
import za.co.wethinkcode.mmayibo.fixme.broker.messagehandlers.FixMessageHandlerResponse;
import za.co.wethinkcode.mmayibo.fixme.core.client.Client;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixDecoder;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.core.model.MarketData;


public class Broker extends Client {

    public Broker(String host, int port) {
        super(host, port);
    }


    public ObservableList<MarketData> markets = FXCollections.observableArrayList();

    @Override
    public void messageRead(ChannelHandlerContext ctx, String message) {
        FixMessage fixMessage = FixDecoder.decode(message);
        FixMessageHandlerResponse messageHandler = BrokerMessageHandlerTool.getMessageHandler(fixMessage);
        assert messageHandler != null;
        messageHandler.handleMessage(fixMessage, this);

        System.out.println(message);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {

    }

    public void updateMarkets(MarketData marketData) {
        if (!markets.contains(marketData))
            markets.add(marketData);
        else {
            for (MarketData localMarketData: markets)
                if (localMarketData.equals(marketData)) {
                    localMarketData.setInstruments(marketData.getInstruments());
                    break;
                }
        }
    }
}
