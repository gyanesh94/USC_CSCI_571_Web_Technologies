package edu.gyaneshm.ebay_product_search.models;

import org.json.JSONObject;

import edu.gyaneshm.ebay_product_search.shared.Utils;

public class ShippingModel {
    private Double cost;
    private String locations;
    private String handlingTime;
    private boolean expedited;
    private boolean oneDay;
    private boolean returnAccepted;

    ShippingModel(JSONObject data) {
        try {
            cost = Utils.optDouble(data, "cost");
            locations = Utils.optString(data, "locations");
            handlingTime = Utils.optString(data, "handlingTime");
            expedited = Utils.optBoolean(data, "expedited");
            oneDay = Utils.optBoolean(data, "oneDay");
            returnAccepted = Utils.optBoolean(data, "returnAccepted");
        } catch (Exception ex) {
        }
    }

    public JSONObject toJSONObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("cost", cost);
            obj.put("locations", locations);
            obj.put("handlingTime", handlingTime);
            obj.put("expedited", expedited);
            obj.put("oneDay", oneDay);
            obj.put("returnAccepted", returnAccepted);
        } catch (Exception ex) {
        }
        return obj;
    }

    public Double getCost() {
        return cost;
    }

    public String getLocations() {
        return locations;
    }

    public String getHandlingTime() {
        return handlingTime;
    }

    public boolean isExpedited() {
        return expedited;
    }

    public boolean isOneDay() {
        return oneDay;
    }

    public boolean isReturnAccepted() {
        return returnAccepted;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("{");
        str.append("cost: ");
        str.append(cost);
        str.append(", ");
        str.append("locations: ");
        str.append(locations);
        str.append(", ");
        str.append("handlingTime: ");
        str.append(handlingTime);
        str.append(", ");
        str.append("expedited: ");
        str.append(expedited);
        str.append(", ");
        str.append("oneDay: ");
        str.append(oneDay);
        str.append(", ");
        str.append("returnAccepted: ");
        str.append(returnAccepted);
        str.append("}");
        return str.toString();
    }
}
