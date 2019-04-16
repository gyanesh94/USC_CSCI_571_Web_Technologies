package edu.gyaneshm.ebay_product_search.data;

import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import edu.gyaneshm.ebay_product_search.EbayProductSearchApplication;
import edu.gyaneshm.ebay_product_search.R;
import edu.gyaneshm.ebay_product_search.listeners.DataFetchingListener;
import edu.gyaneshm.ebay_product_search.models.ProductModel;
import edu.gyaneshm.ebay_product_search.models.SearchResultModel;
import edu.gyaneshm.ebay_product_search.shared.Logger;
import edu.gyaneshm.ebay_product_search.shared.Utils;

public class ProductData {
    private static ProductData instance = null;

    private SearchResultModel item = null;
    private RequestQueue mRequestQueue = null;

    private boolean productDetailFetched;
    private boolean googlePhotosFetched;
    private boolean similarItemsFetched;

    private String productDetailError = null;
    private String googlePhotosError = null;
    private String similarItemsError = null;

    private ProductModel mProductDetail;
    private ArrayList<DataFetchingListener> mCallbacks = new ArrayList<>();

    private ProductData() {
    }

    public static ProductData getInstance() {
        if (instance == null) {
            instance = new ProductData();
        }
        return instance;
    }

    public void fetchProductDetail() {
        String apiEndPoint = AppConfig.getApiEndPoint() + "/productInfo";
        Uri.Builder builder = Uri.parse(apiEndPoint).buildUpon();
        builder.appendQueryParameter("productId", item.getProductId());

        String finalUrl = builder.build().toString();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                finalUrl,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mProductDetail = new ProductModel(response);
                        productDetailFetched = true;
                        sendNotification();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error != null && error.networkResponse != null && error.networkResponse.data != null) {
                            productDetailError = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                        } else {
                            productDetailError = Utils.getString(R.string.no_response_from_server);
                        }
                        productDetailFetched = true;
                        sendNotification();
                    }
                }
        );
        jsonObjectRequest.setTag("productDetail");

        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(EbayProductSearchApplication.getInstance().getApplicationContext());
        }
        mRequestQueue.add(jsonObjectRequest);
    }

    public void fetchGooglePhotos() {

    }

    public void fetchSimilarItems() {

    }

    public void setItem(SearchResultModel item) {
        if (this.item == null || !item.getProductId().equals(this.item.getProductId())) {
            this.item = item;
            productDetailFetched = false;
            googlePhotosFetched = false;
            similarItemsFetched = false;
            productDetailError = null;
            googlePhotosError = null;
            similarItemsError = null;
        }
    }

    public SearchResultModel getItem() {
        return item;
    }

    public boolean isProductDetailFetched() {
        return productDetailFetched;
    }

    public boolean isGooglePhotosFetched() {
        return googlePhotosFetched;
    }

    public boolean isSimilarItemsFetched() {
        return similarItemsFetched;
    }

    public String getProductDetailError() {
        return productDetailError;
    }

    public String getGooglePhotosError() {
        return googlePhotosError;
    }

    public String getSimilarItemsError() {
        return similarItemsError;
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
            mRequestQueue.cancelAll("productDetail");
        }
    }
}
