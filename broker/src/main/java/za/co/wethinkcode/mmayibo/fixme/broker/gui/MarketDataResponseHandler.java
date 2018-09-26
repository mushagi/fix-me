package za.co.wethinkcode.mmayibo.fixme.broker.gui;

import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessageHandler;

import java.util.ArrayList;

public class MarketDataResponseHandler implements FixMessageHandlerResponse {
    FixMessage fixMessage;

    @Override
    public void next(FixMessageHandler next) {
        //next.handleMessage(fixMessage, ctx.channel(), responseChannels);
    }

    @Override
    public void handleMessage(FixMessage fixMessage, BrokerInterface brokerInterface) {
        if (fixMessage.getRequestOrResponse().equals("1"))
        {
            ArrayList<MarketData> markets = getMarkets(fixMessage.getMessage());
            brokerInterface.updateMarkets(markets);
        }

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

                    marketData.getInstruments().add(new Instrument("randy"));
                    marketData.getInstruments().add(new Instrument("whatever"));
                    marketData.getInstruments().add(new Instrument("rasdad"));

                }
                return markets;
            }
        }
        return null;
    }


}
