package za.co.wethinkcode.mmayibo.fixme.core.router;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessageBuilder;
import za.co.wethinkcode.mmayibo.fixme.core.server.Server;

import java.nio.channels.Channels;

public class Router extends Server {

    Router(String host, int port, ChannelGroup channels, ChannelGroup responseChannels) {
        super(host, port, channels, responseChannels);
    }

    @Override
    public void messageRead(final ChannelHandlerContext ctx, final String message) {
        FixMessage fixMessage = new FixMessageBuilder().build(message);
        final Channel channel =  responseChannels.find(null);
        channel.write(message + "\r\n");
    }
}
