package edu.gyaneshm.ebay_product_search.models;

import org.json.JSONObject;

import edu.gyaneshm.ebay_product_search.shared.Utils;

public class SimilarProductModel {
    private String productId;
    private String productUrl;
    private String title;
    private Double price;
    private Double shippingCost;
    private Double daysLeft;
    private String imageUrl;

    public SimilarProductModel(JSONObject data) {
        try {
            productId = Utils.optString(data, "productId");
            productUrl = Utils.optString(data, "productUrl");
            title = Utils.optString(data, "title");
            price = Utils.optDouble(data, "price");
            shippingCost = Utils.optDouble(data, "shippingCost");
            daysLeft = Utils.optDouble(data, "daysLeft");
            imageUrl = Utils.optString(data, "imageUrl");
        } catch (Exception ex) {
        }
    }

    public String getProductId() {
        return productId;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public String getTitle() {
        return title;
    }

    public Double getPrice() {
        return price;
    }

    public Double getShippingCost() {
        return shippingCost;
    }

    public Double getDaysLeft() {
        return daysLeft;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
