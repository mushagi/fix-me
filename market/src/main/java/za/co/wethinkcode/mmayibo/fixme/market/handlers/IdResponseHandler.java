package za.co.wethinkcode.mmayibo.fixme.market.handlers;

import za.co.wethinkcode.mmayibo.fixme.core.IMessageHandler;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessageHandler;
import za.co.wethinkcode.mmayibo.fixme.market.MarketClient;

import java.util.logging.Logger;

public class IdResponseHandler implements IMessageHandler {
    private final Logger logger = Logger.getLogger(getClass().getName());

    private final MarketClient client;
    private final FixMessage responseMessage;
    private final String rawFixMessage;

    public IdResponseHandler(MarketClient client, FixMessage responseMessage, String rawFixMessage) {
        this.client = client;
        this.responseMessage = responseMessage;
        this.rawFixMessage = rawFixMessage;
    }

    @Override
    public void processMessage() {
        logger.info("Fix message read : " + rawFixMessage);
        client.networkId = responseMessage.getText();
        logger.info("MarketClient Id : " + client.networkId);
    }
}
