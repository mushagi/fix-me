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
import za.co.wethinkcode.mmayibo.fixme.core.model.InstrumentModel;
import za.co.wethinkcode.mmayibo.fixme.core.model.MarketModel;
import za.co.wethinkcode.mmayibo.fixme.core.model.OwnedInstrumentModel;
import za.co.wethinkcode.mmayibo.fixme.core.model.Wallet;

import java.util.ArrayList;
import java.util.logging.Logger;

public class Broker extends Client {

    public String id;
    private Wallet wallet;
    private UserClientData userClientData;
    private Logger logger = Logger.getLogger(getClass().getName());


    private ArrayList<BrokerUI> userInterfaces = new ArrayList<>();
    public ObservableList<MarketModel> markets = FXCollections.observableArrayList();

    public Broker(String host, int port, UserClientData userClientData) {
        super(host, port);
        this.userClientData = userClientData;
        this.wallet = userClientData.getWallet();
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

    public void updateMarkets(MarketModel marketModel) {
        if (!markets.contains(marketModel))
            markets.add(marketModel);
        else {
            for (MarketModel localMarketModel : markets) {
                if (localMarketModel.equals(marketModel)) {
                    localMarketModel.updateInstruments(marketModel.getInstruments());
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

    public void updateWallet(double availableAmount, ArrayList<OwnedInstrumentModel> ownedInstrumentModels) {
        wallet.setAvailableFunds(availableAmount);
        wallet.updateInstruments(ownedInstrumentModels);
        for (BrokerUI userInterface: userInterfaces)
            userInterface.updateWallet(wallet);
    }

    public void newOrderSingle(String marketId, InstrumentModel instrumentModel){
        FixMessage requestFixMessage = new FixMessageBuilder()
                .newFixMessage()
                .withMessageType("D")
                .withClOrderId(generateClientOrder(marketId, instrumentModel.getName()))
                .withPrice(instrumentModel.getPrice())
                .withSide("Buy")
                .withSenderCompId(id)
                .withTargetCompId(marketId)
                .withSymbol(instrumentModel.toSymbol())
                .getFixMessage();
        String fixMessage = FixEncode.encode(requestFixMessage);
        logger.fine("New Order Single " + fixMessage);
        sendMessage(fixMessage);
    }

    private String generateClientOrder(String marketId, String name) {
        StringBuilder builder = new StringBuilder();

        builder.append(System.currentTimeMillis()).append(marketId).append(name);

        return builder.toString();
    }
}