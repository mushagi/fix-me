package za.co.wethinkcode.mmayibo.fixme.data.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import za.co.wethinkcode.mmayibo.fixme.data.ResponseFuture;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixDecoder;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixEncode;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessageBuilder;

import java.util.logging.Logger;

class ClientHandler extends SimpleChannelInboundHandler<String> {
    private final Client client;
    private final Logger logger = Logger.getLogger(getClass().getName());
    private ResponseFuture responseFuture;

    ClientHandler(Client client) {
        this.client = client;
        this.responseFuture = client.responseFuture;
    }


    @Override
    public void channelRead0(ChannelHandlerContext ctx, String message) throws Exception {
        logger.info("Fix Message read: " + message);
        FixMessage fixMessage = FixDecoder.decode(message);
        client.messageRead(ctx, fixMessage);
        if (fixMessage.getMessageId() != null)
            responseFuture.set(fixMessage.getMessageId(), fixMessage);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        client.isActive = true;
        client.channelActive(ctx);
    }

}