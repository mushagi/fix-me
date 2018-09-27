package za.co.wethinkcode.mmayibo.fixme.broker;

import io.netty.channel.ChannelHandlerContext;
import lombok.Setter;
import za.co.wethinkcode.mmayibo.fixme.broker.gui.BrokerInterface;
import za.co.wethinkcode.mmayibo.fixme.broker.messagehandlers.BrokerMessageHandlerTool;
import za.co.wethinkcode.mmayibo.fixme.broker.messagehandlers.FixMessageHandlerResponse;
import za.co.wethinkcode.mmayibo.fixme.core.model.MarketData;
import za.co.wethinkcode.mmayibo.fixme.core.client.Client;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixDecoder;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixEncode;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessageBuilder;


public class Broker extends Client {
    protected String channelId;

    public Broker(String host, int port) {
        super(host, port);
    }

    @Setter
    BrokerInterface brokerInterface;

    @Override
    public void messageRead(ChannelHandlerContext ctx, String message) {
        FixMessage fixMessage = FixDecoder.decode(message);
        FixMessageHandlerResponse messageHandler = BrokerMessageHandlerTool.getMessageHandler(fixMessage);
        assert messageHandler != null;
        messageHandler.handleMessage(fixMessage, brokerInterface, this);

        System.out.println(message);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
       // channelId = ctx.channel().id().toString();
    }

    public void requestMarkets() {

        lastChannel = lastChannel.writeAndFlush("35=M|200=0|\r\n").channel();
    }

    public void requestMarketData(MarketData marketData) {
        FixMessage fixMessage = new FixMessageBuilder()
                .newFixMessage()
                .withMessageType("V")
                .withTargetCompId(marketData.getId())
                .withSenderCompId("0")
                .withMDReqID(marketData.getId())
                .getFixMessage();

        String fixString = FixEncode.encode(fixMessage);
        lastChannel = lastChannel.writeAndFlush(fixString+"\r\n").channel();
    }
}
