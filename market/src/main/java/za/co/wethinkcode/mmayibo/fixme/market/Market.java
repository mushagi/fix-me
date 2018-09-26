package za.co.wethinkcode.mmayibo.fixme.market;

import io.netty.channel.ChannelHandlerContext;
import za.co.wethinkcode.mmayibo.fixme.core.client.Client;

public class Market extends Client {

    Market(String host, int port        ) {
        super(host, port);
    }

    @Override
    public void messageRead(ChannelHandlerContext ctx, String message) {
//        FixMessage fixMessage = new FixMessageBuilder()
//                .build(message)
//                .withSender(ctx.channel().toString())
//                .getFixMessage();

        System.out.println(message);

//        ctx.writeAndFlush(fixMessage.getReceiverChannelID() + "| from market" + message + "\r\n") ;
    }
}
