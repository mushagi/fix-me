package za.co.wethinkcode.mmayibo.fixme.broker.handlers;


import za.co.wethinkcode.mmayibo.fixme.core.IMessageHandler;

import java.util.logging.Logger;

public class InvalidResponseRequestHandler implements IMessageHandler {
    private final String rawFixMessage;
    private final Logger logger = Logger.getLogger(getClass().getName());

    public InvalidResponseRequestHandler(String rawFixMessage) {
        this.rawFixMessage = rawFixMessage;
    }

    @Override
    public void processMessage() {
        logger.info("Raw Fix Message read: " + rawFixMessage);
        logger.warning("Response/Request message type doesn't have a handler or A non-synchronous call may have been called");
    }
}
