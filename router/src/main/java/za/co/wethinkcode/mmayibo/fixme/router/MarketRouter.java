package za.co.wethinkcode.mmayibo.fixme.router;

import io.netty.channel.ChannelHandlerContext;
import za.co.wethinkcode.mmayibo.fixme.core.ChannelGroupHashed;
import za.co.wethinkcode.mmayibo.fixme.core.IMessageHandler;
import za.co.wethinkcode.mmayibo.fixme.router.handlers.MessageHandlerTool;
import za.co.wethinkcode.mmayibo.fixme.core.server.Server;

public class MarketRouter extends Server {
    MarketRouter(String host, int port, ChannelGroupHashed channels, ChannelGroupHashed responseChannels) {
        super(host, port, channels, responseChannels);
    }

    @Override
    public void messageRead(final ChannelHandlerContext ctx, final String rawFixMessage) {
        IMessageHandler handler = MessageHandlerTool.getMessageHandler(rawFixMessage, this);
        handler.processMessage();
    }

    @Override
    protected void channelActive(ChannelHandlerContext ctx) {
        logger.info("Market client connected. " + ctx.channel().id().toString());
        State.marketChannels.add(ctx.channel());
    }
}