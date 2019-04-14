package edu.gyaneshm.ebay_product_search.data;

import org.json.JSONObject;

import edu.gyaneshm.ebay_product_search.models.SearchFormModel;
import edu.gyaneshm.ebay_product_search.shared.Logger;

public class SearchResultData {
    private static SearchResultData searchResultData = null;

    private SearchFormModel searchFormData;

    private SearchResultData() {
    }

    public static SearchResultData getInstance() {
        if (searchResultData == null) {
            searchResultData = new SearchResultData();
        }
        return searchResultData;
    }

    public void setSearchFormData(SearchFormModel searchFormData) {
        this.searchFormData = searchFormData;
    }

    public void fetchData() {

    }
}
