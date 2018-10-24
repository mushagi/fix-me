package za.co.wethinkcode.mmayibo.fixme.core;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.EventExecutor;

import java.util.concurrent.ConcurrentHashMap;

public class ChannelGroupHashed extends DefaultChannelGroup {
    private final ConcurrentHashMap<Integer, Channel> channelIdHashMap = new ConcurrentHashMap<>();

    public Channel findById(Integer id) {
        return channelIdHashMap.get(id);
    }

    @Override
    public boolean remove(Object o) {
        if (o instanceof ChannelId){
            ChannelId channelId = (ChannelId)o;
            channelIdHashMap.remove(channelId.toString());

        }
        return super.remove(o);
    }

    public void replaceIds(Integer newId, Integer oldId, Channel channel) {
        channelIdHashMap.remove(newId);
        channelIdHashMap.put(oldId, channel);
        channel.isActive();
    }




    @Override
    public void clear() {
        channelIdHashMap.clear();
        super.clear();
    }

    public ChannelGroupHashed(EventExecutor executor) {
        super(executor);
    }

    public void add(int id, Channel channel) {
        channelIdHashMap.put(id, channel);
        super.add(channel);
    }
}