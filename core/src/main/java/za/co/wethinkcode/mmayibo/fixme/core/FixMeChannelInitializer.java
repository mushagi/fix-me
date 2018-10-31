package za.co.wethinkcode.mmayibo.fixme.core;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

public class FixMeChannelInitializer extends ChannelInitializer<SocketChannel> {

    private final SimpleChannelInboundHandler<String> handler;

    public FixMeChannelInitializer(SimpleChannelInboundHandler<String> handler) {
        this.handler = handler;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) {
        int MAX_THREADS = 10;

        final EventExecutorGroup executorGroup = new DefaultEventExecutorGroup(MAX_THREADS);


        ChannelPipeline pipeline = socketChannel.pipeline();

        pipeline.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));

        pipeline.addLast(new StringDecoder());
        pipeline.addLast(new StringEncoder());

        pipeline.addLast(executorGroup, handler);
    }
}
