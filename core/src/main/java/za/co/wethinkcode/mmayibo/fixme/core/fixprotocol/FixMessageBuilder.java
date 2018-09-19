package za.co.wethinkcode.mmayibo.fixme.core.fixprotocol;

import io.netty.channel.ChannelId;

public class FixMessageBuilder {
    private FixMessage fixMessage = new FixMessage();

    public FixMessage withID(int id) {
   //     fixMessage.receiverChannelID = get;
        return  fixMessage;
    }
    public FixMessage withMessage(String message) {
        fixMessage.message = message;
        return  fixMessage;
    }


    public FixMessage build(String message) {
        String[] fixStrings = message.split("\\|");
        withID(Integer.parseInt(fixStrings[0]));
        withMessage(fixStrings[1]);
        return fixMessage;
    }
}
