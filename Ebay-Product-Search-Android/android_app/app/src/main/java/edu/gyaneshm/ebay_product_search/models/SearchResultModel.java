package edu.gyaneshm.ebay_product_search.models;

import org.json.JSONObject;

public class SearchResultModel {
    private String index;
    private String image;
    private String title;
    private String productId;
    private Float price;
    private ShippingModel shipping;
    private String zipcode;
    private String sellerName;
    private boolean inWishList;

    SearchResultModel(JSONObject data) {

    }
}
