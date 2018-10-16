package za.co.wethinkcode.mmayibo.fixme.router.handlers.request;

import io.netty.channel.Channel;
import za.co.wethinkcode.mmayibo.fixme.data.ChannelGroupHashed;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixEncode;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessageBuilder;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessageHandler;

public class InvalidResponseRequestHandler implements FixMessageHandler {

    @Override
    public void next(FixMessageHandler next) {

    }

    @Override
    public void handleMessage(FixMessage fixMessage, Channel channel, ChannelGroupHashed channels, Channel databaseChannel) {
        FixMessage responseMessage = new FixMessageBuilder()
                .newFixMessage()
                .withMessageType("invalidrequest")
                .withMessage("Error: Message Type is invalid")
                .withMessageId(fixMessage.getMessageId())
                .getFixMessage();
        String responseString = FixEncode.encode(responseMessage);
        channel.writeAndFlush(responseString +"\r\n");

    }
}
