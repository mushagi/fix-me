package za.co.wethinkcode.mmayibo.fixme.market.handlers;

import za.co.wethinkcode.mmayibo.fixme.core.IMessageHandler;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.market.MarketClient;

public class MarketMessageHandlerTool {

    public static IMessageHandler getMessageHandler(FixMessage message, MarketClient client, String rawFixMessage) {
        if (message.getMessageType() != null){
            switch (message.getMessageType()) {
                case "1":
                    return new IdResponseHandler(client, message, rawFixMessage);
                case "D":
                    return new NewOrderRequestHandler(message, client, rawFixMessage);
            }
        }
        return new InvalidResponseRequestHandler(rawFixMessage);
    }
}