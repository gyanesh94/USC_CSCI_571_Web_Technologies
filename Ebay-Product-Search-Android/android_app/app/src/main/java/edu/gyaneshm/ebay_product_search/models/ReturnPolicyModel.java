package edu.gyaneshm.ebay_product_search.models;

import org.json.JSONObject;

import edu.gyaneshm.ebay_product_search.shared.Utils;

public class ReturnPolicyModel {
    private String policy;
    private String returnWithin;
    private String refund;
    private String shippingPaidBy;

    public ReturnPolicyModel(JSONObject data) {
        try {
            policy = Utils.optString(data, "policy");
            returnWithin = Utils.optString(data, "returnWithin");
            refund = Utils.optString(data, "refund");
            shippingPaidBy = Utils.optString(data, "shippingPaidBy");
        } catch (Exception ex){
        }
    }

    public String getPolicy() {
        return policy;
    }

    public String getReturnWithin() {
        return returnWithin;
    }

    public String getRefund() {
        return refund;
    }

    public String getShippingPaidBy() {
        return shippingPaidBy;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("{");
        str.append("policy: ");
        str.append(policy);
        str.append(", ");
        str.append("returnWithin: ");
        str.append(returnWithin);
        str.append(", ");
        str.append("refund: ");
        str.append(refund);
        str.append(", ");
        str.append("shippingPaidBy: ");
        str.append(shippingPaidBy);
        str.append("}");
        return str.toString();
    }
}
