package za.co.wethinkcode.mmayibo.fixme.router;

import io.netty.channel.ChannelHandlerContext;
import za.co.wethinkcode.mmayibo.fixme.core.ChannelGroupHashed;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.*;
import za.co.wethinkcode.mmayibo.fixme.router.messagehandlers.FixMessageTool;
import za.co.wethinkcode.mmayibo.fixme.core.server.Server;

public class MarketRouter extends Server {

    MarketRouter(String host, int port, ChannelGroupHashed channels, ChannelGroupHashed responseChannels) {
        super(host, port, channels, responseChannels);
    }

    @Override
    public void messageRead(final ChannelHandlerContext ctx, final String message) {

        FixMessage fixMessage = FixDecoder.decode(message);
        System.out.println(message);
        FixMessageHandler fixMessageHandler = FixMessageTool.getMessageHandler(fixMessage);
        if (fixMessageHandler != null)
            fixMessageHandler.handleMessage(fixMessage, ctx.channel(), responseChannels);

    }

    @Override
    protected void channelActive(ChannelHandlerContext ctx) {

    }
}
