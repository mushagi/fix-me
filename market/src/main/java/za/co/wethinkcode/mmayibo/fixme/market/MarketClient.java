package za.co.wethinkcode.mmayibo.fixme.market;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import za.co.wethinkcode.mmayibo.fixme.data.client.Client;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessageBuilder;
import za.co.wethinkcode.mmayibo.fixme.data.model.InitData;
import za.co.wethinkcode.mmayibo.fixme.data.model.InstrumentModel;
import za.co.wethinkcode.mmayibo.fixme.data.model.MarketModel;
import za.co.wethinkcode.mmayibo.fixme.market.handlers.FixMessageHandlerResponse;
import za.co.wethinkcode.mmayibo.fixme.market.handlers.MarketMessageHandlerTool;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.*;
import java.util.logging.Logger;

public class MarketClient extends Client {
    private final Logger logger = Logger.getLogger(getClass().getName());

    public MarketModel marketModel;
    private String marketUserName;

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
        logger.info("Getting an existing account");
        marketModel = repository.getByID(marketUserName, MarketModel.class);
        if (marketModel == null)
        {
            logger.info("Could not find the market on the database");
            logger.info("Creating a new a market");
            marketModel = loadMarketFromXML();
            marketModel = repository.create(marketModel);
        }
        marketModel.updateHashMap();
        logger.info("Market "+ marketModel.getName()+" has been received");
        startTimer();
    }

    private MarketModel loadMarketFromXML() {
        InitData initData ;

        try {
            File file = new File(getClass().getResource("/init.data.xml").getFile());

            JAXBContext jaxbContext = JAXBContext.newInstance(InitData.class);

            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            initData = (InitData) unmarshaller.unmarshal(file);

            for (MarketModel market : initData.markets){
                if (market.getUserName().equals(marketUserName))
                    return market;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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



}
