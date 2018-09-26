package za.co.wethinkcode.mmayibo.fixme.core.fixprotocol;

import lombok.Getter;

import java.util.HashMap;

import static za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixTags.*;

public class FixMessageBuilder {
    @Getter
    private final FixMessage fixMessage = new FixMessage();
    private final HashMap<Integer, String> tagsValuesMap = fixMessage.getTagsValuesMap();

    public FixMessageBuilder withMessageType(String value) {
        fixMessage.messageType = value.charAt(0);
        tagsValuesMap.put(MESSAGE_TYPE.tag, value);
        return  this;
    }
    public FixMessageBuilder withResponseType(String value) {
        fixMessage.requestOrResponse = value;
        tagsValuesMap.put(RESPONSE_TYPE.tag, value);
        return  this;
    }
    public FixMessageBuilder withBeginString(String value) {
        fixMessage.beginString = value;
        tagsValuesMap.put(BEGIN_STRING.tag, value);

        return  this;
    }
    public FixMessageBuilder withBodyLength(String value) {
        fixMessage.bodyLength = value;
        tagsValuesMap.put(BODY_LENGTH.tag, value);
        return  this;
    }
    public FixMessageBuilder withSenderCompId(String value) {
        fixMessage.senderCompId = value;
        tagsValuesMap.put(SENDER_COMP_ID.tag, value);
        return  this;
    }

    public FixMessageBuilder withTargetCompId(String value) {
        fixMessage.targetCompId = value;
        tagsValuesMap.put(TARGET_COMP_ID.tag, value);
        return  this;
    }
    public FixMessageBuilder withSendingTime(String value) {
        fixMessage.sendingTime = value;
        tagsValuesMap.put(SENDING_TIME.tag, value);
        return  this;
    }
    public FixMessageBuilder withOrderQuantity(String value) {
        fixMessage.orderQuantity = value;
        tagsValuesMap.put(ORDER_QUANTITY.tag, value);
        return  this;
    }
    public FixMessageBuilder withOrderType(String value) {
        fixMessage.orderType = value;
        tagsValuesMap.put(ORDER_TYPE.tag, value);
        return  this;
    }
    public FixMessageBuilder withClOrderId(String value) {
        fixMessage.clOrderId = value;
        tagsValuesMap.put(CL_ORDER_ID.tag, value);
        return  this;
    }
    public FixMessageBuilder withSide(String value) {
        fixMessage.side = value;
        tagsValuesMap.put(SIDE.tag, value);
        return  this;
    }
    public FixMessageBuilder withCheckSum(String value) {
        fixMessage.checkSum = value;
        tagsValuesMap.put(CHECK_SUM.tag, value);
        return  this;
    }
    public FixMessageBuilder withPrice(String value) {
        fixMessage.price = value;
        tagsValuesMap.put(PRICE.tag, value);
        return  this;
    }

    public FixMessageBuilder withMessage(String value) {
        fixMessage.message = value;
        tagsValuesMap.put(MESSAGE.tag, value);
        return this;
    }

    public FixMessageBuilder withMDReqID(String value) {
        fixMessage.mDReqID = value;
        tagsValuesMap.put(MESSAGE.tag, value);
        return this;
    }
}
