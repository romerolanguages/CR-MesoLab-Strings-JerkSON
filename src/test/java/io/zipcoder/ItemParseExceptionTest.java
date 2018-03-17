package io.zipcoder;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ItemParseExceptionTest {

    private ItemParser itemParser;
    private String rawBrokenSingleItem = "naMe:;price:3.23;type:Food;expiration:1/25/2016##";

    @Before
    public void setUp() {
        itemParser = new ItemParser();
    }

    @Test
    public void logErrorCountTest() throws ItemParseException {
        // Given
        int expectedErrorCount = 1;
        Item item = itemParser.parseStringIntoItem(rawBrokenSingleItem);
        // When
        int actualErrorCount = itemParser.getIpe().getErrorCount();
        // Then
        Assert.assertEquals(expectedErrorCount, actualErrorCount);
    }
}
