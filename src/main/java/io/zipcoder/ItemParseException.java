package io.zipcoder;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ItemParseException extends Exception {

    private int errorCount;
    private List<String> errors;
    private static final Logger logger = Logger.getGlobal();

    public ItemParseException() {
        this.errorCount = 0;
        errors = new ArrayList<>();
    }

    public ItemParseException(String emptyFieldName) {
        logger.info("This field name is empty: " + "\"" + emptyFieldName + "\"");
    }

    public void logErrorCount(String emptyFieldName, String rawItem) {
        errors.add("Field " + "\"" + emptyFieldName + "\"" + " is empty in " + rawItem);
        errorCount++;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public List<String> getErrors() {
        return errors;
    }

    public String errorCountAsString() {
        String s = String.format("%-13s", "Errors");
        StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append("\t\t" + "seen: " + errorCount + " times");
        return sb.toString();
    }

    public String errorsAsString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Error count: " + errorCount + "\n");
        sb.append("Errors:\n");

        int errorNumber = 1;
        for (String error : errors) {
            sb.append("Error " + errorNumber + ": " + error + "\n");
            errorNumber++;
        }
        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }

}
