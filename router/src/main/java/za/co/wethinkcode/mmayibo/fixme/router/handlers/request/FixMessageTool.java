package za.co.wethinkcode.mmayibo.fixme.router.handlers.request;

import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessageHandler;
import za.co.wethinkcode.mmayibo.fixme.router.handlers.reponse.MarketDataSnapShotResponseHandler;

public class FixMessageTool {
    public static FixMessageHandler getMessageHandler(FixMessage fixMessage) {
        if (fixMessage.getMessageType() != null)
        {
            switch (fixMessage.getMessageType()) {
                case "V":
                    return new MarketsDataSnapDataRequestHandler();
                case "W":
                    return new MarketDataSnapShotResponseHandler();

            }
        }
        return new GeneralMessageHandler();
    }
}