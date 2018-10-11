package za.co.wethinkcode.mmayibo.fixme.market.messagehandlers;

import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessage;

public class MarketMessageHandlerTool {

    public static FixMessageHandlerResponse getMessageHandler(FixMessage fixMessage) {
        if (fixMessage.getMessageType() == '1')
            return new IdResponseHandler();
        if (fixMessage.getMessageType() == 'D')
            return new NewOrderRequestHandler();
        else if (fixMessage.getMessageType() == 'V')
            return new MarketDataSnapShotRequestHandler();
        return null;
    }
}