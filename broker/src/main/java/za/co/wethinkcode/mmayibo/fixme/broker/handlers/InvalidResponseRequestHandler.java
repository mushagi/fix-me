package za.co.wethinkcode.mmayibo.fixme.broker.handlers;


import za.co.wethinkcode.mmayibo.fixme.broker.handlers.response.FixMessageHandlerResponse;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessageHandler;

import java.util.logging.Logger;

public class InvalidResponseRequestHandler implements FixMessageHandlerResponse {
    private final String rawFixMessage;
    private Logger logger = Logger.getLogger(getClass().getName());

    public InvalidResponseRequestHandler(String rawFixMessage) {
        this.rawFixMessage = rawFixMessage;
    }

    @Override
    public void next(FixMessageHandler next) {

    }

    @Override
    public void processMessage() {
        logger.info("Raw Fix Message read: " + rawFixMessage);
        logger.warning("Response/Request message type doesn't have a handler or A non-synchronous call may have been called");
    }
}
