package edu.gyaneshm.ebay_product_search.data;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import edu.gyaneshm.ebay_product_search.EbayProductSearchApplication;
import edu.gyaneshm.ebay_product_search.R;
import edu.gyaneshm.ebay_product_search.listeners.WishListChangeListener;
import edu.gyaneshm.ebay_product_search.models.SearchResultModel;

public class WishListData {
    private static WishListData mWishListData = null;
    private ArrayList<WishListChangeListener> mCallbacks = new ArrayList<>();

    private final String WISH_LIST_SHARED_PREFERENCE_KEY = "wish_list";

    private ArrayList<SearchResultModel> wishList = null;
    private SharedPreferences sharedPreferences = null;


    private WishListData() {
    }

    public static WishListData getInstance() {
        if (mWishListData == null) {
            mWishListData = new WishListData();
        }
        return mWishListData;
    }

    private void setWishListToSharedPreference() {
        if (sharedPreferences == null) {
            sharedPreferences = EbayProductSearchApplication.getInstance().getApplicationContext().getSharedPreferences(
                    EbayProductSearchApplication.getInstance().getApplicationContext().getString(R.string.shared_preference_file_key), Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(WISH_LIST_SHARED_PREFERENCE_KEY, convertWishListToJSON().toString());
        editor.commit();
    }

    private void getWishListFromSharedPreference() {
        if (sharedPreferences == null) {
            sharedPreferences = EbayProductSearchApplication.getInstance().getApplicationContext().getSharedPreferences(
                    EbayProductSearchApplication.getInstance().getApplicationContext().getString(R.string.shared_preference_file_key), Context.MODE_PRIVATE);
        }
        String wishListString = sharedPreferences.getString(WISH_LIST_SHARED_PREFERENCE_KEY, null);
        wishList = new ArrayList<>();
        if (wishListString == null) {
            return;
        }
        try {
            JSONArray arr = new JSONArray(wishListString);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject result = arr.getJSONObject(i);
                wishList.add(new SearchResultModel(result));
            }
        } catch (Exception ex) {
        }
    }

    public void addItemToWishList(SearchResultModel item) {
        addItemToWishList(item, null);
    }

    public void addItemToWishList(SearchResultModel item, Integer position) {
        item.setInWishList(true);
        if (wishList == null) {
            getWishListFromSharedPreference();
        }
        wishList.add(item);
        setWishListToSharedPreference();
        notifyWishListChangeWithPosition(position);
    }

    public void removeItemFromWishList(SearchResultModel item) {
        removeItemFromWishList(item, null);
    }

    public void removeItemFromWishList(SearchResultModel item, Integer position) {
        item.setInWishList(false);
        if (wishList == null) {
            getWishListFromSharedPreference();
        }
        wishList.remove(item);
        setWishListToSharedPreference();
        notifyWishListChangeWithPosition(position);
    }

    public ArrayList<SearchResultModel> getWishList() {
        if (wishList == null) {
            getWishListFromSharedPreference();
        }
        return wishList;
    }

    public void registerCallback(WishListChangeListener callback) {
        mCallbacks.add(callback);
    }

    public void unregisterCallback(WishListChangeListener callback) {
        mCallbacks.remove(callback);
    }

    private void notifyWishListChangeWithPosition(Integer position) {
        for (int i = 0; i < mCallbacks.size(); i++) {
            mCallbacks.get(i).wishListItemChanged(position);
        }
    }

    private JSONArray convertWishListToJSON() {
        JSONArray arr = new JSONArray();
        try {
            for (int i = 0; i < wishList.size(); i++) {
                arr.put(wishList.get(i).toJSONObject());
            }
        } catch (Exception ex) {
        }
        return arr;
    }

    public boolean itemInWishList(SearchResultModel item) {
        if (wishList == null) {
            getWishListFromSharedPreference();
        }
        if (wishList.size() == 0) {
            return false;
        }

        for (int i = 0; i < wishList.size(); i++) {
            if (item.getProductId().equals(wishList.get(i).getProductId())) {
                return true;
            }
        }
        return false;
    }
}
