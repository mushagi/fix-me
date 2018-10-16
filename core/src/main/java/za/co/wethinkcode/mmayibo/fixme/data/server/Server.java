package za.co.wethinkcode.mmayibo.fixme.data.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import za.co.wethinkcode.mmayibo.fixme.data.ChannelGroupHashed;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessageBuilder;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessageHandler;

public abstract class Server implements Runnable{
    private final String HOST;
    private final int PORT;

    public final ChannelGroupHashed channels;

    protected Server(String host, int port, ChannelGroupHashed channels) {
        HOST = System.getProperty("host", host);
        PORT = Integer.parseInt(System.getProperty("port", String.valueOf(port)));
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

   protected void sendInvalidResponse(ChannelHandlerContext ctx, FixMessage message) {
        FixMessage invalidRespondFixMessage = new FixMessageBuilder()
                .newFixMessage()
                .withMessageId(message.getMessageId())
                .withMessageType("invalidrequest")
                .withMessage("Could not find the request of message type = " + message.getMessageType())
                .getFixMessage();
        ctx.writeAndFlush(invalidRespondFixMessage + "\r\n");
    }

   public abstract void messageRead(ChannelHandlerContext ctx, FixMessage message);
    protected abstract void channelActive(ChannelHandlerContext ctx);
}