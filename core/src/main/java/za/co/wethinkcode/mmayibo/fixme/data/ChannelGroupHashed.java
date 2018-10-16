package za.co.wethinkcode.mmayibo.fixme.data;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.EventExecutor;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class ChannelGroupHashed extends DefaultChannelGroup {
    private final ConcurrentHashMap<String, ChannelId> channelIdHashMap = new ConcurrentHashMap<>();

    public Channel find(String hashedCode) {
        ChannelId channelId = channelIdHashMap.get(hashedCode);
        return channelId == null ? null :  find(channelId);
    }

    @Override
    public boolean add(Channel channel) {
        channelIdHashMap.put(channel.id().toString(), channel.id());
        return super.add(channel);
    }

    @Override
    public boolean remove(Object o) {
        if (o instanceof ChannelId){
            ChannelId channelId = (ChannelId)o;
            channelIdHashMap.remove(channelId.toString());
        }
        return super.remove(o);
    }

    @Override
    public void clear() {
        channelIdHashMap.clear();
        super.clear();
    }

    public ChannelGroupHashed(EventExecutor executor) {
        super(executor);
    }
}