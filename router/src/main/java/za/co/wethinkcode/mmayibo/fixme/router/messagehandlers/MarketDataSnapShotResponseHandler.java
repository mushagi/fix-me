package za.co.wethinkcode.mmayibo.fixme.router.messagehandlers;

import io.netty.channel.Channel;
import za.co.wethinkcode.mmayibo.fixme.core.ChannelGroupHashed;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixEncode;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessageHandler;

public class MarketDataSnapShotResponseHandler implements FixMessageHandler {

    @Override
    public void next(FixMessageHandler next) {

    }

    @Override
    public void handleMessage(FixMessage fixMessage, Channel channel, ChannelGroupHashed responseChannels) {
        String fixString = FixEncode.encode(fixMessage);

        if (fixMessage.getTargetCompId().equals("all"))
            responseChannels.writeAndFlush(fixString + "\r\n");
        else {
            Channel respondChannel = responseChannels.find(fixMessage.getTargetCompId());
            if (respondChannel != null)
                respondChannel.writeAndFlush(fixString + "\r\n");
        }
    }
}
