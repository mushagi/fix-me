package za.co.wethinkcode.mmayibo.fixme.broker.messagehandlers;

import za.co.wethinkcode.mmayibo.fixme.broker.Broker;
import za.co.wethinkcode.mmayibo.fixme.broker.model.BrokerInstrumentModel;
import za.co.wethinkcode.mmayibo.fixme.core.model.InstrumentModel;
import za.co.wethinkcode.mmayibo.fixme.core.model.MarketModel;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessageHandler;

import java.util.HashMap;

public class MarketDataSnapShotResponseHandler implements FixMessageHandlerResponse{

    @Override
    public void next(FixMessageHandler next) {

    }

    @Override
    public void handleMessage(FixMessage fixMessage, Broker broker) {
        MarketModel marketModel = new MarketModel();

        marketModel.setUserName(fixMessage.getMDReqID());

        HashMap<String, InstrumentModel> instruments = getInstruments(fixMessage.getSymbol());
        marketModel.setInstruments(instruments);
        marketModel.setName(fixMessage.getMdName());

        broker.updateMarkets(marketModel);
    }

    private HashMap<String, InstrumentModel> getInstruments(String symbol) {
        HashMap<String, InstrumentModel> instruments = new HashMap<>();
        String[] strings = symbol.split("%");

        for (String line: strings) {
            String[] nameAndPrice = line.split("#");
            if (nameAndPrice.length == 2) {
                String name = nameAndPrice[0];
                double price = Double.parseDouble(nameAndPrice[1]);
                instruments.put(name, new BrokerInstrumentModel(name, price));
            }
        }

    return instruments;
    }
}
