package za.co.wethinkcode.mmayibo.fixme.data.handlers.transaction;

import za.co.wethinkcode.mmayibo.fixme.data.DataClient;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessageBuilder;

import static za.co.wethinkcode.mmayibo.fixme.data.persistence.FixPersisitenceTools.encodeEntity;
import static za.co.wethinkcode.mmayibo.fixme.data.persistence.FixPersisitenceTools.getClassType;

public class GetTransactionProcessor extends IDbTransactionProcessor {
    public GetTransactionProcessor(DataClient dataClient) {
        super(dataClient);
    }

    @Override
    public FixMessage process(FixMessage request) throws InterruptedException {
        FixMessageBuilder responseBuilder = createFixResponseBuilder(request);
        Class<?> entityClassType = getClassType(request.getDbTableName());

        if (entityClassType != null) {
            Object entity = repository.getByID(request.getDbData(), entityClassType);
            String entityEncoded = encodeEntity(entity);
            responseBuilder.withDbData(entityEncoded);
            responseBuilder.withDbTable(request.getDbTableName());
            responseBuilder.withDbStatus("success");
        }
        else
            responseBuilder.withDbStatus("failed");
        return responseBuilder.getFixMessage();
    }
}
