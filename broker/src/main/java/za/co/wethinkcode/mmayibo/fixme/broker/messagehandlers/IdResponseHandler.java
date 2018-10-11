package za.co.wethinkcode.mmayibo.fixme.broker.messagehandlers;


import za.co.wethinkcode.mmayibo.fixme.broker.Broker;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessageHandler;

public class IdResponseHandler implements FixMessageHandlerResponse {

    @Override
    public void next(FixMessageHandler next) {

    }

    @Override
    public void handleMessage(FixMessage fixMessage, Broker broker) {
        broker.id = fixMessage.getMDReqID();
        System.out.println("Broker Id " + fixMessage.getMDReqID());
    }
}