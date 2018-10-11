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
import za.co.wethinkcode.mmayibo.fixme.market.messagehandlers.FixMessageHandlerResponse;
import za.co.wethinkcode.mmayibo.fixme.market.messagehandlers.MarketMessageHandlerTool;
import za.co.wethinkcode.mmayibo.fixme.market.model.MarketInstrumentModel;

import java.util.Collection;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MarketClient extends Client {
    public MarketModel marketModel = new MarketModel();
    Timer timer = new Timer("Timer");
    Random random = new Random();

    MarketClient(String host, int port) {
        super(host, port);



        marketModel.setName("Name");
        marketModel.getInstruments().put("bdfgd", new MarketInstrumentModel("bdfgd", 1));
        marketModel.getInstruments().put("dsaddass", new MarketInstrumentModel("dsaddass", 2));
        marketModel.getInstruments().put("sdassadsad", new MarketInstrumentModel("sdassadsad", 3));
        marketModel.getInstruments().put("dasdasddsaasd", new MarketInstrumentModel("dasdasddsaasd", 4));

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
        for (InstrumentModel instrumentModel : marketModel.getInstruments().values())
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
        String symbol = encodeInstruments(marketModel.getInstruments().values());

        FixMessage responseMessage = new FixMessageBuilder()
                .newFixMessage()
                .withMessageType("W")
                .withMDName(marketModel.getName())
                .withMDReqID(marketModel.getId())
                .withSenderCompId(lastChannel.id().toString())
                .withTargetCompId(senderCompId)
                .withSymbol(symbol)
                .getFixMessage();

        String fixString = FixEncode.encode(responseMessage);

        channel.writeAndFlush(fixString + "\r\n");
    }

    private String encodeInstruments(Collection<InstrumentModel> instrumentModels) {
        StringBuilder builder = new StringBuilder();

        for (InstrumentModel instrumentModel : instrumentModels) {
            builder.append(instrumentModel.getName() + "#" + instrumentModel.getPrice() + "%");
        }
        return builder.toString();
    }

}
