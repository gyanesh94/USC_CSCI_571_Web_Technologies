package edu.gyaneshm.ebay_product_search.shared;

import java.util.Locale;

public class Utils {
    public static String formatPriceToString(Double number) {
        if (Math.floor(number) == number) {
            return String.format(Locale.getDefault(), "%.1f", number);
        } else {
            return String.format(Locale.getDefault(), "%.2f", number);
        }
    }
}
