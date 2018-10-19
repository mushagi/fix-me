package za.co.wethinkcode.mmayibo.fixme.broker.handlers.response;

import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessageHandler;

public interface FixMessageHandlerResponse {
    void next(FixMessageHandler next);
     void processMessage();
}
