package za.co.wethinkcode.mmayibo.fixme.core.fixprotocol;

import io.netty.channel.Channel;
import za.co.wethinkcode.mmayibo.fixme.core.ChannelGroupHashed;

public interface FixMessageHandler {
    void next(FixMessageHandler next);
    void routeMessage();
}