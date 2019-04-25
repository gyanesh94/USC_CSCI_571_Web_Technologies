package edu.gyaneshm.ebay_product_search.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import edu.gyaneshm.ebay_product_search.R;
import edu.gyaneshm.ebay_product_search.data.WishListData;
import edu.gyaneshm.ebay_product_search.listeners.ItemClickListener;
import edu.gyaneshm.ebay_product_search.models.SearchResultModel;
import edu.gyaneshm.ebay_product_search.shared.Utils;

public class SearchItemRecyclerViewAdapter extends RecyclerView.Adapter<SearchItemRecyclerViewAdapter.ItemViewHolder> {

    private Context mContext;
    private ArrayList<SearchResultModel> mSearchResults;
    private ItemClickListener mItemClickListener;

    public SearchItemRecyclerViewAdapter(Context context, ArrayList<SearchResultModel> searchResults, ItemClickListener itemClickListener) {
        mContext = context;
        mSearchResults = searchResults;
        mItemClickListener = itemClickListener;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.search_item_card_view, null);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        SearchResultModel searchResult = mSearchResults.get(position);

        String title = searchResult.getTitle();
        if (title == null) {
            holder.mTitleTextView.setText(mContext.getString(R.string.n_a));
        } else {
            holder.mTitleTextView.setText(title);
        }

        String zipcode = searchResult.getZipcode();
        if (zipcode == null) {
            holder.mZipcodeTextView.setText(mContext.getString(R.string.n_a));
        } else {
            holder.mZipcodeTextView.setText(mContext.getString(R.string.card_view_zipcode, zipcode));
        }

        Double price = searchResult.getPrice();
        if (price == null) {
            holder.mPriceTextView.setText(mContext.getString(R.string.n_a));
        } else {
            holder.mPriceTextView.setText(mContext.getString(R.string.price, Utils.formatPriceToString(price)));
        }

        Double shippingCost = searchResult.getShipping().getCost();
        if (shippingCost == null) {
            holder.mShippingTextView.setText(mContext.getString(R.string.n_a));
        } else if (shippingCost == 0) {
            holder.mShippingTextView.setText(mContext.getString(R.string.free_shipping));
        } else {
            holder.mShippingTextView.setText(mContext.getString(R.string.price, Utils.formatPriceToString(shippingCost)));
        }

        String condition = searchResult.getCondition();
        if (condition == null) {
            holder.mConditionTextView.setText(mContext.getString(R.string.n_a));
        } else {
            holder.mConditionTextView.setText(condition);
        }

        String itemUrl = searchResult.getImage();
        if (itemUrl == null) {
            Glide.with(mContext).clear(holder.mItemImageButton);
            holder.mItemImageButton.setImageDrawable(mContext.getDrawable(R.drawable.error_na));
        } else {
            Glide.with(mContext)
                    .load(itemUrl)
                    .into(holder.mItemImageButton);

        }

        boolean inWishList = searchResult.isInWishList();
        if (inWishList) {
            holder.mCartImageButton.setImageDrawable(mContext.getDrawable(R.drawable.cart_remove));
        } else {
            holder.mCartImageButton.setImageDrawable(mContext.getDrawable(R.drawable.cart_add));
        }
    }

    @Override
    public int getItemCount() {
        return mSearchResults.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView mTitleTextView;
        TextView mZipcodeTextView;
        TextView mPriceTextView;
        TextView mShippingTextView;
        TextView mConditionTextView;
        ImageButton mItemImageButton;
        ImageButton mCartImageButton;

        ItemViewHolder(View itemView) {
            super(itemView);

            mTitleTextView = itemView.findViewById(R.id.card_item_product_title);
            mZipcodeTextView = itemView.findViewById(R.id.card_item_product_zipcode);
            mPriceTextView = itemView.findViewById(R.id.card_item_product_price);
            mShippingTextView = itemView.findViewById(R.id.card_item_product_shipping);
            mConditionTextView = itemView.findViewById(R.id.card_item_product_condition);
            mItemImageButton = itemView.findViewById(R.id.card_item_product_image);
            mCartImageButton = itemView.findViewById(R.id.card_item_cart);

            mCartImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SearchResultModel item = mSearchResults.get(getAdapterPosition());
                    if (item.isInWishList()) {
                        WishListData.getInstance().removeItemFromWishList(item, getAdapterPosition());
                    } else {
                        WishListData.getInstance().addItemToWishList(item, getAdapterPosition());
                    }
                }
            });

            mItemImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClicked(getAdapterPosition());
                }
            });
        }
    }
}
