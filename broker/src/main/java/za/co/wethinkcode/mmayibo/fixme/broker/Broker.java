package za.co.wethinkcode.mmayibo.fixme.broker;

import za.co.wethinkcode.mmayibo.fixme.broker.gui.BrokerUI;
import za.co.wethinkcode.mmayibo.fixme.broker.handlers.response.BrokerMessageHandlerTool;
import za.co.wethinkcode.mmayibo.fixme.core.IMessageHandler;
import za.co.wethinkcode.mmayibo.fixme.broker.model.domain.BrokerUser;
import za.co.wethinkcode.mmayibo.fixme.broker.model.domain.Instrument;
import za.co.wethinkcode.mmayibo.fixme.broker.model.domain.Market;
import za.co.wethinkcode.mmayibo.fixme.core.client.Client;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessageBuilder;
import za.co.wethinkcode.mmayibo.fixme.core.model.TradeTransaction;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class Broker extends Client {
    public final ConcurrentHashMap<UUID, TradeTransaction>  transactions = new ConcurrentHashMap<>();
    private final Logger logger = Logger.getLogger(getClass().getName());
    public BrokerUser user;
    private final ArrayList<BrokerUI> userInterfaces = new ArrayList<>();

    public final ConcurrentHashMap<String, Market> markets = new ConcurrentHashMap<>();

    public Broker(String host, int port) {
        super(host, port);
    }

    @Override
    public void messageRead(FixMessage message, String rawFixMessage) {
        IMessageHandler messageHandler = BrokerMessageHandlerTool.getMessageHandler(message, this, rawFixMessage);
        messageHandler.processMessage();
    }

    @Override
    public void channelActive() {
        requestMarketSnapShotData("all");
    }

    public void register(BrokerUI userInterface) {
        if (!userInterfaces.contains(userInterface))
            userInterfaces.add(userInterface);
    }

    public void newOrderSingle(Market market, Instrument instrument, String side, int quantity){
        FixMessage requestFixMessage = new FixMessageBuilder()
                .newFixMessage()
                .withMessageType("D")
                .withClOrderId(generateUUID())
                .withPrice(instrument.costPrice)
                .withSide(side)
                .withOrderQuantity(quantity)
                .withSenderCompId(networkId)
                .withTargetCompId(market.getNetworkId())
                .withMDReqID(market.getMdReqId())
                .withClientIId(user.getUserName())
                .withSymbol(instrument.getId())
                .getFixMessage();

        logger.info("New Order Single. OrderID =  " + requestFixMessage.getClOrderId());
        sendRequest(requestFixMessage);
    }

    private void requestMarketSnapShotData(String marketId){
        FixMessage requestFixMessage = new FixMessageBuilder()
                .newFixMessage()
                .withMessageType("V")
                .withTargetCompId(marketId)
                .getFixMessage();

        logger.info("Requesting market. OrderID =  " + requestFixMessage.getClOrderId());
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

    public void updateTransactions(TradeTransaction tradeTransaction) {
        for (BrokerUI userInterface: userInterfaces)
            userInterface.updateTransactions(tradeTransaction);
    }
}