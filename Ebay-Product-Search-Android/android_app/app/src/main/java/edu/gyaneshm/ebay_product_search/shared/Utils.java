package edu.gyaneshm.ebay_product_search.shared;

import android.widget.Toast;

import java.util.Locale;

import edu.gyaneshm.ebay_product_search.EbayProductSearchApplication;
import edu.gyaneshm.ebay_product_search.R;

public class Utils {
    public static String formatPriceToString(Double number) {
        if (Math.floor(number) == number) {
            return String.format(Locale.getDefault(), "%.1f", number);
        } else {
            return String.format(Locale.getDefault(), "%.2f", number);
        }
    }

    public static String truncateString(String str, int len) {
        if (str.length() > len) {
            str = str.substring(0, len) + getString(R.string.horizontal_ellipsis);
        }
        return str;
    }

    public static String getString(int id) {
        return EbayProductSearchApplication.getInstance().getApplicationContext().getString(id);
    }

    public static void showToast(int id) {
        Toast.makeText(
                EbayProductSearchApplication.getInstance(),
                getString(id),
                Toast.LENGTH_SHORT
        ).show();
    }

    public static void showToast(String message) {
        Toast.makeText(
                EbayProductSearchApplication.getInstance(),
                message,
                Toast.LENGTH_SHORT
        ).show();
    }
}
