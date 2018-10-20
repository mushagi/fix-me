package za.co.wethinkcode.mmayibo.fixme.core.fixprotocol;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FixMessageToolsTest {

    @Test
    void getTagValueByRegex() {
        String fixString = "38=fsd|";
        String expected = "daws";
        String actual = FixMessageTools.getTagValueByRegex(fixString, 38);
        assertEquals(expected, actual);
    }
}