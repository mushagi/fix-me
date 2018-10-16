package za.co.wethinkcode.mmayibo.fixme.data.handlers.messages;

import za.co.wethinkcode.mmayibo.fixme.data.DataClient;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessageBuilder;
import za.co.wethinkcode.mmayibo.fixme.data.handlers.transaction.IDbTransactionProcessor;
import za.co.wethinkcode.mmayibo.fixme.data.model.BrokerUser;
import za.co.wethinkcode.mmayibo.fixme.data.persistence.FixPersisitenceTools;

import java.util.logging.Logger;

public class AuthRequestHandler extends IDbTransactionProcessor implements IFixMessageHandler {

    private final Logger logger = Logger.getLogger(getClass().getName());

    private final FixMessage request;

    AuthRequestHandler(FixMessage request, DataClient dataClient) {
        super(dataClient);
        this.request = request;
    }

    @Override
    public FixMessage process(FixMessage request) throws InterruptedException {
        FixMessageBuilder responseBuilder = createFixResponseBuilder(request)
                .withMessageType("authresponse")
                .withAuthStatus("failed");

        Class<?> entityClassType = FixPersisitenceTools.getClassType(request.getDbTableName());

        if (entityClassType == null) return responseBuilder.getFixMessage();

        if (request.authRequestType.equals("signup"))
            signUp(responseBuilder, entityClassType);
        else if (request.authRequestType.equals("signin"))
            signIn(responseBuilder, entityClassType);

        logger.info("Auth status  : " +
                responseBuilder.getFixMessage().getAuthStatus() + ". " +
                responseBuilder.getFixMessage().getMessage());

        return responseBuilder.getFixMessage();
    }

    private void signIn(FixMessageBuilder responseBuilder, Class<?> entityClassType) throws InterruptedException {
        logger.info("Sign in : " + request.getDbData());
        String username =  extractString(request.getDbData(), "username");

        if (repository.getByID(username, entityClassType) != null)
                responseBuilder.withAuthStatus("success");
        else
            responseBuilder.withMessage("user exists does not exist");
    }

    private void signUp(FixMessageBuilder responseBuilder, Class<?> entityClassType) throws InterruptedException {
        logger.info("Signing up : " + request.getDbData());
        String username =  extractString(request.getDbData(), "username");
        String name = extractString(request.getDbData(), "name");
        logger.info("user name " + username + " name " + name);



        if (repository.getByID(username, entityClassType) == null) {
            Object createFromEntity = createObjectFromRequest();
            Object entity = repository.create(createFromEntity);
            if (entity != null)
                responseBuilder.withAuthStatus("success");
            else
                responseBuilder.withMessage("could not create user");
        }
        else
            responseBuilder.withMessage("user exists");

    }

    private BrokerUser createObjectFromRequest() {

        switch (request.getDbTableName())
        {
            case "broker":
                String username =  extractString(request.getDbData(), "username");
                String name = extractString(request.getDbData(), "name");

                return new BrokerUser(username, name);
        }
        return null;
    }

    private String extractString(String dbData, String key) {
        int firstIndex = dbData.lastIndexOf(key+ ":") + key.length() + 2;
        int lastIndex = dbData.indexOf("\"", firstIndex);
        return dbData.substring(firstIndex, lastIndex);

    }

    @Override
    public void process() throws InterruptedException {
        FixMessage response = process(request);
        dataClient.sendResponse(response);
    }
}
