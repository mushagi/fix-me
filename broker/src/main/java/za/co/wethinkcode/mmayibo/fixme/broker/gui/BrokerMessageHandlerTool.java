package za.co.wethinkcode.mmayibo.fixme.broker.gui;

import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessage;

public class BrokerMessageHandlerTool {

    public static FixMessageHandlerResponse getMessageHandler(FixMessage fixMessage) {
        if (fixMessage.getMessageType() == 'D')
            return new MarketDataResponseHandler();
        else if (fixMessage.getMessageType() == 'V')
            return new MarketDataResponseHandler();
        return null;
    }
}