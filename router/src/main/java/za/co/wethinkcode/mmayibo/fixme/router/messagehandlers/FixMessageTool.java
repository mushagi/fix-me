package za.co.wethinkcode.mmayibo.fixme.router.messagehandlers;

import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessageHandler;

public class FixMessageTool {
    public static FixMessageHandler getMessageHandler(FixMessage fixMessage) {
        if (fixMessage.getMessageType() == 'D')
            return new NewOrderRequestHandler();
        else if (fixMessage.getMessageType() == 'M')
            return new MarketsDataRequestHandler();
        else if (fixMessage.getMessageType() == 'V')
                return new MarketsDataSnapDataRequestHandler();
        else if (fixMessage.getMessageType() == 'W')
            return new MarketDataSnapShotResponseHandler();
        return null;
    }
}