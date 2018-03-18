package io.zipcoder;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ItemParserTest {

    private String rawSingleItem =    "naMe:Milk;price:3.23;type:Food;expiration:1/25/2016##";

    private String rawSingleItemIrregularSeperatorSample = "naMe:MiLK;price:3.23;type:Food^expiration:1/11/2016##";

    // I "broke" the original String value (I removed "Milk"), because the original String value was good
    private String rawBrokenSingleItem =    "naMe:;price:3.23;type:Food;expiration:1/25/2016##";

    private String rawMultipleItems = "naMe:Milk;price:3.23;type:Food;expiration:1/25/2016##"
                                      +"naME:BreaD;price:1.23;type:Food;expiration:1/02/2016##"
                                      +"NAMe:BrEAD;price:1.23;type:Food;expiration:2/25/2016##";

    private String rawMultipleItems2 = "naMe:Milk;price:3.23;type:Food;expiration:1/25/2016##"
                                      +"naME:BreaD;price:3.23;type:Food;expiration:1/02/2016##"
                                      +"NAMe:BrEAD;price:1.23;type:Food;expiration:2/25/2016##";

    private ItemParser itemParser;

    @Before
    public void setUp(){
        itemParser = new ItemParser();
    }

    @Test
    public void getItemStringsTest() {
        // Given
        int expectedItemStringsSize = 3;
        itemParser.parseRawDataIntoItemStringArray(rawMultipleItems);
        // When
        int actualItemStringsSize = itemParser.getItemStrings().size();
        // Then
        Assert.assertEquals(expectedItemStringsSize, actualItemStringsSize);
    }

    @Test
    public void createItemsTest() throws ItemParseException {
        // Given
        int expectedItemsSize = 3;
        itemParser.parseRawDataIntoItemStringArray(rawMultipleItems);
        itemParser.createItems();
        // When
        int actualItemsSize = itemParser.getItems().size();
        // Then
        Assert.assertEquals(expectedItemsSize, actualItemsSize);
    }

    @Test
    public void itemsAsStringTest() throws ItemParseException {
        // Given
        String expectedDisplay = "1 name:milk price:3.23 type:food expiration:1/25/2016" + "\n"
                               + "2 name:bread price:1.23 type:food expiration:1/02/2016" + "\n"
                               + "3 name:bread price:1.23 type:food expiration:2/25/2016";
        itemParser.parseRawDataIntoItemStringArray(rawMultipleItems);
        itemParser.createItems();
        // When
        String actualDisplay = itemParser.itemsAsString();
        // Then
        Assert.assertEquals(expectedDisplay, actualDisplay);
    }

    @Test
    public void getItemsOfSameNameTest() throws ItemParseException {
        // Given
        String expectedName = "milk";
        itemParser.parseRawDataIntoItemStringArray(rawMultipleItems);
        itemParser.createItems();
        // When
        ArrayList<Item> itemOfSameName = itemParser.getItemsOfSameName(expectedName);
        String actualName = itemOfSameName.get(0).getName();
        // Then
        Assert.assertEquals(expectedName, actualName);
    }

    @Test
    public void addNameAndItemsOfSameNameToItemOrganizerTest() throws ItemParseException {
        // Given
        String expectedName = "bread";
        int expectedNameCount = 2;
        itemParser.parseRawDataIntoItemStringArray(rawMultipleItems);
        itemParser.createItems();
        // When
        itemParser.addNameAndItemsOfSameNameToItemOrganizer();
        int actualNameCount = itemParser.getItemOrganizer().get(expectedName).size();
        // Then
        Assert.assertEquals(expectedNameCount, actualNameCount);
    }

    @Test
    public void getPricesAndTheirCountTest() throws ItemParseException {
        // Given
        String nameForBread = "bread";
        double expected323Price = 3.23;
        double expected123Price = 1.23;
        int expected323Count = 1;
        int expected123Count = 1;
        itemParser.parseRawDataIntoItemStringArray(rawMultipleItems2);
        itemParser.createItems();
        itemParser.addNameAndItemsOfSameNameToItemOrganizer();
        // When
        int actual323Count = itemParser.getPricesAndTheirCount(nameForBread).get(expected323Price);
        int actual123Count = itemParser.getPricesAndTheirCount(nameForBread).get(expected123Price);
        // Then
        Assert.assertEquals(expected323Count, actual323Count);
        Assert.assertEquals(expected123Count, actual123Count);
    }

    @Test
    public void parseRawDataIntoStringArrayTest(){
        Integer expectedArraySize = 3;
        ArrayList<String> items = itemParser.parseRawDataIntoItemStringArray(rawMultipleItems);
        Integer actualArraySize = items.size();
        assertEquals(expectedArraySize, actualArraySize);
    }

    @Test
    public void parseStringIntoItemTest() throws ItemParseException{
        Item expected = new Item("milk", 3.23, "food","1/25/2016");
        Item actual = itemParser.parseStringIntoItem(rawSingleItem);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test(expected = ItemParseException.class)
    public void parseBrokenStringIntoItemTest() throws ItemParseException{
        itemParser.parseStringIntoItem(rawBrokenSingleItem);
    }

    @Test
    public void findKeyValuePairsInRawItemDataTest(){
        Integer expected = 4;
        Integer actual = itemParser.findKeyValuePairsInRawItemData(rawSingleItem).size();
        assertEquals(expected, actual);
    }

    @Test
    public void findKeyValuePairsInRawItemDataTestIrregular(){
        Integer expected = 4;
        Integer actual = itemParser.findKeyValuePairsInRawItemData(rawSingleItemIrregularSeperatorSample).size();
        assertEquals(expected, actual);
    }
}
