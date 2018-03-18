package io.zipcoder;

import org.apache.commons.io.IOUtils;


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
        itemParser.parseRawDataIntoItemStringArray(output);
        itemParser.createItems();
        itemParser.addNameAndItemsOfSameNameToItemOrganizer();

        // Prints report in alphabetical order by name (the prices/counts match output.txt)
        System.out.println(itemParser.getReportAsString());

        // These commands (commented out) will print the same information in the order of output.txt
//        System.out.println(itemParser.itemNameAndCountAsString("milk"));
//        System.out.println(itemParser.pricesAndTheirCountAsString(itemParser.getPricesAndTheirCount("milk")));
//        System.out.println(itemParser.itemNameAndCountAsString("bread"));
//        System.out.println(itemParser.pricesAndTheirCountAsString(itemParser.getPricesAndTheirCount("bread")));
//        System.out.println(itemParser.itemNameAndCountAsString("cookies"));
//        System.out.println(itemParser.pricesAndTheirCountAsString(itemParser.getPricesAndTheirCount("cookies")));
//        System.out.println(itemParser.itemNameAndCountAsString("apples"));
//        System.out.println(itemParser.pricesAndTheirCountAsString(itemParser.getPricesAndTheirCount("apples")));
//        System.out.println(itemParser.getIpe().errorCountAsString());
    }
}
