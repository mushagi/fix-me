package za.co.wethinkcode.mmayibo.fixme.core.fixprotocol;

import java.util.HashMap;

public class FixEncode {
    public static String encode(FixMessage fixMessage)
    {
        StringBuilder fixString = new StringBuilder();
        addLine(fixString, fixMessage);
        return fixString.toString();
    }

    private static void addLine(StringBuilder fixString, FixMessage fixMessage) {
        HashMap<Integer, Object> tagsValuesMap = fixMessage.getTagsValuesMap();
        for (Integer tag: tagsValuesMap.keySet())
            if (tagsValuesMap.get(tag) != null)
                fixString.append(tag).append("=").append(tagsValuesMap.get(tag)).append("|");
    }
}