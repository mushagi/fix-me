package za.co.wethinkcode.mmayibo.fixme.broker.handlers.response;


import za.co.wethinkcode.mmayibo.fixme.broker.Broker;
import za.co.wethinkcode.mmayibo.fixme.broker.handlers.response.FixMessageHandlerResponse;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessageHandler;

public class IdResponseHandler implements FixMessageHandlerResponse {

    @Override
    public void next(FixMessageHandler next) {

    }

    @Override
    public void handleMessage(FixMessage fixMessage, Broker broker) {
        broker.networkId = fixMessage.getText();
        System.out.println("MarketClient Id : " + broker.networkId);
    }
}