package za.co.wethinkcode.mmayibo.fixme.broker.handlers.response;

import za.co.wethinkcode.mmayibo.fixme.broker.Broker;
import za.co.wethinkcode.mmayibo.fixme.broker.model.domain.Instrument;
import za.co.wethinkcode.mmayibo.fixme.broker.model.domain.Market;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessageHandler;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class MarketDataSnapShotResponseHandler implements FixMessageHandlerResponse {
    private Logger logger = Logger.getLogger(getClass().getName());

    @Override
    public void next(FixMessageHandler next) {

    }

    @Override
    public void handleMessage(FixMessage message, Broker broker) {
        ConcurrentHashMap<String, Instrument> instruments = createInstruments(message.getSymbol());
        String mdReqId = message.getMDReqID();
        String marketName = message.getMdName();
        String marketNetworkId = message.getSenderCompId();
        String uniqueId = mdReqId + marketNetworkId;

        logger.info("Received snapshot market "  + instruments );

        Market market = broker.markets.get(uniqueId);

        if (market == null){
            market = new Market(marketName, mdReqId, instruments, marketNetworkId);
            broker.markets.put(uniqueId, market);
        }
        else
            market.updateInstruments(instruments);
        broker.marketsUpdated();
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
