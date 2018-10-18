package za.co.wethinkcode.mmayibo.fixme.broker.handlers.response;

import za.co.wethinkcode.mmayibo.fixme.broker.Broker;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessageHandler;

import java.util.logging.Logger;

public class RejectMessageHandler implements FixMessageHandlerResponse {
    private Logger logger = Logger.getLogger(getClass().getName());

    @Override
    public void next(FixMessageHandler next) {

    }

    @Override
    public void handleMessage(FixMessage fixMessage, Broker broker) {
        logger.info("Request has been rejected");
    }
}
