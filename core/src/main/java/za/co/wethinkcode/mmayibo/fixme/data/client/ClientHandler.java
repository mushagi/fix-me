package za.co.wethinkcode.mmayibo.fixme.data.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import za.co.wethinkcode.mmayibo.fixme.data.ResponseFuture;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixDecoder;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessage;

class ClientHandler extends SimpleChannelInboundHandler<String> {
    private final Client client;
    private ResponseFuture responseFuture;

    ClientHandler(Client client) {
        this.client = client;
        this.responseFuture = client.responseFuture;
    }


    @Override
    public void channelRead0(ChannelHandlerContext ctx, String rawFixMessage) throws Exception {
        FixMessage decodeFixMessage = FixDecoder.decode(rawFixMessage);

        if (decodeFixMessage.getMessageId() != null)
            responseFuture.set(decodeFixMessage.getMessageId(), decodeFixMessage);

        client.messageRead(ctx, decodeFixMessage, rawFixMessage);
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