package za.co.wethinkcode.mmayibo.fixme.broker;

import io.netty.channel.ChannelHandlerContext;
import za.co.wethinkcode.mmayibo.fixme.broker.gui.BrokerUI;
import za.co.wethinkcode.mmayibo.fixme.broker.handlers.response.BrokerMessageHandlerTool;
import za.co.wethinkcode.mmayibo.fixme.broker.handlers.response.FixMessageHandlerResponse;
import za.co.wethinkcode.mmayibo.fixme.broker.model.domain.BrokerUser;
import za.co.wethinkcode.mmayibo.fixme.broker.model.domain.Instrument;
import za.co.wethinkcode.mmayibo.fixme.broker.model.domain.Market;
import za.co.wethinkcode.mmayibo.fixme.data.client.Client;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessageBuilder;
import za.co.wethinkcode.mmayibo.fixme.data.model.TradeTransaction;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class Broker extends Client {
    public ArrayList<TradeTransaction> transactions = new ArrayList();
    private Logger logger = Logger.getLogger(getClass().getName());
    public BrokerUser user;
    private ArrayList<BrokerUI> userInterfaces = new ArrayList<>();

    public ConcurrentHashMap<String, Market> markets = new ConcurrentHashMap<>();

    public Broker(String host, int port) {
        super(host, port);
    }

    @Override
    public void messageRead(ChannelHandlerContext ctx, FixMessage message, String rawFixMessage) {
        FixMessageHandlerResponse messageHandler = BrokerMessageHandlerTool.getMessageHandler(message, this, rawFixMessage);
        messageHandler.processMessage();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
    }

    public void register(BrokerUI userInterface) {
        if (!userInterfaces.contains(userInterface))
            userInterfaces.add(userInterface);
    }

    public void newOrderSingle(Market market, Instrument instrument, String side){
        FixMessage requestFixMessage = new FixMessageBuilder()
                .newFixMessage()
                .withMessageType("D")
                .withClOrderId(generateUUID())
                .withPrice(instrument.costPrice)
                .withSide(side)
                .withSenderCompId(networkId)
                .withTargetCompId(market.getNetworkId())
                .withMDReqID(market.getMdReqId())
                .withClientIId(user.getUserName())
                .withSymbol(instrument.getId())
                .getFixMessage();

        logger.info("New Order Single. OrderID =  " + requestFixMessage.getClOrderId());
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

    public void unregisterUi(BrokerUI brokerUI) {
        userInterfaces.remove(brokerUI);
    }

    public void updateTransactions() {
        logger.info("transaction available");
    }
}