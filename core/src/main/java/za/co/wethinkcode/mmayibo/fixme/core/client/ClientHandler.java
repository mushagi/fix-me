package za.co.wethinkcode.mmayibo.fixme.core.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import za.co.wethinkcode.mmayibo.fixme.core.ResponseFuture;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixDecoder;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessage;

import java.util.logging.Logger;

class ClientHandler extends SimpleChannelInboundHandler<String> {
    private final Logger logger = Logger.getLogger(getClass().getName());
    private final Client client;
    private final ResponseFuture responseFuture;

    ClientHandler(Client client) {
        this.client = client;
        this.responseFuture = client.responseFuture;
    }


    @Override
    public void channelRead0(ChannelHandlerContext ctx, String rawFixMessage) throws Exception {
        FixMessage decodeFixMessage = FixDecoder.decode(rawFixMessage);

        if (decodeFixMessage.getMessageId() != null)
            responseFuture.set(decodeFixMessage.getMessageId(), decodeFixMessage);
        client.messageRead(decodeFixMessage, rawFixMessage);
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
        client.channelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        logger.info("Channel is no longer connected with the server. Trying to connect again...");
        client.doConnect();
    }
}