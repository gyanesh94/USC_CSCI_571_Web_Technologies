package edu.gyaneshm.ebay_product_search.models;

import org.json.JSONObject;

public class ShippingModel {
    private Double cost;
    private String locations;
    private String handlingTime;
    private boolean expedited;
    private boolean oneDay;
    private boolean returnAccepted;

    ShippingModel(JSONObject data) {
        try {
            cost = data.getDouble("cost");
            locations = data.getString("locations");
            handlingTime = data.getString("handlingTime");
            expedited = data.getBoolean("expedited");
            oneDay = data.getBoolean("oneDay");
            returnAccepted = data.getBoolean("returnAccepted");
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
        return str.toString();
    }
}
