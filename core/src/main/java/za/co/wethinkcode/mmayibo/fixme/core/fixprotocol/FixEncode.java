package za.co.wethinkcode.mmayibo.fixme.core.fixprotocol;

import java.util.HashMap;
import java.util.UUID;

import static za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixTags.CHECK_SUM;
import static za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixTags.MSG_ID;
import static za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixTags.SENDER_COMP_ID;

public class FixEncode {
    public static String encode(FixMessage fixMessage) {
        StringBuilder fixString = new StringBuilder();
        addLine(fixString, fixMessage);
        return fixString.toString();
    }

    private static void addLine(StringBuilder builder, FixMessage message) {
        HashMap<Integer, Object> tagsValuesMap = message.getTagsValuesMap();

        if (message.getMessageId() == null)
            message.tagsValuesMap.put(FixTags.MSG_ID.tag, generateMessageId());

        appendTagToString(SENDER_COMP_ID.tag, message.getSenderCompId(), builder, false);
        message.tagsValuesMap.remove(SENDER_COMP_ID.tag);

        for (Integer tag: tagsValuesMap.keySet())
            if (tagsValuesMap.get(tag) != null)
                appendTagToString(tag, tagsValuesMap.get(tag), builder, false);

        String checksum = createCheckSum(builder);
        appendTagToString(CHECK_SUM.tag, checksum, builder, true);
    }

    private static String createCheckSum(StringBuilder builder) {
        return String.valueOf(builder.toString().hashCode());
    }

    private static String generateMessageId() {
        return UUID.randomUUID().toString();
    }

    private static void appendTagToString(int tag, Object value, StringBuilder builder, boolean withPipeline) {
        builder.append(tag).append("=").append(value).append(withPipeline ? "": "|");
    }
}