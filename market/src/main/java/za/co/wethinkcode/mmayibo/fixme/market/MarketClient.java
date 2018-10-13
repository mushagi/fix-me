package za.co.wethinkcode.mmayibo.fixme.market;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import za.co.wethinkcode.mmayibo.fixme.core.client.Client;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixDecoder;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixEncode;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessageBuilder;
import za.co.wethinkcode.mmayibo.fixme.core.model.InstrumentModel;
import za.co.wethinkcode.mmayibo.fixme.core.model.MarketModel;
import za.co.wethinkcode.mmayibo.fixme.core.persistence.IRepository;
import za.co.wethinkcode.mmayibo.fixme.market.messagehandlers.FixMessageHandlerResponse;
import za.co.wethinkcode.mmayibo.fixme.market.messagehandlers.MarketMessageHandlerTool;
import za.co.wethinkcode.mmayibo.fixme.market.model.MarketInstrumentModel;

import java.util.*;

public class MarketClient extends Client {
    public MarketModel marketModel;
    private IRepository repository;
    private HashMap<String, InstrumentModel> instruments;

    private Timer timer = new Timer("Timer");
    private Random random = new Random();

    MarketClient(String host, int
            port, MarketModel marketModel, IRepository repository) {
        super(host, port);
        this.marketModel = marketModel;
        this.repository = repository;
        setInstruments();
    }

    private void setInstruments() {
        instruments = new HashMap<>();
        Collection<InstrumentModel> instrumentModels = repository.getMultipleByIds(InstrumentModel.class, marketModel.getInstrumentsIds());
        for (InstrumentModel instrument: instrumentModels)
            instruments.put(instrument.getId(), instrument);
    }

    public void startTimer()
    {
        long delay  = 2000L;
        long period = 2000L;
        timer.scheduleAtFixedRate(repeatedTask, delay, period);
    }

    TimerTask repeatedTask = new TimerTask() {
        public void run() {
            generatePriceForInstruments();
            sendMarketDataSnapShot("all", lastChannel);
        }
    };

    private void generatePriceForInstruments() {
        for (InstrumentModel instrumentModel : instruments.values())
            generateRandomPrice((MarketInstrumentModel) instrumentModel);
    }

    private void generateRandomPrice(MarketInstrumentModel instrument) {
        double randomValue = instrument.getMinPrice() + (instrument.getMaxPrice() - instrument.getMinPrice() ) * random.nextDouble();
        instrument.setPrice(randomValue);
    }


    @Override
    public void messageRead(ChannelHandlerContext ctx, String message) {
        System.out.println("MarketClient read fix message " + message);

        FixMessage fixMessage = FixDecoder.decode(message);

        FixMessageHandlerResponse messageHandler = MarketMessageHandlerTool.getMessageHandler(fixMessage);
        assert messageHandler != null;

        messageHandler.handleMessage(ctx, fixMessage, this);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {

    }

    public void sendMarketDataSnapShot(String senderCompId, Channel channel) {
        String symbol = encodeInstruments();

        FixMessage responseMessage = new FixMessageBuilder()
                .newFixMessage()
                .withMessageType("W")
                .withMDName(marketModel.getName())
                .withMDReqID(marketModel.getUserName())
                .withSenderCompId(lastChannel.id().toString())
                .withTargetCompId(senderCompId)
                .withSymbol(symbol)
                .getFixMessage();

        String fixString = FixEncode.encode(responseMessage);

        channel.writeAndFlush(fixString + "\r\n");
    }

    private String encodeInstruments() {
        StringBuilder builder = new StringBuilder();

        for (InstrumentModel instrumentModel : instruments.values())
            builder.append(instrumentModel.getName() + "#" + instrumentModel.getPrice() + "%");

        return builder.toString();
    }

}
