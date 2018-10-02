package za.co.wethinkcode.mmayibo.fixme.router;

import io.netty.channel.ChannelHandlerContext;
import za.co.wethinkcode.mmayibo.fixme.core.ChannelGroupHashed;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixDecoder;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessageHandler;
import za.co.wethinkcode.mmayibo.fixme.router.messagehandlers.FixMessageTool;
import za.co.wethinkcode.mmayibo.fixme.core.server.Server;

public class BrokerRouter extends Server {

    BrokerRouter(String host, int port, ChannelGroupHashed channels, ChannelGroupHashed responseChannels) {
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
