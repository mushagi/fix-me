package za.co.wethinkcode.mmayibo.fixme.broker;

import io.netty.channel.ChannelHandlerContext;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import za.co.wethinkcode.mmayibo.fixme.broker.gui.BrokerUI;
import za.co.wethinkcode.mmayibo.fixme.broker.messagehandlers.BrokerMessageHandlerTool;
import za.co.wethinkcode.mmayibo.fixme.broker.messagehandlers.FixMessageHandlerResponse;
import za.co.wethinkcode.mmayibo.fixme.broker.model.UserClientData;
import za.co.wethinkcode.mmayibo.fixme.core.client.Client;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixDecoder;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixEncode;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessageBuilder;
import za.co.wethinkcode.mmayibo.fixme.core.model.MarketData;
import za.co.wethinkcode.mmayibo.fixme.core.model.OwnedInstrument;
import za.co.wethinkcode.mmayibo.fixme.core.model.Wallet;

import java.util.ArrayList;

public class Broker extends Client {

    private Wallet wallet = new Wallet();
    private UserClientData userClientData;


    private ArrayList<BrokerUI> userInterfaces = new ArrayList<>();
    public ObservableList<MarketData> markets = FXCollections.observableArrayList();

    public Broker(String host, int port, UserClientData userClientData) {
        super(host, port);
        this.userClientData = userClientData;
    }

    @Override
    public void messageRead(ChannelHandlerContext ctx, String message) {
        FixMessage fixMessage = FixDecoder.decode(message);
        FixMessageHandlerResponse messageHandler = BrokerMessageHandlerTool.getMessageHandler(fixMessage);
        assert messageHandler != null;
        messageHandler.handleMessage(fixMessage, this);

        System.out.println(message);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        requestWalletData();
    }

    private void requestWalletData() {
        FixMessage fixMessage = new FixMessageBuilder()
                .newFixMessage()
                .withMessageType("2")
                .withClientIId(userClientData.getName())
                .getFixMessage();

        String fixMessageString = FixEncode.encode(fixMessage);
        lastChannel.writeAndFlush(fixMessageString +"\r\n");
    }

    public void updateMarkets(MarketData marketData) {
        if (!markets.contains(marketData))
            markets.add(marketData);
        else {
            for (MarketData localMarketData: markets) {
                if (localMarketData.equals(marketData)) {
                    localMarketData.updateInstruments(marketData.getInstruments());
                    break;
                }
            }
        }
        for (BrokerUI userInterface: userInterfaces)
            userInterface.update();
    }

    public void register(BrokerUI userInterface) {
        if (!userInterfaces.contains(userInterface))
            userInterfaces.add(userInterface);
    }

    public void updateWallet(double availableAmount, ArrayList<OwnedInstrument> ownedInstruments) {
        wallet.setAvailableFunds(availableAmount);
        wallet.updateInstruments(ownedInstruments);
        for (BrokerUI userInterface: userInterfaces)
            userInterface.updateWallet(wallet);
    }
}