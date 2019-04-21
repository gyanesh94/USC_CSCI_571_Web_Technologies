package edu.gyaneshm.ebay_product_search.data;

import android.net.Uri;

import com.android.volley.NetworkError;
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
import edu.gyaneshm.ebay_product_search.models.SimilarProductModel;
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
    private ArrayList<String> mGooglePhotos;
    private ArrayList<SimilarProductModel> mSimilarItems;

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
        String apiEndPoint = AppConfig.getApiEndPoint() + "/mobile/productInfo";
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
                        if (error instanceof NetworkError) {
                            productDetailError = Utils.getString(R.string.not_connected_to_internet);
                        } else if (error != null && error.networkResponse != null && error.networkResponse.data != null) {
                            productDetailError = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                        } else {
                            productDetailError = Utils.getString(R.string.no_response_from_server);
                        }
                        Utils.showToast(Utils.getString(R.string.error_while_fetching_product_details) + productDetailError);
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
        String apiEndPoint = AppConfig.getApiEndPoint() + "/googleImages";
        Uri.Builder builder = Uri.parse(apiEndPoint).buildUpon();
        builder.appendQueryParameter("query", item.getTitle());

        String finalUrl = builder.build().toString();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                finalUrl,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response.length() == 0) {
                            googlePhotosError = Utils.getString(R.string.product_tab_no_images);
                        } else {
                            try {
                                mGooglePhotos = new ArrayList<>();
                                for (int i = 0; i < response.length(); i++) {
                                    mGooglePhotos.add(response.getString(i));
                                }
                            } catch (Exception ex) {
                                googlePhotosError = Utils.getString(R.string.product_tab_no_images);
                                Utils.showToast(Utils.getString(R.string.error_while_fetching_photos) + googlePhotosError);
                            }
                        }
                        googlePhotosFetched = true;
                        sendNotification();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof NetworkError) {
                            googlePhotosError = Utils.getString(R.string.not_connected_to_internet);
                        } else if (error != null && error.networkResponse != null && error.networkResponse.data != null) {
                            googlePhotosError = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                        } else {
                            googlePhotosError = Utils.getString(R.string.no_response_from_server);
                        }
                        Utils.showToast(Utils.getString(R.string.error_while_fetching_photos) + googlePhotosError);
                        googlePhotosFetched = true;
                        sendNotification();
                    }
                }
        );
        jsonArrayRequest.setTag("googleImages");

        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(EbayProductSearchApplication.getInstance().getApplicationContext());
        }
        mRequestQueue.add(jsonArrayRequest);
    }

    public void fetchSimilarItems() {
        String apiEndPoint = AppConfig.getApiEndPoint() + "/similarProduct";
        Uri.Builder builder = Uri.parse(apiEndPoint).buildUpon();
        builder.appendQueryParameter("productId", item.getProductId());

        String finalUrl = builder.build().toString();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                finalUrl,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        if (response.length() == 0) {
                            similarItemsError = Utils.getString(R.string.product_tab_no_similar_items);
                        } else {
                            try {
                                mSimilarItems = new ArrayList<>();
                                for (int i = 0; i < response.length(); i++) {
                                    SimilarProductModel item = new SimilarProductModel(response.getJSONObject(i));
                                    mSimilarItems.add(item);
                                }
                            } catch (Exception ex) {
                                similarItemsError = Utils.getString(R.string.product_tab_no_similar_items);
                                Utils.showToast(Utils.getString(R.string.error_while_fetching_similar_items) + similarItemsError);
                            }
                        }
                        similarItemsFetched = true;
                        sendNotification();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof NetworkError) {
                            similarItemsError = Utils.getString(R.string.not_connected_to_internet);
                        } else if (error != null && error.networkResponse != null && error.networkResponse.data != null) {
                            similarItemsError = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                        } else {
                            similarItemsError = Utils.getString(R.string.no_response_from_server);
                        }
                        Utils.showToast(Utils.getString(R.string.error_while_fetching_similar_items) + similarItemsError);
                        similarItemsFetched = true;
                        sendNotification();
                    }
                }
        );
        jsonArrayRequest.setTag("similarItems");

        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(EbayProductSearchApplication.getInstance().getApplicationContext());
        }
        mRequestQueue.add(jsonArrayRequest);
    }

    public void setItem(SearchResultModel item) {
        this.item = item;
        productDetailFetched = false;
        googlePhotosFetched = false;
        similarItemsFetched = false;
        productDetailError = null;
        googlePhotosError = null;
        similarItemsError = null;
    }

    public SearchResultModel getItem() {
        return item;
    }

    public ProductModel getProductDetail() {
        return mProductDetail;
    }

    public ArrayList<String> getGooglePhotos() {
        return mGooglePhotos;
    }

    public ArrayList<SimilarProductModel> getSimilarItems() {
        return mSimilarItems;
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
            mRequestQueue.cancelAll("googleImages");
            mRequestQueue.cancelAll("similarItems");
        }
    }
}
