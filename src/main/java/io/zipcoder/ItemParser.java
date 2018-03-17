package io.zipcoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Map;
import java.util.TreeMap;

public class ItemParser {

    private ItemParseException ipe;
    private List<String> itemStrings;
    private List<Item> items;
    private Map<String, Integer> nameCounter;

    public ItemParser() {
        this.ipe = new ItemParseException();
        this.nameCounter = new TreeMap<String, Integer>();
        this.itemStrings = new ArrayList<>();
        this.items = new ArrayList<>();
    }

    public ItemParseException getIpe() {
        return ipe;
    }

    public ArrayList<String> parseRawDataIntoItemStringArray(String rawData){
        String stringPattern = "##";
        ArrayList<String> response = splitStringWithRegexPattern(rawData, stringPattern);
        itemStrings = response;
        return response;
    }

    public List<String> getItemStrings() {
        return itemStrings;
    }

    public void createItems() throws ItemParseException {
        for (String itemString : itemStrings) {
            Item item = parseStringIntoItem(itemString);
            items.add(item);
        }
    }

    public List<Item> getItems() {
        return items;
    }

    public String itemsAsString() {
        StringBuilder sb = new StringBuilder();
        int count = 1;
        for (Item item : items) {
            sb.append(count + " " + item.toString() + "\n");
            count++;
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
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
            name = "EMPTY";
            emptyFieldName = "name";
            ipe.logErrorCount(emptyFieldName, rawItem);
        }

        Matcher priceMatcher = upToAndIncludingColonPattern.matcher(keyValuePairsArrayList.get(1));
        String priceString = priceMatcher.replaceAll("");
        if (priceString.equals("")) {
            priceString = "0.00";
            emptyFieldName = "price";
            ipe.logErrorCount(emptyFieldName, rawItem);
        }
        double price = Double.parseDouble(priceString);

        Matcher typeMatcher = upToAndIncludingColonPattern.matcher(keyValuePairsArrayList.get(2));
        String type = typeMatcher.replaceAll("").toLowerCase();
        if (type.equals("")) {
            type = "EMPTY";
            emptyFieldName = "type";
            ipe.logErrorCount(emptyFieldName, rawItem);
        }

        Matcher expirationMatcher = upToAndIncludingColonPattern.matcher(keyValuePairsArrayList.get(3));
        String expiration = expirationMatcher.replaceAll("").toLowerCase();
        if (expiration.equals("")) {
            expiration = "EMPTY";
            emptyFieldName = "expiration";
            ipe.logErrorCount(emptyFieldName, rawItem);
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

    public void countNumberOfNameOccurrences(ArrayList<Item> itemArrayList) {

    }

    public Map<String, Integer> getNameCounter() {
        return nameCounter;
    }


}
