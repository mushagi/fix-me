package za.co.wethinkcode.mmayibo.fixme.broker.handlers.response;

import za.co.wethinkcode.mmayibo.fixme.core.IMessageHandler;

import java.util.logging.Logger;

public class RejectMessageHandler implements IMessageHandler {
    private Logger logger = Logger.getLogger(getClass().getName());

    @Override
    public void processMessage() {
        logger.info("Request has been rejected");
    }
}
