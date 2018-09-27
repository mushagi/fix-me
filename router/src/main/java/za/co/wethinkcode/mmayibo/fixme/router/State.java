package za.co.wethinkcode.mmayibo.fixme.router;

import io.netty.util.concurrent.GlobalEventExecutor;
import za.co.wethinkcode.mmayibo.fixme.core.ChannelGroupHashed;

public class State {
    public static final ChannelGroupHashed brokerChannels = new ChannelGroupHashed(GlobalEventExecutor.INSTANCE);
    public static final ChannelGroupHashed marketChannels = new ChannelGroupHashed(GlobalEventExecutor.INSTANCE);
}