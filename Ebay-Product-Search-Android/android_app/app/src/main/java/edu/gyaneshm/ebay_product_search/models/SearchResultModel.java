package edu.gyaneshm.ebay_product_search.models;

import org.json.JSONObject;

public class SearchResultModel {
    private String index;
    private String image;
    private String title;
    private String productId;
    private String condition;
    private Double price;
    private ShippingModel shipping;
    private String zipcode;
    private String sellerName;
    private boolean inWishList = false;

    public SearchResultModel(JSONObject data) {
        try {
            index = data.getString("index");
            image = data.getString("image");
            title = data.getString("title");
            productId = data.getString("productId");
            condition = data.getString("condition");
            zipcode = data.getString("zipcode");
            sellerName = data.getString("sellerName");
            price = data.getDouble("price");
            shipping = new ShippingModel(data.getJSONObject("shipping"));
        } catch (Exception ex) {
        }
    }

    public String getIndex() {
        return index;
    }

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getProductId() {
        return productId;
    }

    public String getCondition() {
        return condition;
    }

    public Double getPrice() {
        return price;
    }

    public ShippingModel getShipping() {
        return shipping;
    }

    public String getZipcode() {
        return zipcode;
    }

    public String getSellerName() {
        return sellerName;
    }

    public boolean isInWishList() {
        return inWishList;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("index: ");
        str.append(index);
        str.append(", ");
        str.append("image: ");
        str.append(image);
        str.append(", ");
        str.append("title: ");
        str.append(title);
        str.append(", ");
        str.append("productId: ");
        str.append(productId);
        str.append(", ");
        str.append("price: ");
        str.append(price);
        str.append(", ");
        str.append("shipping: ");
        str.append(shipping);
        str.append(", ");
        str.append("zipcode: ");
        str.append(zipcode);
        str.append(", ");
        str.append("sellerName: ");
        str.append(sellerName);
        str.append(", ");
        str.append("inWishList: ");
        str.append(inWishList);
        return str.toString();
    }
}
