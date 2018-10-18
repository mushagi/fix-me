package za.co.wethinkcode.mmayibo.fixme.broker.handlers.response;

import za.co.wethinkcode.mmayibo.fixme.broker.Broker;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessageHandler;

public interface FixMessageHandlerResponse {
    void next(FixMessageHandler next);
     void handleMessage(FixMessage fixMessage , Broker broker);
}
