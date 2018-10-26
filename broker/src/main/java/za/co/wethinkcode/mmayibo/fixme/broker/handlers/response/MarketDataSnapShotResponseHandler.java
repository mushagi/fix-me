package za.co.wethinkcode.mmayibo.fixme.broker.handlers.response;

import za.co.wethinkcode.mmayibo.fixme.broker.Broker;
import za.co.wethinkcode.mmayibo.fixme.broker.model.domain.Instrument;
import za.co.wethinkcode.mmayibo.fixme.broker.model.domain.Market;
import za.co.wethinkcode.mmayibo.fixme.core.IMessageHandler;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessage;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class MarketDataSnapShotResponseHandler implements IMessageHandler {
    private final String rawFixMessage;
    private Logger logger = Logger.getLogger(getClass().getName());
    private final Broker client;

    private final FixMessage responseMessage;

    MarketDataSnapShotResponseHandler(Broker client, FixMessage responseMessage, String rawFixMessage) {
        this.client = client;
        this.responseMessage = responseMessage;
        this.rawFixMessage = rawFixMessage;
    }

    @Override
    public void processMessage() {
        ConcurrentHashMap<String, Instrument> instruments = createInstruments(responseMessage.getSymbol());

        String mdReqId = responseMessage.getMDReqID();
        String marketName = responseMessage.getMdName();
        String marketNetworkId = responseMessage.getSenderCompId();

        Market market = client.markets.get(marketNetworkId);

        if (market == null){
            market = new Market(marketName, mdReqId, instruments, marketNetworkId);
        }
        else
            market.updateInstruments(instruments);
        client.marketsUpdated(market, false);
    }


    private ConcurrentHashMap<String, Instrument> createInstruments(String symbol) {
        ConcurrentHashMap<String, Instrument> instruments = new ConcurrentHashMap<>();

        String[] instrumentStringArray = symbol.split("%");

        for (String instrument: instrumentStringArray) {
            String[] instrumentValues = instrument.split("#");
            if (instrumentValues.length == 3) {
                String id = instrumentValues[0];
                String name = instrumentValues[1];
                double price = Double.parseDouble(instrumentValues[2]);
                instruments.put(id, new Instrument(id, name, price));
            }
        }
        return instruments;
    }

}
