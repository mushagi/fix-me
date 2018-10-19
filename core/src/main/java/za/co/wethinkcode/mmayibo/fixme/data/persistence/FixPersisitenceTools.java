package za.co.wethinkcode.mmayibo.fixme.data.persistence;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import za.co.wethinkcode.mmayibo.fixme.data.model.InstrumentModel;
import za.co.wethinkcode.mmayibo.fixme.data.model.MarketModel;

import java.io.IOException;
import java.util.Collection;

public class FixPersisitenceTools {
    private static ObjectMapper  mapper = new ObjectMapper();

    public static <T> T decodeEntity(String content, Class<T> type) {
        try {
            return mapper.readValue(content, type);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    public static String encodeEntity(Object entity) {
        try {
            return mapper.writeValueAsString(entity);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Class<? extends Object> getClassType(String dbTableName) {
        switch (dbTableName.toLowerCase()){
            case "instrument" :
                return InstrumentModel.class;
            case "market" :
                return MarketModel.class;
        }
        return null;
    }

    public static <T> Collection<T> decodeEntities(String content) {
        try {
            return  mapper.readValue(content, new TypeReference<Collection<T>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
