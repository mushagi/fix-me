package za.co.wethinkcode.mmayibo.fixme.broker.handlers.response;

import za.co.wethinkcode.mmayibo.fixme.broker.handlers.InvalidResponseRequestHandler;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessage;

public class BrokerMessageHandlerTool {

    public static FixMessageHandlerResponse getMessageHandler(FixMessage fixMessage) {
        if (fixMessage.getMessageType() != null){
            switch (fixMessage.getMessageType()) {
                case "1":
                    return new IdResponseHandler();
                case "8":
                    return new ExecutionReportHandler();
                case "W":
                    return new MarketDataSnapShotResponseHandler();
                case "2":
                    return new MarketDataSnapShotResponseHandler();
            }
        }

        return new InvalidResponseRequestHandler();
    }
}