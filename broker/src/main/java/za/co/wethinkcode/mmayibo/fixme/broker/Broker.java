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
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class Broker extends Client {
    public final ConcurrentHashMap<UUID, TradeTransaction>  transactions = new ConcurrentHashMap<>();
    private final Logger logger = Logger.getLogger(getClass().getName());
    public BrokerUser user;
    private final ArrayList<BrokerUI> userInterfaces = new ArrayList<>();

    public final ConcurrentHashMap<String, Market> markets = new ConcurrentHashMap<>();
    private Timer timer = new Timer();


    public Broker(String host, int port) {
        super(host, port);
        networkId = "-1";
        startMarketHeartConnectivityCheck();
    }

    @Override
    public void messageRead(FixMessage message, String rawFixMessage) {
        IMessageHandler messageHandler = BrokerMessageHandlerTool.getMessageHandler(message, this, rawFixMessage);
        messageHandler.processMessage();
    }

    @Override
    public void channelActive() {

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
        sendRequest(requestFixMessage, true);
    }

    private void requestMarketSnapShotData(String marketId){
        FixMessage requestFixMessage = new FixMessageBuilder()
                .newFixMessage()
                .withMessageType("V")
                .withTargetCompId(marketId)
                .getFixMessage();
        sendRequest(requestFixMessage, true);
    }

    private String generateUUID() {
        return UUID.randomUUID().toString();
    }

    public void marketsUpdated(Market market) {
        for (BrokerUI userInterface: userInterfaces)
            userInterface.updateMarkets(market);
    }

    public void unregisterUi(BrokerUI brokerUI) {
        userInterfaces.remove(brokerUI);
    }

    public void updateTransactions(TradeTransaction tradeTransaction) {
        for (BrokerUI userInterface: userInterfaces)
            userInterface.updateTransactions(tradeTransaction);
    }

    public void initialize() {
        requestMarketSnapShotData("all");

    }

    private void startMarketHeartConnectivityCheck()
    {
        long delay  = 1000L;
        long period = 1000L;
        timer.scheduleAtFixedRate(checkMarketHeartBeatTask, delay, period);
    }

    private TimerTask checkMarketHeartBeatTask = new TimerTask() {
        @Override
        public void run() {
            try {
                checkMarketHeartBeatRequest();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    private void checkMarketHeartBeatRequest() throws InterruptedException {
        for (Market market: markets.values()) {
            heartBeatFixMessage.withTestReqId(generateUUID())
                    .withTargetCompId(market.getNetworkId());
            FixMessage response = sendMessageWaitForResponse(heartBeatFixMessage.getFixMessage(), false);
            boolean isMarketOnline = response != null && response.getTestReqId().equals("true");
            if (market.isOnline() != isMarketOnline) {
                market.setOnline(isMarketOnline);
                marketsUpdated(market);
            }
        }
    }

    private FixMessageBuilder heartBeatFixMessage = new FixMessageBuilder()
            .newFixMessage()
            .withMessageType("0");
}