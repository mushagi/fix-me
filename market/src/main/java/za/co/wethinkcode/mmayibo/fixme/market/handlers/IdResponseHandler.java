package za.co.wethinkcode.mmayibo.fixme.market.handlers;

import za.co.wethinkcode.mmayibo.fixme.core.IMessageHandler;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.core.persistence.IRepository;
import za.co.wethinkcode.mmayibo.fixme.market.MarketClient;

import java.util.logging.Logger;

public class IdResponseHandler implements IMessageHandler {
    private final Logger logger = Logger.getLogger(getClass().getName());

    private final MarketClient client;
    private final FixMessage responseMessage;
    private final String rawFixMessage;
    private final IRepository repository;

    IdResponseHandler(MarketClient client, FixMessage responseMessage, String rawFixMessage) {
        this.client = client;
        this.responseMessage = responseMessage;
        this.rawFixMessage = rawFixMessage;
        this.repository = client.repository;
    }

    @Override
    public void processMessage() {
        logger.info("Fix message read : " + rawFixMessage);
        if (client.networkId.equals("-1")){
            client.networkId = responseMessage.getText();
            client.marketModel.setNetworkId(Integer.parseInt(client.networkId));
            repository.update(client.marketModel);
        }
        else
            client.registerNetworkId(responseMessage.getText());
        logger.info("MarketClient Id : " + client.networkId);
    }
}
