package za.co.wethinkcode.mmayibo.fixme.broker.messagehandlers;

import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessage;

public class BrokerMessageHandlerTool {

    public static FixMessageHandlerResponse getMessageHandler(FixMessage fixMessage) {
        if (fixMessage.getMessageType() == '1')
            return new IdResponseHandler();
        else if (fixMessage.getMessageType() == 'D')
            return new NewOrderResponseHandler();
        else if (fixMessage.getMessageType() == 'W')
            return new MarketDataSnapShotResponseHandler();
        return null;
    }
}