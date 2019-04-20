package edu.gyaneshm.ebay_product_search.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import edu.gyaneshm.ebay_product_search.R;
import edu.gyaneshm.ebay_product_search.listeners.ItemClickListener;
import edu.gyaneshm.ebay_product_search.models.SimilarProductModel;
import edu.gyaneshm.ebay_product_search.shared.Utils;

public class SimilarItemRecyclerViewAdapter extends RecyclerView.Adapter<SimilarItemRecyclerViewAdapter.ItemViewHolder> {

    private Context mContext;
    private ArrayList<SimilarProductModel> mSimilarProducts;
    private ItemClickListener mItemClickListener;

    public SimilarItemRecyclerViewAdapter(Context context, ArrayList<SimilarProductModel> similarProduct, ItemClickListener itemClickListener) {
        mContext = context;
        mSimilarProducts = similarProduct;
        mItemClickListener = itemClickListener;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.similar_fragment_similar_item_card_view, null);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        SimilarProductModel similarProduct = mSimilarProducts.get(position);

        String title = similarProduct.getTitle();
        if (title == null) {
            holder.mTitleTextView.setText(mContext.getString(R.string.n_a));
        } else {
            holder.mTitleTextView.setText(title);
        }

        Double shippingCost = similarProduct.getShippingCost();
        if (shippingCost == null) {
            holder.mShippingTextView.setText(mContext.getString(R.string.n_a));
        } else if (shippingCost == 0) {
            holder.mShippingTextView.setText(mContext.getString(R.string.free_shipping));
        } else {
            holder.mShippingTextView.setText(mContext.getString(R.string.price, Utils.formatPriceToString(shippingCost)));
        }

        Double daysLeft = similarProduct.getDaysLeft();
        if (daysLeft == null) {
            holder.mDaysTextView.setText(mContext.getString(R.string.n_a));
        } else if (daysLeft == 0 || daysLeft == 1) {
            holder.mDaysTextView.setText(mContext.getString(R.string.day_left, Utils.doubleToString(daysLeft)));
        } else {
            holder.mDaysTextView.setText(mContext.getString(R.string.days_left, Utils.doubleToString(daysLeft)));
        }

        Double price = similarProduct.getPrice();
        if (price == null) {
            holder.mPriceTextView.setText(mContext.getString(R.string.n_a));
        } else {
            holder.mPriceTextView.setText(mContext.getString(R.string.price, Utils.formatPriceToString(price)));
        }

        String itemUrl = similarProduct.getImageUrl();
        if (itemUrl == null) {
            Glide.with(mContext).clear(holder.mImageView);
        } else {
            Glide.with(mContext)
                    .load(itemUrl)
                    .error(R.drawable.error_na)
                    .into(holder.mImageView);
        }
    }

    @Override
    public int getItemCount() {
        return mSimilarProducts.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        CardView mCardView;
        TextView mTitleTextView;
        TextView mPriceTextView;
        TextView mShippingTextView;
        TextView mDaysTextView;
        ImageView mImageView;

        ItemViewHolder(View itemView) {
            super(itemView);

            mCardView = itemView.findViewById(R.id.card_view);
            mTitleTextView = itemView.findViewById(R.id.title);
            mPriceTextView = itemView.findViewById(R.id.price);
            mShippingTextView = itemView.findViewById(R.id.shipping);
            mDaysTextView = itemView.findViewById(R.id.days);
            mImageView = itemView.findViewById(R.id.image);

            mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClicked(getAdapterPosition());
                }
            });
        }
    }
}
