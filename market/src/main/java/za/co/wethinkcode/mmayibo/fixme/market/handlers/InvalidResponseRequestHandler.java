package za.co.wethinkcode.mmayibo.fixme.market.handlers;

import za.co.wethinkcode.mmayibo.fixme.core.IMessageHandler;

import java.util.logging.Logger;

public class InvalidResponseRequestHandler implements IMessageHandler {
    private final Logger logger = Logger.getLogger(getClass().getName());

    private final String rawFixMessage;

    InvalidResponseRequestHandler(String rawFixMessage) {
        this.rawFixMessage = rawFixMessage;
    }

    @Override
    public void processMessage() {
        logger.info("Fix message read : " + rawFixMessage);

    }

}
