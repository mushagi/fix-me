package za.co.wethinkcode.mmayibo.fixme.router;

import io.netty.channel.ChannelHandlerContext;
import za.co.wethinkcode.mmayibo.fixme.data.ChannelGroupHashed;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessageHandler;
import za.co.wethinkcode.mmayibo.fixme.data.server.Server;
import za.co.wethinkcode.mmayibo.fixme.router.handlers.request.FixMessageTool;

import java.util.logging.Logger;

public class DatabaseRouter extends Server {
    private final Logger logger = Logger.getLogger(getClass().getName());

    DatabaseRouter(String host, int port, ChannelGroupHashed channels) {
        super(host, port, channels);

    }

    @Override
    public void messageRead(final ChannelHandlerContext ctx, final FixMessage message) {
        FixMessageHandler fixMessageHandler = FixMessageTool.getMessageHandler(message);
        fixMessageHandler.handleMessage(message, ctx.channel(), State.channels, State.dataChannel);
    }

    @Override
    protected void channelActive(ChannelHandlerContext ctx) {
        logger.info("Data client connected. " + ctx.channel().id().toString());

        State.dataChannel = ctx.channel();
    }
}