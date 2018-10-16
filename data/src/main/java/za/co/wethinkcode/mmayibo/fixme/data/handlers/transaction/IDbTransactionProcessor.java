package za.co.wethinkcode.mmayibo.fixme.data.handlers.transaction;

import za.co.wethinkcode.mmayibo.fixme.data.DataClient;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessageBuilder;
import za.co.wethinkcode.mmayibo.fixme.data.persistence.IRepository;

public abstract class IDbTransactionProcessor {
    protected final DataClient dataClient;
    protected IRepository repository;

    public IDbTransactionProcessor(DataClient dataClient) {
        this.repository = dataClient.repository;
        this.dataClient = dataClient;
    }

    public abstract FixMessage process(FixMessage request) throws InterruptedException;

    protected FixMessageBuilder createFixResponseBuilder(FixMessage fixMessage) {
        return new FixMessageBuilder()
                .newFixMessage()
                .withMessageType("dbresponse")
                .withMessageId(fixMessage.getMessageId())
                .withSenderCompId(fixMessage.getTargetCompId())
                .withTargetCompId(fixMessage.getSenderCompId())
                .withClOrderId(fixMessage.getClOrderId())
                .withClientIId(fixMessage.getClientId());
    }

}
