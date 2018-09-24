package za.co.wethinkcode.mmayibo.fixme.core.router;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import za.co.wethinkcode.mmayibo.fixme.core.ChannelGroupHashed;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixDecoder;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.core.server.Server;

public class Router extends Server {

    Router(String host, int port, ChannelGroupHashed channels, ChannelGroupHashed responseChannels) {
        super(host, port, channels, responseChannels);
    }

    @Override
    public void messageRead(final ChannelHandlerContext ctx, final String message) {

        FixMessage fixMessage = FixDecoder.decode(message);
        System.out.println(message);

        final Channel channel =  responseChannels.find(fixMessage.getTargetCompId());

        if (channel != null)
            channel.writeAndFlush(message + "\r\n");
    }
}
