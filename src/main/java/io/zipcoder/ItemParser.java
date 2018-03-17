package io.zipcoder;

import java.util.ArrayList;
import java.util.Arrays;

public class ItemParser {


    public ArrayList<String> parseRawDataIntoStringArray(String rawData){
        String stringPattern = "##";
        ArrayList<String> response = splitStringWithRegexPattern(stringPattern , rawData);
        return response;
    }

    private ArrayList<String> splitStringWithRegexPattern(String stringPattern, String inputString){
        return new ArrayList<String>(Arrays.asList(inputString.split(stringPattern)));
    }
    
    public Item parseStringIntoItem(String rawItem) throws ItemParseException{

        // rawItem:
        String rawSingleItem = "naMe:Milk;price:3.23;type:Food;expiration:1/25/2016##";
        // from above String, must extract:
        // naMe:Milk;
        // price:3.23;
        // type:Food;

        String name = null;
        double price = 0;
        String food = null;
        String expiration = null;



        return null;
    }

    public ArrayList<String> findKeyValuePairsInRawItemData(String rawItem){
        String stringPattern = "[;|^]";
        ArrayList<String> response = splitStringWithRegexPattern(stringPattern , rawItem);
        return response;
    }





}
