package za.co.wethinkcode.mmayibo.fixme.core.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import za.co.wethinkcode.mmayibo.fixme.core.ChannelGroupHashed;

public abstract class Server implements Runnable{
    private final String HOST;
    private final int PORT;

    protected final ChannelGroupHashed responseChannels;
    private final ChannelGroupHashed channels;

    protected Server(String host, int port, ChannelGroupHashed channels, ChannelGroupHashed responseChannels ) {
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
            bootstrap.bind(HOST, PORT).sync().channel();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public abstract void messageRead(ChannelHandlerContext ctx, String message);
}
