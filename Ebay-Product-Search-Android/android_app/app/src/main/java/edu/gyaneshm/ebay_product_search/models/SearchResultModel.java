package edu.gyaneshm.ebay_product_search.models;

import org.json.JSONObject;

import edu.gyaneshm.ebay_product_search.shared.Utils;

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
            index = Utils.optString(data, "index");
            image = Utils.optString(data, "image");
            title = Utils.optString(data, "title");
            productId = Utils.optString(data, "productId");
            condition = Utils.optString(data, "condition");
            zipcode = Utils.optString(data, "zipcode");
            sellerName = Utils.optString(data, "sellerName");
            price = Utils.optDouble(data, "price");
            shipping = new ShippingModel(data.getJSONObject("shipping"));
            if (data.has("inWishList")) {
                inWishList = Utils.optBoolean(data, "inWishList");
            }
        } catch (Exception ex) {
        }
    }

    public JSONObject toJSONObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("index", index);
            obj.put("image", image);
            obj.put("title", title);
            obj.put("productId", productId);
            obj.put("condition", condition);
            obj.put("price", price);
            obj.put("shipping", shipping.toJSONObject());
            obj.put("zipcode", zipcode);
            obj.put("sellerName", sellerName);
            obj.put("inWishList", inWishList);
        } catch (Exception ex) {
        }
        return obj;
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

    public boolean isInWishList() {
        return inWishList;
    }

    public void setInWishList(boolean inWishList) {
        this.inWishList = inWishList;
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
