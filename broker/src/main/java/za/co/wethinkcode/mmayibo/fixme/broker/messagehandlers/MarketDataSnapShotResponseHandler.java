package za.co.wethinkcode.mmayibo.fixme.broker.messagehandlers;

import za.co.wethinkcode.mmayibo.fixme.broker.Broker;
import za.co.wethinkcode.mmayibo.fixme.core.model.Instrument;
import za.co.wethinkcode.mmayibo.fixme.core.model.MarketData;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessageHandler;

import java.util.ArrayList;
import java.util.HashMap;

public class MarketDataSnapShotResponseHandler implements FixMessageHandlerResponse{

    @Override
    public void next(FixMessageHandler next) {

    }

    @Override
    public void handleMessage(FixMessage fixMessage, Broker broker) {
        MarketData marketData = new MarketData();

        marketData.setId(fixMessage.getMDReqID());

        HashMap<String, Instrument> instruments = getInstruments(fixMessage.getSymbol());
        marketData.setInstruments(instruments);
        marketData.setName(fixMessage.getMdName());

        broker.updateMarkets(marketData);
    }

    private HashMap<String, Instrument> getInstruments(String symbol) {
        HashMap<String, Instrument> instruments = new HashMap<>();
        String[] strings = symbol.split("%");

        for (String line: strings) {
            String[] nameAndPrice = line.split("#");
            if (nameAndPrice.length == 2) {
                String name = nameAndPrice[0];
                double price = Double.parseDouble(nameAndPrice[1]);
                instruments.put(name, new Instrument(name, price));
            }
        }

    return instruments;
    }
}
