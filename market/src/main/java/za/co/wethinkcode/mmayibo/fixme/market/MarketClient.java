package za.co.wethinkcode.mmayibo.fixme.market;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import za.co.wethinkcode.mmayibo.fixme.data.client.Client;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessageBuilder;
import za.co.wethinkcode.mmayibo.fixme.data.model.InstrumentModel;
import za.co.wethinkcode.mmayibo.fixme.data.model.MarketModel;
import za.co.wethinkcode.mmayibo.fixme.market.handlers.FixMessageHandlerResponse;
import za.co.wethinkcode.mmayibo.fixme.market.handlers.MarketMessageHandlerTool;
import za.co.wethinkcode.mmayibo.fixme.market.model.MarketInstrumentModel;

import java.util.*;
import java.util.logging.Logger;

public class MarketClient extends Client {
    private final Logger logger = Logger.getLogger(getClass().getName());

    public MarketModel marketModel;
    private String marketUserName;
    private boolean isLoggedIn = false;

    private Timer timer = new Timer("Timer");
    private Random random = new Random();

    MarketClient(String host, int
            port, String marketUserName) {
        super(host, port);
        this.marketUserName = marketUserName;
    }

    public void startTimer()
    {
        long delay  = 10000L;
        long period = 10000L;
        timer.scheduleAtFixedRate(broadcastMarket, delay, period);
    }

    private TimerTask broadcastMarket = new TimerTask() {
        public void run() {
            generatePriceForInstruments();
            sendMarketDataSnapShot("all", lastChannel);
        }
    };

    private void generatePriceForInstruments() {
        for (InstrumentModel instrument : marketModel.getInstruments())
            generateRandomPrice(instrument);
    }

    private void generateRandomPrice(InstrumentModel instrument) {
        double randomValue = 0 + (100- 1) * random.nextDouble();
        instrument.setPrice(randomValue);
    }


    @Override
    public void messageRead(ChannelHandlerContext ctx, FixMessage message) throws InterruptedException {
        FixMessageHandlerResponse messageHandler = MarketMessageHandlerTool.getMessageHandler(message, this);
        messageHandler.handleMessage(message);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws InterruptedException {
        login();
    }

    private void login() {
        String dbData = "username:\"" + marketUserName + "\"";

        sendRequest(authRequestMessage(dbData, "market", "signin"));
    }

    public void sendMarketDataSnapShot(String senderCompId, Channel channel) {
        String symbol = encodeInstruments();

        FixMessage responseMessage = new FixMessageBuilder()
                .newFixMessage()
                .withMessageType("W")
                .withMDName(marketModel.getName())
                .withMDReqID(marketModel.getUserName())
                .withSenderCompId(networkId)
                .withTargetCompId(senderCompId)
                .withSymbol(symbol)
                .getFixMessage();

        sendResponse(responseMessage);
    }

    private String encodeInstruments() {
        StringBuilder builder = new StringBuilder();

        for (InstrumentModel instrumentModel : marketModel.getInstruments())
            builder.append(instrumentModel.getId()).append("#").append(instrumentModel.getName()).append("#").append(instrumentModel.getPrice()).append("%");

        return builder.toString();
    }

    public void loggedInSuccessfully() throws InterruptedException {
        isLoggedIn = true;
        requestMarketInformation();
        startTimer();
    }

    private void requestMarketInformation() throws InterruptedException {
        new Thread(() -> {
            marketModel = repository.getByID(marketUserName, MarketModel.class);
            if (marketModel == null) {
                System.out.println("Could not find the market on the database or database is down");
                System.exit(0);
            }
            logger.info("Market "+ marketModel.getName()+" has been received");


        }).start();
    }

    public void failedToLogin(String err) {
        System.out.println(err);
        System.exit(0);
    }
}
