package za.co.wethinkcode.mmayibo.fixme.router.messagehandlers;

import io.netty.channel.Channel;
import za.co.wethinkcode.mmayibo.fixme.core.ChannelGroupHashed;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixEncode;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessageHandler;

public class NewOrderRequestHandler implements FixMessageHandler {
    @Override
    public void next(FixMessageHandler next) {

    }

    @Override
    public void handleMessage(FixMessage fixMessage, Channel channel, ChannelGroupHashed responseChannels) {
        Channel respondChannel = responseChannels.find(fixMessage.getTargetCompId());

        String fixMessageString = FixEncode.encode(fixMessage);

        if (respondChannel != null)
            respondChannel.writeAndFlush(  fixMessageString+ "\r\n");
    }
}
