package za.co.wethinkcode.mmayibo.fixme.core.fixprotocol;

import org.junit.jupiter.api.Test;

import java.util.function.BinaryOperator;

import static org.junit.jupiter.api.Assertions.*;

class FixMessageToolsTest {

    @Test
    void getTagValueByRegex() {
        String fixString = "38=fsd|";
        String expected = "daws";
        String actual = FixMessageTools.getTagValueByRegex(fixString, 38);
        assertEquals(expected, actual);
    }

    @Test
    void getTagValueByRegex1() {
        String fixString = "508=fsd|16=555";
        double hash = fixString.hashCode();
        fixString += "10=" + hash + "|";
        boolean s = FixMessageTools.isValidMessage(fixString);

        assertEquals(true,s );

    }
}