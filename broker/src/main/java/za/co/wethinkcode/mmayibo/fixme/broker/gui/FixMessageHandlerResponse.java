package za.co.wethinkcode.mmayibo.fixme.broker.gui;

import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessageHandler;

public interface FixMessageHandlerResponse {
    void next(FixMessageHandler next);
    void handleMessage(FixMessage fixMessage, BrokerInterface brokerInterface);
}
