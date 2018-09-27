package za.co.wethinkcode.mmayibo.fixme.market.messagehandlers;

import io.netty.channel.ChannelHandlerContext;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixEncode;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessageBuilder;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessageHandler;
import za.co.wethinkcode.mmayibo.fixme.core.model.Instrument;
import za.co.wethinkcode.mmayibo.fixme.core.model.MarketData;

import java.util.Collection;

public class MarketDataSnapShotResponseHandler implements FixMessageHandlerResponse{

    @Override
    public void next(FixMessageHandler next) {

    }

    @Override
    public void handleMessage(ChannelHandlerContext ctx, FixMessage fixMessage, MarketData marketData) {
        String symbol = encodeInstruments(marketData.getInstruments());
        FixMessage responseMessage = new FixMessageBuilder()
                .newFixMessage()
                .withMessageType("W")
                .withMDName(marketData.getName())
                .withMDReqID(marketData.getId())
                .withSenderCompId(ctx.channel().id().toString())
                .withTargetCompId(fixMessage.getSenderCompId())
                .withSymbol(symbol)
                .getFixMessage();

        String fixString = FixEncode.encode(responseMessage);

        ctx.writeAndFlush(fixString + "\r\n");

    }

    private String encodeInstruments(Collection<Instrument> instruments) {
        StringBuilder builder = new StringBuilder();

        for (Instrument instrument: instruments) {
            builder.append(instrument.getName() + "#" + instrument.getPrice() + "%");
        }
        return builder.toString();
    }

}
