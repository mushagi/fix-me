package za.co.wethinkcode.mmayibo.fixme.broker.messagehandlers;

import za.co.wethinkcode.mmayibo.fixme.broker.Broker;
import za.co.wethinkcode.mmayibo.fixme.broker.gui.BrokerInterface;
import za.co.wethinkcode.mmayibo.fixme.core.model.MarketData;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessageHandler;

import java.util.ArrayList;

public class MarketDataListResponseHandler implements FixMessageHandlerResponse {
    Broker broker;

    @Override
    public void next(FixMessageHandler next) {
    }

    @Override
    public void handleMessage(FixMessage fixMessage, BrokerInterface brokerInterface, Broker broker) {
        this.broker = broker;
        ArrayList<MarketData> markets = getMarkets(fixMessage.getMessage());
    }

    private ArrayList<MarketData> getMarkets(String message) {

        if (message != null) {
            String strings[] = message.split(",");
            if (message.length() > 0) {
                ArrayList<MarketData> markets = new ArrayList<>();
                for (String string : strings) {
                    MarketData marketData = new MarketData();
                    marketData.setId(string);
                    marketData.setName("");
                    markets.add(marketData);
                    broker.requestMarketData(marketData);
                }
                return markets;
            }
        }
        return null;
    }


}
