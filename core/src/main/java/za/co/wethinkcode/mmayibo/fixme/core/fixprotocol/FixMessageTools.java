package za.co.wethinkcode.mmayibo.fixme.core.fixprotocol;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FixMessageTools {
    private static final String FIX_PROTOCOL_VALIDATOR_REGEX = "(" + FixTags.MSG_ID.tag+"=(.+)\\|(.+)\\|)"+FixTags.CHECK_SUM.tag+"=(.+)$";
    private static final Pattern fixProtocolPattern = Pattern.compile(FIX_PROTOCOL_VALIDATOR_REGEX);

    public static String getTagValueByRegex(String rawFixmessage, int tag) {
        Pattern tagPattern = Pattern.compile(tag + "=(.*?)(\\||$)");
        Matcher m = tagPattern.matcher(rawFixmessage);
        if (m.find())
            return m.group(1);
        return null;
    }

    public static boolean isValidMessage(String rawFixMessage){

        Matcher m = fixProtocolPattern.matcher(rawFixMessage);
        if (m.find()){
            String messageWithoutChecksum = m.group(1);
            double generateChecksum = messageWithoutChecksum.hashCode();
            double messageChecksum = Double.parseDouble(m.group(4));
            return generateChecksum == messageChecksum;
        }

        return false;
    }

    public static String generateMessageId() {
        return UUID.randomUUID().toString();
    }
}