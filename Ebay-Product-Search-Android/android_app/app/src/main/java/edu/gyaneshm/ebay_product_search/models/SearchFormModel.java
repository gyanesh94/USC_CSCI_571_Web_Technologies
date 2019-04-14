package edu.gyaneshm.ebay_product_search.models;

import org.json.JSONObject;

import java.util.HashMap;

public class SearchFormModel {
    private String keyword;
    private String category;
    private HashMap<String, Boolean> condition;
    private HashMap<String, Boolean> shipping;
    private boolean useLocation;
    private String distance;
    private String here;
    private String zipcode;

    public JSONObject toJSONObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("keyword", keyword);
            obj.put("category", category);
            obj.put("condition", new JSONObject(condition));
            obj.put("shipping", new JSONObject(shipping));
            obj.put("useLocation", useLocation);
            obj.put("distance", distance);
            obj.put("here", here);
            obj.put("zipcode", zipcode);
        } catch (Exception ex) {
        }
        return obj;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setCondition(HashMap<String, Boolean> condition) {
        this.condition = condition;
    }

    public void setShipping(HashMap<String, Boolean> shipping) {
        this.shipping = shipping;
    }

    public void setUseLocation(Boolean useLocation) {
        this.useLocation = useLocation;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public void setHere(String here) {
        this.here = here;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }
}
