package edu.gyaneshm.ebay_product_search.data;

import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

public class ZipcodeSuggestionsData {
    private static ZipcodeSuggestionsData mZipcodeSuggestionsData = null;

    private ZipcodeSuggestionsData() {

    }

    public static ZipcodeSuggestionsData getInstance() {
        if (mZipcodeSuggestionsData == null) {
            mZipcodeSuggestionsData = new ZipcodeSuggestionsData();
        }
        return mZipcodeSuggestionsData;
    }

    public JsonObjectRequest fetchZipcodeSuggestions(String zipcode, Response.Listener<JSONObject> successListener, Response.ErrorListener errorListener) {
        String apiEndPoint = AppConfig.getApiEndPoint() + "/zipcode";
        Uri.Builder builder = Uri.parse(apiEndPoint).buildUpon();
        builder.appendQueryParameter("zipcode", zipcode);

        String finalUrl = builder.build().toString();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                finalUrl,
                null,
                successListener,
                errorListener
        );
        jsonObjectRequest.setTag("zipcode");
        return jsonObjectRequest;
    }
}
