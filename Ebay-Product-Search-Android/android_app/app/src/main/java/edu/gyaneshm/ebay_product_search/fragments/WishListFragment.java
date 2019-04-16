package edu.gyaneshm.ebay_product_search.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import edu.gyaneshm.ebay_product_search.ProductDetailActivity;
import edu.gyaneshm.ebay_product_search.R;
import edu.gyaneshm.ebay_product_search.adapters.SearchItemRecyclerViewAdapter;
import edu.gyaneshm.ebay_product_search.data.ProductData;
import edu.gyaneshm.ebay_product_search.data.WishListData;
import edu.gyaneshm.ebay_product_search.listeners.ItemClickListener;
import edu.gyaneshm.ebay_product_search.listeners.WishListChangeListener;
import edu.gyaneshm.ebay_product_search.models.SearchResultModel;
import edu.gyaneshm.ebay_product_search.shared.Utils;

public class WishListFragment extends Fragment {
    private TextView mTotalItemsTextView;
    private TextView mPriceTextView;
    private TextView mErrorTextView;
    private RecyclerView mWishListRecyclerView;

    private WishListData mWishListData;
    private ArrayList<SearchResultModel> wishList;

    private SearchItemRecyclerViewAdapter searchItemRecyclerViewAdapter = null;

    private WishListChangeListener wishListChangeListener = new WishListChangeListener() {
        @Override
        public void wishListItemChanged(Integer position) {
            refreshView();
            if (searchItemRecyclerViewAdapter != null) {
                searchItemRecyclerViewAdapter.notifyDataSetChanged();
            }
        }
    };

    private ItemClickListener itemClickListener = new ItemClickListener() {
        @Override
        public void onItemClicked(int position) {
            ProductData.getInstance().setItem(wishList.get(position));
            Intent intent = new Intent(getContext(), ProductDetailActivity.class);
            startActivity(intent);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wishlist_fragment_layout, container, false);

        mTotalItemsTextView = view.findViewById(R.id.wish_list_total_items);
        mPriceTextView = view.findViewById(R.id.wish_list_total_price);
        mErrorTextView = view.findViewById(R.id.wish_list_error);
        mWishListRecyclerView = view.findViewById(R.id.wish_list_items);

        mWishListData = WishListData.getInstance();
        mWishListData.registerCallback(wishListChangeListener);

        setupView();

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWishListData.unregisterCallback(wishListChangeListener);
    }

    private void setupView() {
        wishList = mWishListData.getWishList();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        mWishListRecyclerView.setHasFixedSize(true);
        mWishListRecyclerView.setLayoutManager(gridLayoutManager);
        searchItemRecyclerViewAdapter = new SearchItemRecyclerViewAdapter(getContext(), wishList, itemClickListener);
        mWishListRecyclerView.setAdapter(searchItemRecyclerViewAdapter);

        refreshView();
    }

    private void refreshView() {
        Double totalPrice = 0.0;
        for (int i = 0; i < wishList.size(); i++) {
            totalPrice += wishList.get(i).getPrice();
        }
        mPriceTextView.setText(getString(R.string.price, Utils.formatPriceToString(totalPrice)));
        mTotalItemsTextView.setText(getString(R.string.wish_list_total_items, wishList.size()));

        if (wishList.size() == 0) {
            mWishListRecyclerView.setVisibility(View.GONE);
            mErrorTextView.setVisibility(View.VISIBLE);
            return;
        }
        mWishListRecyclerView.setVisibility(View.VISIBLE);
        mErrorTextView.setVisibility(View.GONE);
    }
}
