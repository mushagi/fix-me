package za.co.wethinkcode.mmayibo.fixme.broker.handlers.response;


import za.co.wethinkcode.mmayibo.fixme.broker.Broker;
import za.co.wethinkcode.mmayibo.fixme.broker.model.domain.BrokerUser;
import za.co.wethinkcode.mmayibo.fixme.core.IMessageHandler;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.core.persistence.IRepository;

import java.util.logging.Logger;

public class IdResponseHandler implements IMessageHandler {
    private final String rawFixMessage;
    private final Logger logger = Logger.getLogger(getClass().getName());
    private final Broker client;
    private final FixMessage responseMessage;

    IdResponseHandler(Broker client, FixMessage responseMessage, String rawFixMessage) {
        this.client = client;
        IRepository repository = client.repository;
        BrokerUser user = client.user;
        this.responseMessage = responseMessage;
        this.rawFixMessage = rawFixMessage;
    }

    @Override
    public void processMessage() {
        client.networkId = responseMessage.getText();

        logger.info("Raw Fix Message read: " + rawFixMessage
                + "\nBroker Id : " + client.networkId
        );
    }
}