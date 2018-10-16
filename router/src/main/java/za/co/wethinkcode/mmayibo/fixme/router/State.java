package za.co.wethinkcode.mmayibo.fixme.router;

import io.netty.channel.Channel;
import io.netty.util.concurrent.GlobalEventExecutor;
import za.co.wethinkcode.mmayibo.fixme.data.ChannelGroupHashed;


public class State {
    static final ChannelGroupHashed channels = new ChannelGroupHashed(GlobalEventExecutor.INSTANCE);
    static final ChannelGroupHashed brokerChannels = new ChannelGroupHashed(GlobalEventExecutor.INSTANCE);
    static final ChannelGroupHashed marketChannels = new ChannelGroupHashed(GlobalEventExecutor.INSTANCE);
    static Channel dataChannel;

}