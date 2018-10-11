package za.co.wethinkcode.mmayibo.fixme.router.messagehandlers;

import io.netty.channel.Channel;
import za.co.wethinkcode.mmayibo.fixme.core.ChannelGroupHashed;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixEncode;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessageBuilder;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessageHandler;
import za.co.wethinkcode.mmayibo.fixme.core.model.OwnedInstrumentModel;
import za.co.wethinkcode.mmayibo.fixme.core.model.Wallet;

import java.util.ArrayList;

public class WalletRequestHandler implements FixMessageHandler {
    @Override
    public void next(FixMessageHandler next) {

    }

    @Override
    public void handleMessage(FixMessage fixMessage, Channel channel, ChannelGroupHashed responseChannels) {
        Wallet wallet = new Wallet();
        wallet.setAvailableFunds(1000);
        ArrayList<OwnedInstrumentModel> ownedInstrumentModels = new ArrayList<>();
        ownedInstrumentModels.add(new OwnedInstrumentModel("nane", 80));
        wallet.setOwnedInstrumentModels(ownedInstrumentModels);
        String walletString = getWalletString(wallet);

        FixMessage responseFixMessage = new FixMessageBuilder()
                .newFixMessage()
                .withMessageType("3")
                .withWallet(walletString)
                .getFixMessage();
        String fixMessageString = FixEncode.encode(responseFixMessage);
        channel.writeAndFlush(fixMessageString+"\r\n");
    }

    private String getWalletString(Wallet wallet) {
        StringBuilder walletString = new StringBuilder();
        walletString.append(wallet.getAvailableFunds()).append("%");
        for (OwnedInstrumentModel instrument: wallet.getOwnedInstrumentModels())
            walletString.append(instrument.getName()).append("&").append(instrument.getQuantity()).append("#");
        return walletString.toString();
    }
}
