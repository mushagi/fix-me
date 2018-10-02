package za.co.wethinkcode.mmayibo.fixme.core.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixEncode;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessageBuilder;

class ClientHandler extends SimpleChannelInboundHandler<String> {
    private final Client client;

    ClientHandler(Client client) {
        this.client = client;
    }


    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) {
        client.messageRead(ctx, msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);

        FixMessage responseIdBackToChannelFixMessage = new FixMessageBuilder()
                .newFixMessage()
                .withMessageType("1")
                .withMDReqID(ctx.channel().id().toString())
                .getFixMessage();

        String fixStringResponseBackToChannel = FixEncode.encode(responseIdBackToChannelFixMessage);
        ctx.channel().writeAndFlush(fixStringResponseBackToChannel + "\r\n");

        client.channelActive(ctx);
    }
}