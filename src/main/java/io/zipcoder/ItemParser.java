package io.zipcoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class ItemParser {

    public ItemParser() {
    }

    public ArrayList<String> parseRawDataIntoItemStringArray(String rawData){
        String stringPattern = "##";
        ArrayList<String> response = splitStringWithRegexPattern(rawData, stringPattern);
        return response;
    }

    public Item parseStringIntoItem(String rawItem) throws ItemParseException {

        List<String> keyValuePairsArrayList = findKeyValuePairsInRawItemData(rawItem);

        String upToAndIncludingColonString = "\\w+:";
        Pattern upToAndIncludingColonPattern = Pattern.compile(upToAndIncludingColonString);

        Matcher nameMatcher = upToAndIncludingColonPattern.matcher(keyValuePairsArrayList.get(0));
        String name = nameMatcher.replaceAll("");
        String cookiesString = "(c|C)\\w+(s|S)";
        Pattern cookiesPattern = Pattern.compile(cookiesString);
        Matcher cookiesMatcher = cookiesPattern.matcher(name);
        if (cookiesMatcher.find()) {
            name = "Cookies";
        }
        if (name.equals("")) {
            name = "empty string";
        }

        Matcher priceMatcher = upToAndIncludingColonPattern.matcher(keyValuePairsArrayList.get(1));
        String priceString = priceMatcher.replaceAll("");
        double price = Double.parseDouble(priceString);

        Matcher typeMatcher = upToAndIncludingColonPattern.matcher(keyValuePairsArrayList.get(2));
        String type = typeMatcher.replaceAll("");

        Matcher expirationMatcher = upToAndIncludingColonPattern.matcher(keyValuePairsArrayList.get(3));
        String expiration = expirationMatcher.replaceAll("");

        Item item = new Item(name, price, type, expiration);

        return item;
    }

    public ArrayList<String> findKeyValuePairsInRawItemData(String rawItem){
        String stringPattern = "[^\\w:.\\/]";
        ArrayList<String> response = splitStringWithRegexPattern(rawItem, stringPattern);
        return response;
    }

    private ArrayList<String> splitStringWithRegexPattern(String inputString, String stringPattern){
        return new ArrayList<String>(Arrays.asList(inputString.split(stringPattern)));
    }

    public static void main(String[] args) throws ItemParseException {
        ItemParser itemParser = new ItemParser();

        String rawSingleItem =    "naMe:;price:3.23;type:Food;expiration:1/25/2016##";
        String rawSingleItemIrregularSeperatorSample = "naMe:MiLK;price:3.23;type:Food^expiration:1/11/2016##";
        String rawBrokenSingleItem =    "naMe:Milk;price:3.23;type:Food;expiration:1/25/2016##";
        String rawMultipleItems = "naMe:Milk;price:3.23;type:Food;expiration:1/25/2016##"
                                    +"naME:BreaD;price:1.23;type:Food;expiration:1/02/2016##"
                                    +"NAMe:BrEAD;price:1.23;type:Food;expiration:2/25/2016##";

        List<String> itemArrayList = itemParser.parseRawDataIntoItemStringArray(rawMultipleItems);
        System.out.println(itemArrayList.toString());

        List<String> keyValuePairsArrayList = itemParser.findKeyValuePairsInRawItemData(rawSingleItem);
        System.out.println(keyValuePairsArrayList.toString());

        Item testItem = itemParser.parseStringIntoItem(rawSingleItem);
        System.out.println(testItem);
    }

}
