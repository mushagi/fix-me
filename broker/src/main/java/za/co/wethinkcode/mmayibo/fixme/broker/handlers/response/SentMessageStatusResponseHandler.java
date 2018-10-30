package za.co.wethinkcode.mmayibo.fixme.broker.handlers.response;

import za.co.wethinkcode.mmayibo.fixme.broker.Broker;
import za.co.wethinkcode.mmayibo.fixme.broker.model.domain.BrokerUser;
import za.co.wethinkcode.mmayibo.fixme.broker.model.domain.Market;
import za.co.wethinkcode.mmayibo.fixme.core.IMessageHandler;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.core.persistence.IRepository;

import java.util.logging.Logger;

public class SentMessageStatusResponseHandler implements IMessageHandler {
    private final Logger logger = Logger.getLogger(getClass().getName());

    private final String rawFixMessage;
    private final Broker client;
    private final FixMessage responseMessage;

    public SentMessageStatusResponseHandler(Broker client, FixMessage responseMessage, String rawFixMessage) {
        this.client = client;
        this.responseMessage = responseMessage;
        this.rawFixMessage = rawFixMessage;
    }


    @Override
    public void processMessage() {
        boolean isMarketOnline = responseMessage.getTestReqId() != null && responseMessage.getTestReqId().equals("true");
        Market market = client.markets.get(responseMessage.getTargetCompId());

        if (market != null && market.isOnline() != isMarketOnline) {
            market.setOnline(isMarketOnline);
            client.marketsUpdated(market, true);
        }
    }
}
