package za.co.wethinkcode.mmayibo.fixme.data.handlers.transaction;

import za.co.wethinkcode.mmayibo.fixme.data.DataClient;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessageBuilder;
import za.co.wethinkcode.mmayibo.fixme.data.persistence.IRepository;

import javax.xml.crypto.Data;

import static za.co.wethinkcode.mmayibo.fixme.data.persistence.FixPersisitenceTools.decodeEntity;
import static za.co.wethinkcode.mmayibo.fixme.data.persistence.FixPersisitenceTools.getClassType;

public class CreateTransactionProcessor extends IDbTransactionProcessor {

    public CreateTransactionProcessor(DataClient dataClient) {
        super(dataClient);
    }

    @Override
    public FixMessage process(FixMessage request) {
        FixMessageBuilder responseBuilder = createFixResponseBuilder(request);
        Class<?> entityClassType = getClassType(request.getDbTableName());
        Object entity = decodeEntity(request.getDbData(), entityClassType);

        if (entity != null && repository.create(entity) != null)
            responseBuilder.withDbStatus("success");
        else
            responseBuilder.withDbStatus("failed");
        return responseBuilder.getFixMessage();
    }



}
