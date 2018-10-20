package za.co.wethinkcode.mmayibo.fixme.core.fixprotocol;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import za.co.wethinkcode.mmayibo.fixme.core.model.InstrumentModel;
import za.co.wethinkcode.mmayibo.fixme.core.model.MarketModel;

import java.io.IOException;
import java.util.Collection;
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
