package edu.gyaneshm.ebay_product_search;

import android.app.Application;

import edu.gyaneshm.ebay_product_search.data.SearchResultData;

public class EbayProductSearchApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SearchResultData.getInstance(this);
    }
}
