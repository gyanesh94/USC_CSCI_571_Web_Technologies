package edu.gyaneshm.ebay_product_search.models;

import org.json.JSONObject;

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
            productId = data.getString("productId");
            productUrl = data.getString("productUrl");
            title = data.getString("title");
            price = data.getDouble("price");
            shippingCost = data.getDouble("shippingCost");
            daysLeft = data.getDouble("daysLeft");
            imageUrl = data.getString("imageUrl");
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
