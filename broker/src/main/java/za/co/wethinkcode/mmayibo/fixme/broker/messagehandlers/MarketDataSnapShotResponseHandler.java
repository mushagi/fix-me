package za.co.wethinkcode.mmayibo.fixme.broker.messagehandlers;

import za.co.wethinkcode.mmayibo.fixme.broker.Broker;
import za.co.wethinkcode.mmayibo.fixme.broker.gui.BrokerInterface;
import za.co.wethinkcode.mmayibo.fixme.core.model.Instrument;
import za.co.wethinkcode.mmayibo.fixme.core.model.MarketData;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessageHandler;

import java.util.ArrayList;

public class MarketDataSnapShotResponseHandler implements FixMessageHandlerResponse{

    @Override
    public void next(FixMessageHandler next) {

    }

    @Override
    public void handleMessage(FixMessage fixMessage, BrokerInterface brokerInterface, Broker broker) {
        MarketData marketData = new MarketData();

        marketData.setId(fixMessage.getMDReqID());

        ArrayList<Instrument> instruments = getInstruments(fixMessage.getSymbol());
        marketData.setInstruments(instruments);
        marketData.setName(fixMessage.getMdName());

        brokerInterface.updateMarketSnapShot(marketData);
    }

    private ArrayList<Instrument> getInstruments(String symbol) {
        ArrayList<Instrument> instruments = new ArrayList<>();
        String[] strings = symbol.split("%");

        for (String line: strings) {
            String[] nameAndPrice = line.split("#");
            if (nameAndPrice.length == 2) {
                String name = nameAndPrice[0];
                double price = Double.parseDouble(nameAndPrice[1]);
                instruments.add(new Instrument(name, price));
            }
        }
        return instruments;
    }
}
