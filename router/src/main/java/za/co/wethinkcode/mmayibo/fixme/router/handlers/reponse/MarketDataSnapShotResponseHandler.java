package za.co.wethinkcode.mmayibo.fixme.router.handlers.reponse;

import io.netty.channel.Channel;
import za.co.wethinkcode.mmayibo.fixme.data.ChannelGroupHashed;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixEncode;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessageHandler;

public class MarketDataSnapShotResponseHandler implements FixMessageHandler {

    @Override
    public void next(FixMessageHandler next) {

    }

    @Override
    public void handleMessage(FixMessage message, Channel channel, ChannelGroupHashed responseChannels, Channel databaseChannel) {
        String response = FixEncode.encode(message);

        if (message.getTargetCompId().equals("all"))
            responseChannels.writeAndFlush(response + "\r\n");
        else {
            Channel respondChannel = responseChannels.find(message.getTargetCompId());
            if (respondChannel != null)
                respondChannel.writeAndFlush(response + "\r\n");
        }
    }
}
