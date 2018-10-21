package za.co.wethinkcode.mmayibo.fixme.core.fixprotocol;

import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Logger;

public class FixDecoder {
    private static final Logger logger = Logger.getLogger(FixDecoder.class.getName());

    public static FixMessage decode(String message)
    {
        FixMessage fixMessage = new FixMessage();
        fixMessage.setTagsValuesMap(new HashMap<>());

        String[] fixStrings = message.split("\\|");

        for (String tagAndValue : fixStrings) {
            validateTagAndValue();
            addLine(fixMessage, tagAndValue);
        }

        return fixMessage;
    }

    private static void addLine(FixMessage fixMessage, String tagAndValue) {
        String[] tagAndValueArray = tagAndValue.split("=");
        if (tagAndValueArray.length == 2 )
        {
            try {
                int tag = Integer.parseInt(tagAndValueArray[0]);
                String value = tagAndValueArray[1];
                decodeFixMessage(fixMessage, tag, value);
            }
            catch (NumberFormatException e)
            {
                logger.warning("Fix Decoding Error : Could not decode a number in {"
                        + Arrays.toString(tagAndValueArray) + "}");
            }

        }

    }

    private static void decodeFixMessage(FixMessage fixMessage, int tag, String value) throws NumberFormatException{
        fixMessage.getTagsValuesMap().put(tag, value);
        switch (tag)
        {
            case 8 :
                fixMessage.beginString = value;
                break;
            case 9 :
                fixMessage.bodyLength = value;
                break;
            case 10 :
                fixMessage.checkSum = value;
                break;
            case 35:
                fixMessage.messageType = value;
                break;
            case 37 :
                fixMessage.clOrderId = value;
                break;
            case 38 :
                fixMessage.orderQuantity = Integer.parseInt(value);
                break;
            case 39 :
                fixMessage.ordStatus = value;
                break;
            case 44 :
                fixMessage.price = Double.parseDouble(value);
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
            case 55 :
                fixMessage.symbol = value;
                break;
            case 56 :
                fixMessage.targetCompId = value;
                break;
            case 200:
                fixMessage.requestOrResponse = value;
                break;
            case 58:
                fixMessage.text = value;
                break;
            case 262:
                fixMessage.mDReqID = value;
                break;
            case 500:
                fixMessage.mdName = value;
                break;
            case 501:
                fixMessage.clientId = value;
                break;
            case 502:
                fixMessage.walletResponse = value;
                break;
            case 503:
                fixMessage.dbStatus = value;
                break;
            case 504:
                fixMessage.dbTableName = value;
            case 505:
                fixMessage.dbData = value;
                break;
            case 506:
                fixMessage.dbTransactionType = value;
                break;
            case 507:
                fixMessage.authStatus = value;
                break;
            case 508:
                fixMessage.messageId = value;
            case 509:
                fixMessage.authRequestType = value;
                break;
        }
    }

    private static void validateTagAndValue() {

    }
}
