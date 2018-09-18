package za.co.wethinkcode.mmayibo.fixme.core.router;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.group.ChannelGroup;;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Promise;
import za.co.wethinkcode.mmayibo.fixme.core.ResponseFuture;
import za.co.wethinkcode.mmayibo.fixme.core.server.Server;
import za.co.wethinkcode.mmayibo.fixme.core.server.ServerHandler;


import java.util.Iterator;
import java.util.concurrent.ExecutionException;

public class BrokerRouter extends Server {

    BrokerRouter(String host, int port) {
        super(host, port);
        State.brokerChannels = getChannels();
    }

    @Override
    public void messageRead(final ChannelHandlerContext ctx, final String message) {
        final Channel channel =  getChannel(State.marketChannels, message);
        final ResponseFuture responseFuture = new ResponseFuture();

        channel.pipeline().get(ServerHandler.class).setResponseFuture(responseFuture);
        channel.writeAndFlush(message + "\r\n");

        String reply = "not set";
        try {
            reply =  responseFuture.get();
        } catch (InterruptedException e) {

        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        ctx.writeAndFlush("from broker to server" + reply + "\r\n");

    }

    private Channel getChannel(ChannelGroup marketChannels, String message) {

        if (marketChannels.size() != 0)
        {
            Iterator<Channel> iterable = marketChannels.iterator();
            if (iterable.hasNext())
                return iterable.next();
        }

        return null;
    }

}
