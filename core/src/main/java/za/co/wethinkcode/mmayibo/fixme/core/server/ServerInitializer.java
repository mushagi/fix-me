package za.co.wethinkcode.mmayibo.fixme.core.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

public class ServerInitializer extends ChannelInitializer<SocketChannel> {
    private final Server server;
    int MAX_THREADS = 10;

    public ServerInitializer(ChannelGroup channels, Server server) {
        this.channels = channels;
        this.server = server;
    }

    ChannelGroup channels;

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        final EventExecutorGroup executorGroup = new DefaultEventExecutorGroup(MAX_THREADS);

        ChannelPipeline pipeline = socketChannel.pipeline();

        pipeline.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));

        pipeline.addLast(new StringDecoder());
        pipeline.addLast(new StringEncoder());

        pipeline.addLast(executorGroup, new ServerHandler(channels, server));

    }
}
