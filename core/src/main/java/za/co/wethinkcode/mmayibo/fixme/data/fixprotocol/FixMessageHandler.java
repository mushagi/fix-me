package za.co.wethinkcode.mmayibo.fixme.data.fixprotocol;

import io.netty.channel.Channel;
import za.co.wethinkcode.mmayibo.fixme.data.ChannelGroupHashed;

public interface FixMessageHandler {
    void next(FixMessageHandler next);
    void handleMessage(FixMessage fixMessage, Channel channel, ChannelGroupHashed channels, Channel databaseChannel);
}