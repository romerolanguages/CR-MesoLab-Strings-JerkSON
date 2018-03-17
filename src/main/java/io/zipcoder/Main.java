package io.zipcoder;

import org.apache.commons.io.IOUtils;

import java.util.ArrayList;
import java.util.List;


public class Main {

    public String readRawDataToString() throws Exception{
        ClassLoader classLoader = getClass().getClassLoader();
        String result = IOUtils.toString(classLoader.getResourceAsStream("RawData.txt"));
        return result;
    }

    public static void main(String[] args) throws Exception{
        String output = (new Main()).readRawDataToString();

//        System.out.println(output);
        // TODO: parse the data in output into items, and display to console.

        ItemParser itemParser = new ItemParser();
        List<String> itemStrings = itemParser.parseRawDataIntoItemStringArray(output);
        List<Item> items = new ArrayList<>();

        for (String itemString : itemStrings) {
            Item item = itemParser.parseStringIntoItem(itemString);
            items.add(item);
        }

        int count = 1;
        for (Item item : items) {
            System.out.println(count + " " + item.toString());
            count++;
        }

//        List<String> keyValuePairsArrayList = itemParser.findKeyValuePairsInRawItemData(rawSingleItem);
//        System.out.println(keyValuePairsArrayList.toString());
//
//        Item testItem = itemParser.parseStringIntoItem(rawSingleItem);
//        System.out.println(testItem);
    }
}
