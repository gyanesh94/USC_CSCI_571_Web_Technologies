package edu.gyaneshm.ebay_product_search.models;

import android.text.Html;

import org.json.JSONObject;

import java.net.URLDecoder;

import edu.gyaneshm.ebay_product_search.shared.Utils;

public class SellerModel {
    private Double feedbackScore;
    private Double popularity;
    private String feedbackStarColor;
    private boolean topRated;
    private String storeName;
    private String storeUrl;
    private String sellerName;

    SellerModel(JSONObject data) {
        try {
            feedbackScore = Utils.optDouble(data, "feedbackScore");
            popularity = Utils.optDouble(data, "popularity");
            feedbackStarColor = Utils.optString(data, "feedbackStarColor");
            topRated = Utils.optBoolean(data, "topRated");
            storeName = URLDecoder.decode(Utils.optString(data, "storeName"), "UTF-8");
            storeName = Html.fromHtml(storeName, Html.FROM_HTML_MODE_LEGACY).toString();
            storeUrl = Utils.optString(data, "storeUrl");
            sellerName = Utils.optString(data, "sellerName");
        } catch (Exception ex) {
        }
    }

    public Double getFeedbackScore() {
        return feedbackScore;
    }

    public Double getPopularity() {
        return popularity;
    }

    public String getFeedbackStarColor() {
        return feedbackStarColor;
    }

    public boolean isTopRated() {
        return topRated;
    }

    public String getStoreName() {
        return storeName;
    }

    public String getStoreUrl() {
        return storeUrl;
    }

    public String getSellerName() {
        return sellerName;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("feedbackScore: ");
        str.append(feedbackScore);
        str.append(", ");
        str.append("popularity: ");
        str.append(popularity);
        str.append(", ");
        str.append("feedbackStarColor: ");
        str.append(feedbackStarColor);
        str.append(", ");
        str.append("topRated: ");
        str.append(topRated);
        str.append(", ");
        str.append("storeName: ");
        str.append(storeName);
        str.append(", ");
        str.append("storeUrl: ");
        str.append(storeUrl);
        str.append(", ");
        str.append("sellerName: ");
        str.append(sellerName);
        return str.toString();
    }
}
