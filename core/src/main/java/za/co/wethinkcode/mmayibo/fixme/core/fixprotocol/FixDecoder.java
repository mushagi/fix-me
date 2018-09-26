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
        if (tagAndValueArray.length == 2)
        {
            int tag = Integer.parseInt(tagAndValueArray[0]);
            String value = tagAndValueArray[1];
            decodeFixMessage(fixMessage, tag, value);
        }

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
            case 35:
                fixMessage.messageType = value.charAt(0);
                break;
            case 200:
                fixMessage.requestOrResponse = value;
                break;
            case 201:
                fixMessage.message = value;
                break;
        }
    }

    private static void validateTagAndValue(String fixTagAndValue) {

    }
}
