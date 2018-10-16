package za.co.wethinkcode.mmayibo.fixme.router.handlers.request;

import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessageHandler;
import za.co.wethinkcode.mmayibo.fixme.router.handlers.reponse.MarketDataSnapShotResponseHandler;

public class FixMessageTool {
    public static FixMessageHandler getMessageHandler(FixMessage fixMessage) {
        if (fixMessage.getMessageType() != null)
        {
            switch (fixMessage.getMessageType()) {
                case "D":
                    return new NewOrderRequestHandler();
                case "V":
                    return new MarketsDataSnapDataRequestHandler();
                case "W":
                    return new MarketDataSnapShotResponseHandler();
                case "authrequest":
                    return new DbHandlerHandler();
                case "authresponse":
                case "dbresponse":
                    return new DbResponseHandler();
                case "dbrequest" :
                    return new DbHandlerHandler();

            }
        }
        return new InvalidResponseRequestHandler();
    }
}