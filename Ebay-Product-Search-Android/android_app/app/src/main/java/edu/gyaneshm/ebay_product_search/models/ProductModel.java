package edu.gyaneshm.ebay_product_search.models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ProductModel {
    private String title;
    private String productId;
    private String productUrl;
    private ArrayList<String> productImages = new ArrayList<>();
    private String subtitle;
    private Double price;
    private Boolean globalShipping;
    private String condition;
    private String location;
    private ReturnPolicyModel returnDetail;
    private HashMap<String, String> itemSpecifics = new HashMap<>();
    private SellerModel seller;

    public ProductModel(JSONObject data) {
        try {
            title = data.getString("title");
            productId = data.getString("productId");
            productUrl = data.getString("productUrl");
            JSONArray images = data.getJSONArray("productImages");
            for (int i = 0; i < images.length(); i++) {
                productImages.add(images.getString(i));
            }
            subtitle = data.getString("subtitle");
            price = data.getDouble("price");
            globalShipping = data.getBoolean("globalShipping");
            condition = data.getString("condition");
            location = data.getString("location");
            returnDetail = new ReturnPolicyModel(data.getJSONObject("returnDetail"));
            JSONArray specifics = data.getJSONArray("itemSpecifics");
            for (int i = 0; i < specifics.length(); i++) {
                JSONObject item = specifics.getJSONObject(i);
                itemSpecifics.put(item.getString("name"), item.getString("value"));
            }
            seller = new SellerModel(data.getJSONObject("seller"));
        } catch (Exception ex) {
        }
    }

    public String getTitle() {
        return title;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public ArrayList<String> getProductImages() {
        return productImages;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public Double getPrice() {
        return price;
    }

    public String getLocation() {
        return location;
    }

    public Boolean getGlobalShipping() {
        return globalShipping;
    }

    public String getCondition() {
        return condition;
    }

    public ReturnPolicyModel getReturnDetail() {
        return returnDetail;
    }

    public HashMap<String, String> getItemSpecifics() {
        return itemSpecifics;
    }

    public SellerModel getSeller() {
        return seller;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("title: ");
        str.append(title);
        str.append(", ");
        str.append("productId: ");
        str.append(productId);
        str.append(", ");
        str.append("productUrl: ");
        str.append(productUrl);
        str.append(", ");
        str.append("subtitle: ");
        str.append(subtitle);
        str.append(", ");
        str.append("price: ");
        str.append(price);
        str.append(", ");
        str.append("globalShipping: ");
        str.append(globalShipping);
        str.append(", ");
        str.append("condition: ");
        str.append(condition);
        str.append(", ");
        str.append("location: ");
        str.append(location);
        str.append(", ");
        str.append("returnPolicy: ");
        str.append(returnDetail.toString());
        str.append(", ");
        str.append("seller: ");
        str.append(seller.toString());
        str.append(", ");
        str.append("productImages: ");
        str.append(productImages.toString());
        str.append(", ");
        str.append("itemSpecifics: ");
        str.append(itemSpecifics.toString());
        return str.toString();
    }
}
