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
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessageTools;
import za.co.wethinkcode.mmayibo.fixme.core.model.TradeTransaction;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class Broker extends Client {
    public final ConcurrentHashMap<String, Market> markets = new ConcurrentHashMap<>();
    public final ConcurrentHashMap<UUID, TradeTransaction>  transactions = new ConcurrentHashMap<>();
    private final Logger logger = Logger.getLogger(getClass().getName());
    public BrokerUser user;
    private final ArrayList<BrokerUI> userInterfaces = new ArrayList<>();
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

    public void newOrderSingle(Market market, Instrument instrument, String side, int quantity, int delay){
        FixMessage requestFixMessage = new FixMessageBuilder()
                .newFixMessage()
                .withMessageType("D")
                .withClOrderId(FixMessageTools.generateMessageId())
                .withPrice(instrument.costPrice)
                .withSide(side)
                .withDelay(delay)
                .withOrderQuantity(quantity)
                .withSenderCompId(networkId)
                .withTargetCompId(market.getNetworkId())
                .withMDReqID(market.getMdReqId())
                .withClientIId(user.getUserName())
                .withSymbol(instrument.getId())
                .getFixMessage();

        logger.info("New Order Single. OrderID =  " + requestFixMessage.getClOrderId());
        sendRequest(requestFixMessage, true, true);
    }

    private void requestMarketSnapShotData(String marketId){
        FixMessage requestFixMessage = new FixMessageBuilder()
                .newFixMessage()
                .withMessageType("V")
                .withTargetCompId(marketId)
                .getFixMessage();
        sendRequest(requestFixMessage, true, true);
    }


    public void marketsUpdated(Market market, boolean wasOnlineStatusChanged) {
        for (BrokerUI userInterface: userInterfaces)
            userInterface.updateMarkets(market, wasOnlineStatusChanged);
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

    private void startMarketHeartConnectivityCheck() {
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
            heartBeatRequest.withTestReqId(FixMessageTools.generateMessageId())
                    .withTargetCompId(market.getNetworkId());
            sendRequest(heartBeatRequest.getFixMessage(), false, false);
        }
    }

    private FixMessageBuilder heartBeatRequest = new FixMessageBuilder()
            .newFixMessage()
            .withMessageType("0");

    @Override
    public void stop() {
        super.stop();
        timer.cancel();

    }
}