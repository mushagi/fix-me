package za.co.wethinkcode.mmayibo.fixme.broker;

import io.netty.channel.ChannelHandlerContext;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import za.co.wethinkcode.mmayibo.fixme.broker.gui.BrokerUI;
import za.co.wethinkcode.mmayibo.fixme.broker.messagehandlers.BrokerMessageHandlerTool;
import za.co.wethinkcode.mmayibo.fixme.broker.messagehandlers.FixMessageHandlerResponse;
import za.co.wethinkcode.mmayibo.fixme.broker.model.domain.Instrument;
import za.co.wethinkcode.mmayibo.fixme.broker.model.domain.Market;
import za.co.wethinkcode.mmayibo.fixme.data.client.Client;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixEncode;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessageBuilder;
import za.co.wethinkcode.mmayibo.fixme.data.model.*;
import za.co.wethinkcode.mmayibo.fixme.data.persistence.IRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class Broker extends Client {
    private Logger logger = Logger.getLogger(getClass().getName());
    private BrokerUser user;
    private ArrayList<BrokerUI> userInterfaces = new ArrayList<>();

    public ConcurrentHashMap<String, Market> markets = new ConcurrentHashMap<>();

    public Broker(String host, int port) {
        super(host, port);

    }

    @Override
    public void messageRead(ChannelHandlerContext ctx, FixMessage message) {
        FixMessageHandlerResponse messageHandler = BrokerMessageHandlerTool.getMessageHandler(message);
        messageHandler.handleMessage(message, this);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
    }


    public void register(BrokerUI userInterface) {
        if (!userInterfaces.contains(userInterface))
            userInterfaces.add(userInterface);
    }

    public void updateWallet(double availableAmount) {
        user.setAccountBalance(availableAmount);
        for (BrokerUI userInterface: userInterfaces)
            userInterface.updateUser(user);
    }

    public void newOrderSingle(Market market, Instrument instrument){
        FixMessage requestFixMessage = new FixMessageBuilder()
                .newFixMessage()
                .withMessageType("D")
                .withClOrderId(generateUUID())
                .withPrice(instrument.costPrice)
                .withSide("buy")
                .withSenderCompId(networkId)
                .withTargetCompId(market.getNetworkId())
                .withMDReqID(market.getMdReqId())
                .withClientIId(user.getUserName())
                .withSymbol(instrument.getId())
                .getFixMessage();

        logger.fine("New Order Single. OrderID =  " + requestFixMessage.getClOrderId());
        sendRequest(requestFixMessage);
    }

    private String generateUUID() {
        return UUID.randomUUID().toString();
    }



    private void updateUi(){
        for (BrokerUI userInterface: userInterfaces)
            userInterface.update();
    }
    public void marketsUpdated() {
        updateUi();
    }

    public void prepareBroker(String userName) {
        new Thread(() -> {
            user = repository.getByID(userName, BrokerUser.class);
            if (user == null) {
                logger.severe("Could not find data about the broker");
                System.exit(0);
            }
            logger.severe("Successfully retrieved data about the broker");
        }).start();

    }

    public void unregisterUi(BrokerUI brokerUI) {
        userInterfaces.remove(brokerUI);
    }
}