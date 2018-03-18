package io.zipcoder;

import apple.laf.JRSUIUtils;

import java.rmi.activation.ActivationGroup_Stub;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Map.Entry;

public class ItemParser {

    private ItemParseException ipe;
    private List<String> itemStrings;
    private List<Item> items;
    private TreeMap<String, ArrayList<Item>> itemOrganizer;

    public ItemParser() {
        this.ipe = new ItemParseException();
        this.itemStrings = new ArrayList<>();
        this.items = new ArrayList<>();
        this.itemOrganizer = new TreeMap<>();
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

    public ArrayList<Item> getItemsOfSameName(String name) {
        ArrayList<Item> itemsOfSameName = new ArrayList<>();
        for (Item item : items) {
            if (item.getName().equals(name)) {
                itemsOfSameName.add(item);
            }
        }
        return itemsOfSameName;
    }

    public void addNameAndItemsOfSameNameToItemOrganizer() {
        for (Item item : items) {
            String itemName = item.getName();
            if (itemOrganizer.get(itemName) == null) {
                itemOrganizer.put(itemName, getItemsOfSameName(itemName));
            }
        }
    }

    public TreeMap<String, ArrayList<Item>> getItemOrganizer() {
        return itemOrganizer;
    }

    public String itemOrganizerAsString() {
        StringBuilder sb = new StringBuilder();
        for (Entry entry : itemOrganizer.entrySet()) {
            sb.append("name: " + entry.getKey() + " " + "nameCount: " + itemOrganizer.get(entry.getKey()).size() + "\n"
            + entry.getValue() + "\n");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public String itemNameAndCountAsString(String name) {
        String timeOrTimes = "times";
        StringBuilder sb = new StringBuilder();
        for (String keyName : itemOrganizer.keySet()) {
            if (itemOrganizer.get(keyName).size() == 1) {
                timeOrTimes = "time";
            }
            if (keyName.equals(name)) {
                int numberOfEmptyPriceFields = countNumberOfEmptyPriceFields(name);
                int actualCount = itemOrganizer.get(name).size() - numberOfEmptyPriceFields;
                String s = String.format("%-5s%8s", "name:", capitalizeFirstLetterOnly(name));
                sb.append(s);
                sb.append("\t\tseen: " + actualCount + " " + timeOrTimes);
            }
        }
        return sb.toString();
    }

    public int countNumberOfEmptyPriceFields(String name) {
        int count = 0;
        for (String keyName : itemOrganizer.keySet()) {
            if (keyName.equals(name)) {
                for (Item item : itemOrganizer.get(keyName)) {
                    if (item.getPrice() == 0) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    public String capitalizeFirstLetterOnly(String word) {
        StringBuilder sb = new StringBuilder();
        String firstLetterOfWord = word.substring(0,1);
        sb.append(firstLetterOfWord.toUpperCase() + word.substring(1, word.length()));
        return sb.toString();
    }

    public Map<Double, Integer> getPricesAndTheirCount(String name) {
        Map<Double, Integer> pricesAndTheirCount = new TreeMap<>(Collections.reverseOrder());
        for (String nameKey : itemOrganizer.keySet()) {
            if (nameKey.equals(name)) {
                for (int i = 0; i < itemOrganizer.get(nameKey).size(); i++) {
                    double price = itemOrganizer.get(nameKey).get(i).getPrice();
                    if (pricesAndTheirCount.get(price) == null) {
                        pricesAndTheirCount.put(price, 1);
                    } else {
                        pricesAndTheirCount.put(price, pricesAndTheirCount.get(price) + 1);
                    }
                }
            }
        }
        return pricesAndTheirCount;
    }

    public String pricesAndTheirCountAsString(Map<Double, Integer> pricesAndTheirCount) {

        int count = 0;
        StringBuilder sb = new StringBuilder();
        sb.append("=============\t\t=============\n");
        for (Double price : pricesAndTheirCount.keySet()) {
            String timeOrTimes = "times";
            if (pricesAndTheirCount.get(price) == 1) {
                timeOrTimes = "time";
            }
            if (price != 0) {
                sb.append("Price:   " + price + "\t\tseen: " + pricesAndTheirCount.get(price) + " " + timeOrTimes + "\n");
                sb.append("-------------\t\t-------------\n");
            }
            count++;
        }
        if (count > 1) {
            sb.delete(sb.length() - 29, sb.length());
        }
//        sb.deleteCharAt(sb.length() - 1);
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

}
