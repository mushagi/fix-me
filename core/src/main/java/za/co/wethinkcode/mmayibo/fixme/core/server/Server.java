package za.co.wethinkcode.mmayibo.fixme.core.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.Promise;

import java.util.concurrent.SynchronousQueue;

public abstract class Server implements Runnable{

    protected final SynchronousQueue<Promise<String>> queue = new SynchronousQueue<>();

    private Channel lastChannel;
    private final String HOST;
    private final int PORT;


    protected final ChannelGroup responseChannels;
    protected final ChannelGroup channels;


    protected Server(String host, int port, ChannelGroup channels, ChannelGroup responseChannels ) {
        HOST = System.getProperty("host", host);
        PORT = Integer.parseInt(System.getProperty("port", String.valueOf(port)));
        this.responseChannels = responseChannels;
        this.channels = channels;
    }

    @Override
    public void run() {
        start();
    }

    private void start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup);
            bootstrap.channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ServerInitializer(channels, this))
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            lastChannel = bootstrap.bind(HOST, PORT).sync().channel();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void stop() {
        if (lastChannel != null)
            lastChannel.closeFuture();
    }

    public abstract void messageRead(ChannelHandlerContext ctx, String message);
}
