package za.co.wethinkcode.mmayibo.fixme.router.handlers.request;

import io.netty.channel.Channel;
import za.co.wethinkcode.mmayibo.fixme.data.ChannelGroupHashed;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixEncode;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessageBuilder;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessageHandler;

public class MarketsDataSnapDataRequestHandler implements FixMessageHandler {
    @Override
    public void next(FixMessageHandler next) {

    }

    @Override
    public void handleMessage(FixMessage fixMessage, Channel channel, ChannelGroupHashed responseChannels, Channel databaseChannel) {
        Channel respondChannel = responseChannels.find(fixMessage.getTargetCompId());

        fixMessage.setSenderCompId(channel.id().toString());

        new FixMessageBuilder().existingMessage(fixMessage).withSenderCompId(channel.id().toString());
        
        String fixString = FixEncode.encode(fixMessage);

        if (respondChannel != null)
            respondChannel.writeAndFlush(  fixString + "\r\n");
    }
}
