package za.co.wethinkcode.mmayibo.fixme.data;

import io.netty.channel.ChannelHandlerContext;
import za.co.wethinkcode.mmayibo.fixme.data.client.Client;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.data.handlers.messages.FixMessageHandlerTool;
import za.co.wethinkcode.mmayibo.fixme.data.handlers.messages.IFixMessageHandler;
import za.co.wethinkcode.mmayibo.fixme.data.persistence.IRepository;

public class DataClient extends Client {

    public final IRepository repository;

    public DataClient(String host, int port, IRepository repository) {
        super(host, port);
        this.repository = repository;
    }

    @Override
    public void messageRead(ChannelHandlerContext ctx, FixMessage message) throws InterruptedException {
        IFixMessageHandler handler = FixMessageHandlerTool.get(message, this);
        handler.process();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {

    }


}
