package za.co.wethinkcode.mmayibo.fixme.router;

import io.netty.channel.ChannelHandlerContext;
import za.co.wethinkcode.mmayibo.fixme.data.ChannelGroupHashed;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixDecoder;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessageHandler;
import za.co.wethinkcode.mmayibo.fixme.router.handlers.request.FixMessageTool;
import za.co.wethinkcode.mmayibo.fixme.data.server.Server;

import java.util.logging.Logger;

public class BrokerRouter extends Server {
    private final Logger logger = Logger.getLogger(getClass().getName());

    BrokerRouter(String host, int port, ChannelGroupHashed channels) {
        super(host, port, channels);
    }

    @Override
    public void messageRead(final ChannelHandlerContext ctx, final FixMessage message) {
        FixMessageHandler fixMessageHandler = FixMessageTool.getMessageHandler(message);
        fixMessageHandler.handleMessage(message, ctx.channel(), State.marketChannels, State.dataChannel);
    }

    @Override
    protected void channelActive(ChannelHandlerContext ctx) {
        logger.info("Broker client connected. " + ctx.channel().id().toString());
        State.brokerChannels.add(ctx.channel());
    }
}
