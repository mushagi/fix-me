package za.co.wethinkcode.mmayibo.fixme.router;

import io.netty.channel.ChannelHandlerContext;
import za.co.wethinkcode.mmayibo.fixme.data.ChannelGroupHashed;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.*;
import za.co.wethinkcode.mmayibo.fixme.router.handlers.request.FixMessageTool;
import za.co.wethinkcode.mmayibo.fixme.data.server.Server;

import java.util.logging.Logger;

public class MarketRouter extends Server {
    private final Logger logger = Logger.getLogger(getClass().getName());

    MarketRouter(String host, int port, ChannelGroupHashed channels) {
        super(host, port, channels);
    }

    @Override
    public void messageRead(final ChannelHandlerContext ctx, final FixMessage message) {
        FixMessageHandler fixMessageHandler = FixMessageTool.getMessageHandler(message);
        fixMessageHandler.handleMessage(message, ctx.channel(), State.brokerChannels, State.dataChannel);
    }

    @Override
    protected void channelActive(ChannelHandlerContext ctx) {
        logger.info("Market client connected. " + ctx.channel().id().toString());
        State.marketChannels.add(ctx.channel());
    }
}
