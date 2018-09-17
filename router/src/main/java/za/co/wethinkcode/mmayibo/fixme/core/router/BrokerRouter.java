package za.co.wethinkcode.mmayibo.fixme.core.router;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.group.ChannelGroup;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import za.co.wethinkcode.mmayibo.fixme.core.server.Server;

import java.nio.BufferOverflowException;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class BrokerRouter extends Server {

    private BlockingQueue<ChannelPromise> messageList = new ArrayBlockingQueue<>(16);

    BrokerRouter(String host, int port) {
        super(host, port);
        State.brokerChannels = getChannels();
    }

    @Override
    public void messageRead(final ChannelHandlerContext ctx, String message) {
        ChannelGroup group = State.marketChannels;

        if (State.marketChannels != null) {
            final Channel channel = getChannel(State.marketChannels, message);
            if (channel != null)
                channel.writeAndFlush(message + "\r\n").addListener(
                        new GenericFutureListener<Future<? super Void>>() {
                        @Override
                        public void operationComplete(Future<? super Void> future) {
                            ctx.writeAndFlush("done bro\r\n");
                          //ctx.writeAndFlush("server says " + message + "\r\n");
                     }
                     });
        }
    }

    public ChannelPromise sendMessage(ChannelHandlerContext ctx, String message) {
        if(ctx == null)
            throw new IllegalStateException();
        return sendMessage(ctx, message, ctx.newPromise());
    }

    public ChannelPromise sendMessage(ChannelHandlerContext ctx, String message, ChannelPromise prom) {
        synchronized(this){
            if(messageList == null) {
                // Connection closed
                prom.setFailure(new IllegalStateException());
            } else if(messageList.offer(prom)) {
                // Connection open and message accepted
                ctx.writeAndFlush(message);
            } else {
                // Connection open and message rejected
                prom.setFailure(new BufferOverflowException());
            }
            return prom;
        }
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
