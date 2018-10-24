package za.co.wethinkcode.mmayibo.fixme.market;

import za.co.wethinkcode.mmayibo.fixme.core.IMessageHandler;
import za.co.wethinkcode.mmayibo.fixme.core.client.Client;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixEncode;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessageBuilder;
import za.co.wethinkcode.mmayibo.fixme.core.model.InitData;
import za.co.wethinkcode.mmayibo.fixme.core.model.InstrumentModel;
import za.co.wethinkcode.mmayibo.fixme.core.model.MarketModel;
import za.co.wethinkcode.mmayibo.fixme.market.handlers.MarketMessageHandlerTool;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.*;
import java.util.logging.Logger;

public class MarketClient extends Client {
    private final Logger logger = Logger.getLogger(getClass().getName());

    public MarketModel marketModel;
    private final String marketUserName;

    private final Timer timer = new Timer("Timer");
    private final Random random = new Random();

    MarketClient(String host, int
            port, String marketUserName) {
        super(host, port);
        this.marketUserName = marketUserName;
        login();
    }

    private void startTimer()
    {
        long delay  = 10000L;
        long period = 10000L;
        timer.scheduleAtFixedRate(broadcastMarket, delay, period);
    }

    private final TimerTask broadcastMarket = new TimerTask() {
        public void run() {
            generatePriceForInstruments();
            sendMarketDataSnapShot("all");
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
    public void messageRead(FixMessage message, String rawFixMessage) {
        IMessageHandler handler = MarketMessageHandlerTool.getMessageHandler(message, this, rawFixMessage);
        handler.processMessage();
    }

    @Override
    public void channelActive()  {

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
        networkId = String.valueOf(marketModel.getNetworkId());
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

    public void sendMarketDataSnapShot(String senderCompId) {
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
        if (channel != null)
            channel.writeAndFlush(FixEncode.encode(responseMessage) + "\r\n");
    }

    private String encodeInstruments() {
        StringBuilder builder = new StringBuilder();

        for (InstrumentModel instrumentModel : marketModel.getInstruments())
            builder.append(instrumentModel.getId()).append("#").append(instrumentModel.getName()).append("#").append(instrumentModel.getPrice()).append("%");

        return builder.toString();
    }



}
