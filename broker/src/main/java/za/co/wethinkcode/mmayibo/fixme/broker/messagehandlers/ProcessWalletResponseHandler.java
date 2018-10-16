package za.co.wethinkcode.mmayibo.fixme.broker.messagehandlers;

import za.co.wethinkcode.mmayibo.fixme.broker.Broker;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessageHandler;
import za.co.wethinkcode.mmayibo.fixme.data.model.OwnedInstrumentModel;

public class ProcessWalletResponseHandler implements FixMessageHandlerResponse{
    @Override
    public void next(FixMessageHandler next) {

    }

    @Override
    public void handleMessage(FixMessage fixMessage, Broker broker) {

        double availableAmount = Double.parseDouble(fixMessage.getWalletResponse());

        broker.updateWallet(availableAmount);
    }


}
