package io.zipcoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class ItemParser {

    private int errorCount;

    public ItemParser() {
        this.errorCount = 0;
    }

    public ArrayList<String> parseRawDataIntoItemStringArray(String rawData){
        String stringPattern = "##";
        ArrayList<String> response = splitStringWithRegexPattern(rawData, stringPattern);
        return response;
    }

    public Item parseStringIntoItem(String rawItem) throws ItemParseException {

        String emptyFieldName = "";
        List<String> keyValuePairsArrayList = findKeyValuePairsInRawItemData(rawItem);

        String upToAndIncludingColonString = "\\w+:";
        Pattern upToAndIncludingColonPattern = Pattern.compile(upToAndIncludingColonString);

        Matcher nameMatcher = upToAndIncludingColonPattern.matcher(keyValuePairsArrayList.get(0));
        String name = nameMatcher.replaceAll("").toLowerCase();
        String cookiesString = "(c|C)\\w+(s|S)";
        Pattern cookiesPattern = Pattern.compile(cookiesString);
        Matcher cookiesMatcher = cookiesPattern.matcher(name);
        if (cookiesMatcher.find()) {
            name = cookiesMatcher.replaceAll("cookies");
        }
        if (name.equals("")) {
            errorCount++;
            name = "EMPTY";
            emptyFieldName = "name";
//            throw new ItemParseException(emptyFieldName);
        }

        Matcher priceMatcher = upToAndIncludingColonPattern.matcher(keyValuePairsArrayList.get(1));
        String priceString = priceMatcher.replaceAll("");
        if (priceString.equals("")) {
            priceString = "0.00";
            errorCount++;
            emptyFieldName = "price";
//            throw new ItemParseException(emptyFieldName);
        }
        double price = Double.parseDouble(priceString);

        Matcher typeMatcher = upToAndIncludingColonPattern.matcher(keyValuePairsArrayList.get(2));
        String type = typeMatcher.replaceAll("").toLowerCase();
        if (type.equals("")) {
            type = "EMPTY";
            errorCount++;
            emptyFieldName = "type";
//            throw new ItemParseException(emptyFieldName);
        }

        Matcher expirationMatcher = upToAndIncludingColonPattern.matcher(keyValuePairsArrayList.get(3));
        String expiration = expirationMatcher.replaceAll("").toLowerCase();
        if (expiration.equals("")) {
            expiration = "EMPTY";
            errorCount++;
            emptyFieldName = "expiration";
//            throw new ItemParseException(emptyFieldName);
        }

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
}
