package za.co.wethinkcode.mmayibo.fixme.broker.handlers;


import za.co.wethinkcode.mmayibo.fixme.broker.Broker;
import za.co.wethinkcode.mmayibo.fixme.broker.handlers.response.FixMessageHandlerResponse;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessageHandler;

import java.util.logging.Logger;

public class InvalidResponseRequestHandler implements FixMessageHandlerResponse {
    private Logger logger = Logger.getLogger(getClass().getName());

    @Override
    public void next(FixMessageHandler next) {

    }

    @Override
    public void handleMessage(FixMessage fixMessage, Broker broker) {
        logger.warning("Invalid response");
    }
}
