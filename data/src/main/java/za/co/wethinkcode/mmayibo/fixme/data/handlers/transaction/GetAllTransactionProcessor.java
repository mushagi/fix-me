package za.co.wethinkcode.mmayibo.fixme.data.handlers.transaction;

import za.co.wethinkcode.mmayibo.fixme.data.DataClient;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessageBuilder;
import za.co.wethinkcode.mmayibo.fixme.data.persistence.IRepository;

import javax.xml.crypto.Data;
import java.util.Collection;

import static za.co.wethinkcode.mmayibo.fixme.data.persistence.FixPersisitenceTools.encodeEntity;
import static za.co.wethinkcode.mmayibo.fixme.data.persistence.FixPersisitenceTools.getClassType;

public class GetAllTransactionProcessor extends IDbTransactionProcessor {
    public GetAllTransactionProcessor(DataClient dataClient) {
        super(dataClient);

    }

    @Override
    public FixMessage process(FixMessage request) throws InterruptedException {
        FixMessageBuilder responseBuilder = createFixResponseBuilder(request);
        Class<?> entityClassType = getClassType(request.getDbTableName());
        if (entityClassType != null) {
            Collection<?> entities = repository.getAll(entityClassType);
            String entitiesEncoded = encodeEntity(entities);
            responseBuilder.withDbData(entitiesEncoded);
            responseBuilder.withDbTable(request.getDbTableName());
            responseBuilder.withDbStatus("success");
        }
        else
            responseBuilder.withDbStatus("failed");
        return responseBuilder.getFixMessage();
    }
}
