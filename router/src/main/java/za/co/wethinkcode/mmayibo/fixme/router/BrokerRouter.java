package za.co.wethinkcode.mmayibo.fixme.router;

import io.netty.channel.ChannelHandlerContext;
import za.co.wethinkcode.mmayibo.fixme.core.ChannelGroupHashed;
import za.co.wethinkcode.mmayibo.fixme.core.IMessageHandler;
import za.co.wethinkcode.mmayibo.fixme.core.server.IdCounterFileUtil;
import za.co.wethinkcode.mmayibo.fixme.router.handlers.MessageHandlerTool;
import za.co.wethinkcode.mmayibo.fixme.core.server.Server;

public class BrokerRouter extends Server {
    BrokerRouter(String host, int port, ChannelGroupHashed channels, ChannelGroupHashed responseChannels) {
        super(host, port, channels, responseChannels);
    }

    @Override
    public int generateId() {
        return ++State.idCounter;
    }

    @Override
    public void messageRead(final String rawFixMessage, ChannelHandlerContext ctx) {
        IMessageHandler handler = MessageHandlerTool.getMessageHandler(rawFixMessage, this, ctx.channel());
        handler.processMessage();
    }

    @Override
    protected void channelActive(ChannelHandlerContext ctx, int id) {
        logger.info("Broker client connected. " + ctx.channel().id().toString());
        channels.add(id, ctx.channel());
        IdCounterFileUtil.saveCounter(State.idCounter);
    }
}