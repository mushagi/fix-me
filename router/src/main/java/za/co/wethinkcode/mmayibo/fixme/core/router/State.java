package za.co.wethinkcode.mmayibo.fixme.core.router;

import io.netty.util.concurrent.GlobalEventExecutor;
import za.co.wethinkcode.mmayibo.fixme.core.ChannelGroupHashed;

class State {
    static final ChannelGroupHashed brokerChannels = new ChannelGroupHashed(GlobalEventExecutor.INSTANCE);
    static final ChannelGroupHashed marketChannels = new ChannelGroupHashed(GlobalEventExecutor.INSTANCE);
}
