package za.co.wethinkcode.mmayibo.fixme.data.persistence;

import za.co.wethinkcode.mmayibo.fixme.data.client.Client;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessageBuilder;
import za.co.wethinkcode.mmayibo.fixme.data.model.InstrumentModel;
import za.co.wethinkcode.mmayibo.fixme.data.model.MarketModel;

import java.util.Collection;

public class FixRepositoryImp implements IRepository {

    private final Client client;

    public FixRepositoryImp(Client client) {
        this.client = client;
    }

    @Override
    public <T> Collection<T> getAll(Class<T> type) { ;
        FixMessageBuilder builder = createRequestMessageBuilder("getall", type);
        FixMessage response = null;
        try {
            response = client.sendMessageWaitForResponse(builder.getFixMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
        return FixPersisitenceTools.decodeEntities(response.getDbData());
    }

    @Override
    public <T> T getByID(String id, Class<T> type) {
        FixMessageBuilder builder = createRequestMessageBuilder("get", type);
        builder.withDbData(id);
        FixMessage response;
        try {
            response = client.sendMessageWaitForResponse(builder.getFixMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
        return FixPersisitenceTools.decodeEntity(response.getDbData(), type);
    }

    @Override
    public <T> boolean update(T entity) {
        return false;
    }

    @Override
    public <T> T create(T entity) {

        return null;
    }

    @Override
    public <T> boolean delete(T entity) {
        return false;
    }

    @Override
    public <T> boolean createAll(Collection<T> markets) {
        return false;
    }

    @Override
    public <T> Collection<T> getMultipleByIds(Class<T> type, Collection<String> ids) {
        return null;
    }

    private <T> FixMessageBuilder createRequestMessageBuilder(String transactionType, Class<T> type){
        return new FixMessageBuilder()
                .newFixMessage()
                .withMessageType("dbrequest")
                .withDbTransactionType(transactionType)
                .withDbTable(getTable(type));
    }

    private String getTable(Class<?> type) {
        if (type == MarketModel.class)
            return "market";
        else if (type == InstrumentModel.class)
            return "instrument";
        return null;
    }
}
