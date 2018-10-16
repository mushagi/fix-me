package za.co.wethinkcode.mmayibo.fixme.router.handlers.request;

import io.netty.channel.Channel;
import za.co.wethinkcode.mmayibo.fixme.data.ChannelGroupHashed;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixEncode;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessageHandler;

public class NewOrderRequestHandler implements FixMessageHandler {
    @Override
    public void next(FixMessageHandler next) {

    }

    @Override
    public void handleMessage(FixMessage fixMessage, Channel channel, ChannelGroupHashed responseChannels, Channel databaseChannel) {
        Channel respondChannel = responseChannels.find(fixMessage.getTargetCompId());

        String fixMessageString = FixEncode.encode(fixMessage);

        if (respondChannel != null)
            respondChannel.writeAndFlush(  fixMessageString+ "\r\n");
    }
}
