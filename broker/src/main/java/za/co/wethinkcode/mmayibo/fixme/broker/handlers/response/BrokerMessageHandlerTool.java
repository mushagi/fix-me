package za.co.wethinkcode.mmayibo.fixme.broker.handlers.response;

import za.co.wethinkcode.mmayibo.fixme.broker.Broker;
import za.co.wethinkcode.mmayibo.fixme.broker.handlers.InvalidResponseRequestHandler;
import za.co.wethinkcode.mmayibo.fixme.core.IMessageHandler;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessage;

public class BrokerMessageHandlerTool {

    public static IMessageHandler getMessageHandler(FixMessage message, Broker broker, String rawFixMessage) {
        if (message.getMessageType() != null){
            switch (message.getMessageType()) {
                case "1":
                    return new IdResponseHandler(broker, message, rawFixMessage);
                case "8":
                    return new ExecutionReportHandler(broker, message, rawFixMessage);
                case "W":
                    return new MarketDataSnapShotResponseHandler(broker, message, rawFixMessage);
            }
        }

        return new InvalidResponseRequestHandler(rawFixMessage);
    }
}