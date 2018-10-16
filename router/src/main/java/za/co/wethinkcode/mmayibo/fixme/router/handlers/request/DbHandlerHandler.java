package za.co.wethinkcode.mmayibo.fixme.router.handlers.request;

import io.netty.channel.Channel;
import za.co.wethinkcode.mmayibo.fixme.data.ChannelGroupHashed;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixEncode;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessageBuilder;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessageHandler;

public class DbHandlerHandler implements FixMessageHandler {
    @Override
    public void next(FixMessageHandler next) {

    }

    @Override
    public void handleMessage(FixMessage fixMessage, Channel channel, ChannelGroupHashed channels, Channel databaseChannel) {
            FixMessage request = new FixMessageBuilder()
                .existingMessage(fixMessage)
                .withSenderCompId(channel.id().toString())
                .withTargetCompId(databaseChannel.id().toString())
                .getFixMessage();

        String requestString = FixEncode.encode(request);
        databaseChannel.writeAndFlush(requestString+"\r\n");

    }
}
