package za.co.wethinkcode.mmayibo.fixme.market.messagehandlers;

import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessage;

public class MarketMessageHandlerTool {

    public static FixMessageHandlerResponse getMessageHandler(FixMessage fixMessage) {
        if (fixMessage.getMessageType() == 'D')
            return new NewOrderResponseHandler();
        else if (fixMessage.getMessageType() == 'V')
            return new MarketDataSnapShotResponseHandler();
        return null;
    }
}