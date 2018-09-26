package za.co.wethinkcode.mmayibo.fixme.core.router;

import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessageHandler;

public class FixMessageTool {
    public static FixMessageHandler getMessageHandler(FixMessage fixMessage) {
        if (fixMessage.getMessageType() == 'D')
            return new NewOrderRequestHandler();
        else
            if (fixMessage.getMessageType() == 'V')
            return new MarketsDataRequestHandler();
        return null;
    }
}