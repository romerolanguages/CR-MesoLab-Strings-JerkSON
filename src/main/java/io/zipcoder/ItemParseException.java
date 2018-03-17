package io.zipcoder;

import java.util.logging.Logger;

public class ItemParseException extends Exception {

    private static final Logger logger = Logger.getGlobal();

    public ItemParseException(String emptyFieldName) {
        logger.info("This field name is empty: " + "\"" + emptyFieldName + "\"");
    }

}
