package za.co.wethinkcode.mmayibo.fixme.core.fixprotocol;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FixMessageTools {

    public static String getTagValueByRegex(String rawFixmessage, int tag) {
        Pattern tagPattern = Pattern.compile(tag + "=(.*?)(\\||$)");
        Matcher m = tagPattern.matcher(rawFixmessage);
        if (m.find())
            return m.group(1);
        return null;
    }

}