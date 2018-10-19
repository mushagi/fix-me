package za.co.wethinkcode.mmayibo.fixme.broker.handlers.response;


import za.co.wethinkcode.mmayibo.fixme.broker.Broker;
import za.co.wethinkcode.mmayibo.fixme.broker.model.domain.BrokerUser;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessageHandler;
import za.co.wethinkcode.mmayibo.fixme.data.persistence.IRepository;

import java.util.logging.Logger;

public class IdResponseHandler implements FixMessageHandlerResponse {
    private final String rawFixMessage;
    private Logger logger = Logger.getLogger(getClass().getName());
    private Broker client;
    private IRepository repository;
    private BrokerUser user;
    private FixMessage responseMessage;

    IdResponseHandler(Broker client, FixMessage responseMessage, String rawFixMessage) {
        this.client = client;
        this.repository = client.repository;
        this.user = client.user;
        this.responseMessage = responseMessage;
        this.rawFixMessage = rawFixMessage;
    }

    @Override
    public void next(FixMessageHandler next) {

    }

    @Override
    public void processMessage() {
        client.networkId = responseMessage.getText();

        logger.info("Raw Fix Message read: " + rawFixMessage
                + "\nMarketClient Id : " + client.networkId
        );
    }
}