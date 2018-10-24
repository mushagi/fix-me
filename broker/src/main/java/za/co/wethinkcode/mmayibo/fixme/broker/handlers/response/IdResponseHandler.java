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
    IRepository repository;

    IdResponseHandler(Broker client, FixMessage responseMessage, String rawFixMessage) {
        this.client = client;
        repository = client.repository;
        this.responseMessage = responseMessage;
        this.rawFixMessage = rawFixMessage;
    }

    @Override
    public void processMessage() {
        logger.info("Raw Fix Message read: " + rawFixMessage
                + "\nBroker Id : " + client.networkId
        );
        client.networkId = responseMessage.getText();
        if (client.networkId.equals("-1")){
            client.networkId = responseMessage.getText();
            client.user.setNetworkId(Integer.parseInt(client.networkId));
            repository.update(client.user);
        }
        else
            client.registerNetworkId(responseMessage.getText());
        client.initialize();
    }
}