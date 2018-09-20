package za.co.wethinkcode.mmayibo.fixme.core.fixprotocol;

import lombok.Getter;

public class FixMessageBuilder {
    @Getter
    private final FixMessage fixMessage = new FixMessage();

    private void withReceiverID(String id) {
        fixMessage.receiverChannelID = id;
    }
    private void withMessage(String message) {
        fixMessage.message = message;
    }

    public FixMessageBuilder withSender(String id) {
        fixMessage.senderChannelID = id;
        return this;
    }

    public FixMessageBuilder build(String message) {
        String[] fixStrings = message.split("\\|");
        withReceiverID(fixStrings[0]);
        withMessage(fixStrings[1]);
        return this;
    }
}