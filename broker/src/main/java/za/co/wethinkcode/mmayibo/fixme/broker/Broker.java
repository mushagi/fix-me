package za.co.wethinkcode.mmayibo.fixme.broker;

import io.netty.channel.ChannelHandlerContext;
import lombok.Setter;
import za.co.wethinkcode.mmayibo.fixme.broker.gui.BrokerInterface;
import za.co.wethinkcode.mmayibo.fixme.broker.gui.BrokerMessageHandlerTool;
import za.co.wethinkcode.mmayibo.fixme.broker.gui.FixMessageHandlerResponse;
import za.co.wethinkcode.mmayibo.fixme.broker.gui.MarketData;
import za.co.wethinkcode.mmayibo.fixme.core.client.Client;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixDecoder;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixEncode;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessageBuilder;


public class Broker extends Client {
    public Broker(String host, int port) {
        super(host, port);
    }

    @Setter
    BrokerInterface brokerInterface;

    @Override
    public void messageRead(ChannelHandlerContext ctx, String message) {
        FixMessage fixMessage = FixDecoder.decode(message);
        FixMessageHandlerResponse messageHandler = BrokerMessageHandlerTool.getMessageHandler(fixMessage);
        messageHandler.handleMessage(fixMessage, brokerInterface);

        System.out.println(message);
    }
    
    public void requestMarkets() {
        lastChannel = lastChannel.writeAndFlush("35=V|200=0|\r\n").channel();
    }

    public void requestMarketData(MarketData marketData) {
        FixMessage fixMessage = new FixMessageBuilder()
                .withMessageType("V")
                .withMDReqID(marketData.getId())
                .getFixMessage();

        String fixString = FixEncode.encode(fixMessage);

        lastChannel = lastChannel.writeAndFlush(fixString+"\r\n").channel();
    }

}
