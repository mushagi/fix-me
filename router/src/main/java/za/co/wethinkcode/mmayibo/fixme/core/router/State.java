package za.co.wethinkcode.mmayibo.fixme.core.router;

import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

class State {
    static ChannelGroup brokerChannels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    static ChannelGroup marketChannels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
}
