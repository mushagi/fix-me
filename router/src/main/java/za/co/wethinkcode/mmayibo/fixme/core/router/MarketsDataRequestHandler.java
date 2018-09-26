package za.co.wethinkcode.mmayibo.fixme.core.router;

import io.netty.channel.Channel;
import za.co.wethinkcode.mmayibo.fixme.core.ChannelGroupHashed;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixEncode;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessageBuilder;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessageHandler;

public class MarketsDataRequestHandler implements FixMessageHandler {
    FixMessage fixMessage;
    @Override
    public void next(FixMessageHandler next) {
    //    next.handleMessage(fixMessage, ctx.channel(), responseChannels);
    }

    @Override
    public void handleMessage(FixMessage fixMessage, Channel channel, ChannelGroupHashed responseChannels) {
        this.fixMessage = fixMessage;

        if (fixMessage.getRequestOrResponse().equals("0")) {
            String markets = getMarkets();

            FixMessage responseMessage = new FixMessageBuilder()
                    .withBeginString("sdasd")
                    .withBodyLength("dasda")
                    .withMessageType("V")
                    .withResponseType("1")
                    .withMessage(markets)
                    .getFixMessage();

            String fixString = FixEncode.encode(responseMessage);
            channel.writeAndFlush(fixString + "\r\n");
        }

    }

    private String getMarkets() {
        StringBuilder builder = new StringBuilder();

        for (Channel channel: State.marketChannels)
            builder.append(channel.id().toString()).append(",");

        return builder.toString();
    }
}
