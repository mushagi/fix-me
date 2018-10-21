package za.co.wethinkcode.mmayibo.fixme.broker.handlers.response;

import za.co.wethinkcode.mmayibo.fixme.broker.Broker;
import za.co.wethinkcode.mmayibo.fixme.broker.model.domain.BrokerUser;
import za.co.wethinkcode.mmayibo.fixme.broker.model.domain.OwnedInstrument;
import za.co.wethinkcode.mmayibo.fixme.core.IMessageHandler;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.core.model.TradeTransaction;
import za.co.wethinkcode.mmayibo.fixme.core.persistence.IRepository;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class ExecutionReportHandler implements IMessageHandler {
    private final String rawFixMessage;
    private final Logger logger = Logger.getLogger(getClass().getName());
    private final Broker client;
    private final IRepository repository;
    private final BrokerUser user;
    private final FixMessage responseMessage;

    ExecutionReportHandler(Broker client, FixMessage responseMessage, String rawFixMessage) {
        this.client = client;
        this.repository = client.repository;
        this.user = client.user;
        this.responseMessage = responseMessage;
        this.rawFixMessage = rawFixMessage;
    }

    public void processMessage() {
        logger.info("Raw Fix Message read: " + rawFixMessage
                + "\nExecution report of Client Order : " +
                "{"+responseMessage.getClOrderId()+"}. " +
                "Status : "+ responseMessage.getOrdStatus()
        );

        TradeTransaction transaction = createTransaction(responseMessage);
       // client.transactions.put(transaction.getClientOrderId(), transaction);

        createUpdatePurchasedInstrument();
        repository.update(client.user);

        client.updateTransactions(transaction);
    }



    private void createUpdatePurchasedInstrument() {
        ConcurrentHashMap<String, OwnedInstrument> ownedInstruments = client.user.getInstruments();

        String instrumentId = responseMessage.getSymbol();
        int purchasedQuantity = responseMessage.getOrderQuantity();

        OwnedInstrument ownedInstrument = ownedInstruments.get(instrumentId);
        if (ownedInstrument == null){
            ownedInstrument = new OwnedInstrument(instrumentId);
            ownedInstruments.put(ownedInstrument.getId(), ownedInstrument);
        }
        ownedInstrument.setQuantity(purchasedQuantity);
    }

    private void saveTransactionToDatabase(TradeTransaction transaction) {
        repository.create(transaction);
    }

    private TradeTransaction createTransaction(FixMessage message) {
        TradeTransaction transaction = new TradeTransaction();

        transaction.setClient(message.getClientId());
        transaction.setClientOrderId(UUID.fromString(message.getClOrderId()));
        transaction.setSide(message.getSide());
        transaction.setSymbol(message.getSymbol());
        transaction.setOrderStatus(message.getOrdStatus());
        transaction.setText(message.getText());
        transaction.setPrice(message.getPrice());
        transaction.setQuantity(message.getOrderQuantity());
        transaction.setClient(user.getUserName());

      //  saveTransactionToDatabase(transaction);

        return transaction;

    }


}
