package za.co.wethinkcode.mmayibo.fixme.core.fixprotocol;

import java.util.HashMap;

public class FixDecoder {

    public static FixMessage decode(String message)
    {
        FixMessage fixMessage = new FixMessage();
        fixMessage.setTagsValuesMap(new HashMap<>());

        String[] fixStrings = message.split("\\|");

        for (String tagAndValue : fixStrings) {
            validateTagAndValue(tagAndValue);
            addLine(fixMessage, tagAndValue);
        }

        return fixMessage;
    }

    private static void addLine(FixMessage fixMessage, String tagAndValue) {
        String[] tagAndValueArray = tagAndValue.split("=");

        int tag = Integer.parseInt(tagAndValueArray[0]);
        String value = tagAndValueArray[1];
        decodeFixMessage(fixMessage, tag, value);
    }

    private static void decodeFixMessage(FixMessage fixMessage, int tag, String value) {
        fixMessage.getTagsValuesMap().put(tag, value);
        switch (tag)
        {
            case 8 :
                fixMessage.beginString = value;
                break;
            case 9 :
                fixMessage.bodyLength = value;
                break;
            case 10:
                fixMessage.checkSum = value;
                break;
            case 11 :
                fixMessage.clOrderId = value;
                break;
            case 35 :
                fixMessage.messageType = value;
                break;
            case 38 :
                fixMessage.orderQuantity = value;
                break;
            case 40 :
                fixMessage.orderType = value;
                break;
            case 44 :
                fixMessage.price = value;
                break;
            case 49 :
                fixMessage.senderCompId = value;
                break;
            case 52 :
                fixMessage.sendingTime = value;
                break;
            case 54 :
                fixMessage.side = value;
                break;
            case 56 :
                fixMessage.targetCompId = value;
                break;
        }
    }

    private static void validateTagAndValue(String fixTagAndValue) {

    }
}
