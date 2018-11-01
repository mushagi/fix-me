package za.co.wethinkcode.mmayibo.fixme.market.handlers;

import za.co.wethinkcode.mmayibo.fixme.core.IMessageHandler;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.market.MarketClient;

import java.util.logging.Logger;

public class MarketSnapShotRequestHandler implements IMessageHandler {
    private final Logger logger = Logger.getLogger(getClass().getName());

    private final FixMessage requestMessage;
    private final String rawFixMessage;
    private final MarketClient client;

    public MarketSnapShotRequestHandler(FixMessage requestMessage, MarketClient client, String rawFixMessage) {
        this.requestMessage = requestMessage;
        this.client = client;
        this.rawFixMessage = rawFixMessage;

    }

    @Override
    public void processMessage() {
        logger.info("Fix message read : " + rawFixMessage);

        client.sendMarketDataSnapShot(requestMessage.getSenderCompId(), requestMessage.getMessageId(), true);
        client.sendUnsentMessages(requestMessage.getTargetCompId());
    }
}
