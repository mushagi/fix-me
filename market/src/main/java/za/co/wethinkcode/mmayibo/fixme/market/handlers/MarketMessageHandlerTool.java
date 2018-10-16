package za.co.wethinkcode.mmayibo.fixme.market.handlers;

import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.data.persistence.IRepository;
import za.co.wethinkcode.mmayibo.fixme.market.MarketClient;

public class MarketMessageHandlerTool {

    public static FixMessageHandlerResponse getMessageHandler(FixMessage fixMessage, MarketClient client) {
        if (fixMessage.getMessageType() != null){
            switch (fixMessage.getMessageType()) {
                case "invalidrequest":
                    break;
                case "authresponse":
                    return new AuthResponseHandler(client);
                case "1":
                    return new IdResponseHandler(client);
                case "D":
                    return new NewOrderRequestHandler(client);
            }
        }
        return new InvalidResponseRequestHandler();
    }
}