package za.co.wethinkcode.mmayibo.fixme.market;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import za.co.wethinkcode.mmayibo.fixme.core.client.Client;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixDecoder;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixEncode;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessageBuilder;
import za.co.wethinkcode.mmayibo.fixme.core.model.Instrument;
import za.co.wethinkcode.mmayibo.fixme.core.model.MarketData;
import za.co.wethinkcode.mmayibo.fixme.market.messagehandlers.FixMessageHandlerResponse;
import za.co.wethinkcode.mmayibo.fixme.market.messagehandlers.MarketMessageHandlerTool;
import za.co.wethinkcode.mmayibo.fixme.market.model.MarketInstrument;

import java.util.Collection;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Market extends Client {
    public MarketData marketData = new MarketData();
    Timer timer = new Timer("Timer");
    Random random = new Random();

    Market(String host, int port) {
        super(host, port);



        marketData.setName("Name");
        marketData.getInstruments().put("bdfgd", new MarketInstrument("bdfgd", 1));
        marketData.getInstruments().put("dsaddass", new MarketInstrument("dsaddass", 2));
        marketData.getInstruments().put("sdassadsad", new MarketInstrument("sdassadsad", 3));
        marketData.getInstruments().put("dasdasddsaasd", new MarketInstrument("dasdasddsaasd", 4));

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
        for (Instrument instrument: marketData.getInstruments().values())
            generateRandomPrice((MarketInstrument) instrument);
    }

    private void generateRandomPrice(MarketInstrument instrument) {
        double randomValue = instrument.getMinPrice() + (instrument.getMaxPrice() - instrument.getMinPrice() ) * random.nextDouble();
        instrument.setPrice(randomValue);
    }


    @Override
    public void messageRead(ChannelHandlerContext ctx, String message) {
        System.out.println("Market read fix message " + message);

        FixMessage fixMessage = FixDecoder.decode(message);

        FixMessageHandlerResponse messageHandler = MarketMessageHandlerTool.getMessageHandler(fixMessage);
        assert messageHandler != null;

        messageHandler.handleMessage(ctx, fixMessage, this);

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {

    }

    public void sendMarketDataSnapShot(String senderCompId, Channel channel) {
        String symbol = encodeInstruments(marketData.getInstruments().values());

        FixMessage responseMessage = new FixMessageBuilder()
                .newFixMessage()
                .withMessageType("W")
                .withMDName(marketData.getName())
                .withMDReqID(marketData.getId())
                .withSenderCompId(lastChannel.id().toString())
                .withTargetCompId(senderCompId)
                .withSymbol(symbol)
                .getFixMessage();

        String fixString = FixEncode.encode(responseMessage);

        channel.writeAndFlush(fixString + "\r\n");
    }

    private String encodeInstruments(Collection<Instrument> instruments) {
        StringBuilder builder = new StringBuilder();

        for (Instrument instrument: instruments) {
            builder.append(instrument.getName() + "#" + instrument.getPrice() + "%");
        }
        return builder.toString();
    }

}
