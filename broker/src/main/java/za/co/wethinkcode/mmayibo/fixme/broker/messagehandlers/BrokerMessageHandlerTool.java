package za.co.wethinkcode.mmayibo.fixme.broker.messagehandlers;

import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessage;

public class BrokerMessageHandlerTool {

    public static FixMessageHandlerResponse getMessageHandler(FixMessage fixMessage) {
        if (fixMessage.getMessageType() != null){
            switch (fixMessage.getMessageType()) {
                case "1":
                    return new IdResponseHandler();
                case "3":
                    return new ProcessWalletResponseHandler();
                case "D":
                    return new NewOrderResponseHandler();
                case "W":
                    return new MarketDataSnapShotResponseHandler();
                case "2":
                    return new MarketDataSnapShotResponseHandler();
            }
        }

        return new InvalidResponseRequestHandler();
    }
}