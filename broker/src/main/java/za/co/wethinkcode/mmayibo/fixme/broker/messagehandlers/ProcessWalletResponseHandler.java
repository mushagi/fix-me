package za.co.wethinkcode.mmayibo.fixme.broker.messagehandlers;

import za.co.wethinkcode.mmayibo.fixme.broker.Broker;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessageHandler;
import za.co.wethinkcode.mmayibo.fixme.core.model.OwnedInstrumentModel;

import java.util.ArrayList;

public class ProcessWalletResponseHandler implements FixMessageHandlerResponse{
    @Override
    public void next(FixMessageHandler next) {

    }

    @Override
    public void handleMessage(FixMessage fixMessage, Broker broker) {
        String[] walletString = fixMessage.getWalletResponse().split("%");

        double availableAmount = Double.parseDouble(walletString[0]);

        ArrayList<OwnedInstrumentModel> ownedInstrumentModels = getAvailableAmount(walletString[1]);

        broker.updateWallet(availableAmount, ownedInstrumentModels);
    }

    private ArrayList<OwnedInstrumentModel> getAvailableAmount(String walletResponse) {
        ArrayList<OwnedInstrumentModel> ownedInstrumentModels = new ArrayList<>();
        String[] instruments = walletResponse.split("#");

        for (String instrument : instruments) {
            String[] instrumentValues = instrument.split("&");

            String name = instrumentValues[0];
            int quantity = Integer.parseInt(instrumentValues[1]);
            ownedInstrumentModels.add(new OwnedInstrumentModel(name, quantity));
        }
        return ownedInstrumentModels;
    }
}
