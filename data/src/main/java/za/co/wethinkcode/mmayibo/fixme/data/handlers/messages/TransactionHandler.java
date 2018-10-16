package za.co.wethinkcode.mmayibo.fixme.data.handlers.messages;

import za.co.wethinkcode.mmayibo.fixme.data.DataClient;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.data.handlers.transaction.IDbTransactionProcesserTool;
import za.co.wethinkcode.mmayibo.fixme.data.handlers.transaction.IDbTransactionProcessor;
import za.co.wethinkcode.mmayibo.fixme.data.persistence.IRepository;

public class TransactionHandler implements IFixMessageHandler {

    private final FixMessage request;
    private final IRepository repository;
    private final DataClient dataClient;

    public TransactionHandler(FixMessage request, DataClient dataClient) {
        this.dataClient = dataClient;
        this.request = request;
        this.repository = dataClient.repository;
    }

    @Override
    public void process() throws InterruptedException {
        IDbTransactionProcessor transactionProcessor = IDbTransactionProcesserTool.get(request.getDbTransactionType(), dataClient);
        FixMessage response = transactionProcessor.process(request);
        dataClient.sendResponse(response);
    }
}
