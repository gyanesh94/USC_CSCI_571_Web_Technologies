package edu.gyaneshm.ebay_product_search.data;

import android.net.Uri;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import edu.gyaneshm.ebay_product_search.EbayProductSearchApplication;
import edu.gyaneshm.ebay_product_search.R;
import edu.gyaneshm.ebay_product_search.models.SearchFormModel;
import edu.gyaneshm.ebay_product_search.models.SearchResultModel;
import edu.gyaneshm.ebay_product_search.listeners.DataFetchingListener;
import edu.gyaneshm.ebay_product_search.shared.Utils;

public class SearchResultData {
    private static SearchResultData mSearchResultData = null;

    private SearchFormModel searchFormData;

    private boolean dataFetched = false;
    private String errorMessage = null;
    private ArrayList<SearchResultModel> searchResults = new ArrayList<>();

    private ArrayList<DataFetchingListener> mCallbacks = new ArrayList<>();

    private RequestQueue mRequestQueue = null;

    private SearchResultData() {
    }

    public static SearchResultData getInstance() {
        if (mSearchResultData == null) {
            mSearchResultData = new SearchResultData();
        }
        return mSearchResultData;
    }

    public void setSearchFormData(SearchFormModel searchFormData) {
        this.searchFormData = searchFormData;
        errorMessage = null;
        dataFetched = false;
        searchResults.clear();
    }

    public void fetchData() {
        String apiEndPoint = AppConfig.getApiEndPoint() + "/mobile/search";
        Uri.Builder builder = Uri.parse(apiEndPoint).buildUpon();
        builder.appendQueryParameter("data", searchFormData.toJSONObject().toString());

        String finalUrl = builder.build().toString();

        dataFetched = false;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                finalUrl,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        errorMessage = null;
                        if (response.length() == 0) {
                            errorMessage = "No Records.";
                        } else {
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject result = response.getJSONObject(i);
                                    SearchResultModel item = new SearchResultModel(result);
                                    item.setInWishList(WishListData.getInstance().itemInWishList(item));
                                    searchResults.add(item);
                                } catch (Exception e) {
                                    errorMessage = "No Records.";
                                    break;
                                }
                            }
                        }
                        dataFetched = true;
                        sendNotification();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof NetworkError) {
                            errorMessage = Utils.getString(R.string.not_connected_to_internet);
                        } else if (error != null && error.networkResponse != null && error.networkResponse.data != null) {
                            errorMessage = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                        } else {
                            errorMessage = Utils.getString(R.string.search_results_no_records);
                        }

                        dataFetched = true;
                        sendNotification();
                    }
                }
        );
        jsonArrayRequest.setTag("results");

        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(EbayProductSearchApplication.getInstance().getApplicationContext());
        }
        mRequestQueue.add(jsonArrayRequest);
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public ArrayList<SearchResultModel> getSearchResults() {
        return searchResults;
    }

    public SearchFormModel getSearchFormData() {
        return searchFormData;
    }

    public boolean isDataFetched() {
        return dataFetched;
    }

    public void registerCallback(DataFetchingListener callback) {
        mCallbacks.add(callback);
    }

    public void unregisterCallback(DataFetchingListener callback) {
        mCallbacks.remove(callback);
    }

    private void sendNotification() {
        for (int i = 0; i < mCallbacks.size(); i++) {
            mCallbacks.get(i).dataSuccessFullyFetched();
        }
    }

    public void cancelRequest() {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll("results");
        }
    }
}
