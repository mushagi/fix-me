package za.co.wethinkcode.mmayibo.fixme.market.handlers;

import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.data.persistence.IRepository;

public class MarketMessageHandlerTool {

    public static FixMessageHandlerResponse getMessageHandler(FixMessage fixMessage, IRepository repository) {
        if (fixMessage.getMessageType() != null){
            switch (fixMessage.getMessageType()) {
                case "invalidrequest":
                    break;
                case "authresponse":
                    return new AuthResponseHandler();
                case "1":
                    return new IdResponseHandler();
                case "D":
                    return new NewOrderRequestHandler(repository);
            }
        }
        return new InvalidResponseRequestHandler();
    }
}