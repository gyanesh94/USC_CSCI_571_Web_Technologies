package edu.gyaneshm.ebay_product_search.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.wssholmes.stark.circular_score.CircularScoreView;

import edu.gyaneshm.ebay_product_search.R;
import edu.gyaneshm.ebay_product_search.data.ProductData;
import edu.gyaneshm.ebay_product_search.listeners.DataFetchingListener;
import edu.gyaneshm.ebay_product_search.models.ProductModel;
import edu.gyaneshm.ebay_product_search.models.ReturnPolicyModel;
import edu.gyaneshm.ebay_product_search.models.SearchResultModel;
import edu.gyaneshm.ebay_product_search.models.SellerModel;
import edu.gyaneshm.ebay_product_search.models.ShippingModel;
import edu.gyaneshm.ebay_product_search.shared.Utils;

public class ShippingFragment extends Fragment {
    private LinearLayout mProgressBarContainer;
    private ScrollView mMainContainer;

    private LinearLayout mSoldByLinearLayout;
    private LinearLayout mSoldByStoreNameLinearLayout;
    private TextView mSoldByStoreNameTextView;
    private LinearLayout mSoldByFeedbackScoreLinearLayout;
    private TextView mSoldByFeedbackScoreTextView;
    private LinearLayout mSoldByPopularityLinearLayout;
    private CircularScoreView mSoldByPopularityCircularScoreView;
    private LinearLayout mSoldByFeedbackStarLinearLayout;
    private ImageView mSoldByFeedbackStarImageView;

    private LinearLayout mShippingInfoLinearLayout;
    private LinearLayout mShippingInfoShippingCostLinearLayout;
    private TextView mShippingInfoShippingCostTextView;
    private LinearLayout mShippingInfoGlobalShippingLinearLayout;
    private TextView mShippingInfoGlobalShippingTextView;
    private LinearLayout mShippingInfoHandlingTimeLinearLayout;
    private TextView mShippingInfoHandlingTimeTextView;
    private LinearLayout mShippingInfoConditionLinearLayout;
    private TextView mShippingInfoConditionTextView;

    private LinearLayout mReturnPolicyLinearLayout;
    private LinearLayout mReturnPolicyPolicyLinearLayout;
    private TextView mReturnPolicyPolicyTextView;
    private LinearLayout mReturnPolicyReturnsWithinLinearLayout;
    private TextView mReturnPolicyReturnsWithinTextView;
    private LinearLayout mReturnPolicyRefundModeLinearLayout;
    private TextView mReturnPolicyRefundModeTextView;
    private LinearLayout mReturnPolicyShippedByLinearLayout;
    private TextView mReturnPolicyShippedByTextView;

    private TextView mErrorTextView;

    private ProductData mProductDataInstance;
    private ProductModel productDetail;
    private SearchResultModel searchResultItem;

    private boolean isFetched = false;

    private DataFetchingListener dataFetchingListener = new DataFetchingListener() {
        @Override
        public void dataSuccessFullyFetched() {
            if (!isFetched) {
                initiate();
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shipping_fragment_layout, container, false);

        mProgressBarContainer = view.findViewById(R.id.progress_bar_container);
        mMainContainer = view.findViewById(R.id.main_container);

        mSoldByLinearLayout = view.findViewById(R.id.sold_by_section);
        mSoldByStoreNameLinearLayout = view.findViewById(R.id.sold_by_section_store_name_layout);
        mSoldByStoreNameTextView = view.findViewById(R.id.sold_by_section_store_name);
        mSoldByFeedbackScoreLinearLayout = view.findViewById(R.id.sold_by_section_feedback_score_layout);
        mSoldByFeedbackScoreTextView = view.findViewById(R.id.sold_by_section_feedback_score);
        mSoldByPopularityLinearLayout = view.findViewById(R.id.sold_by_section_popularity_layout);
        mSoldByPopularityCircularScoreView = view.findViewById(R.id.sold_by_section_popularity);
        mSoldByFeedbackStarLinearLayout = view.findViewById(R.id.sold_by_section_feedback_star_layout);
        mSoldByFeedbackStarImageView = view.findViewById(R.id.sold_by_section_feedback_star);

        mShippingInfoLinearLayout = view.findViewById(R.id.shipping_info_section);
        mShippingInfoShippingCostLinearLayout = view.findViewById(R.id.shipping_info_section_shipping_cost_layout);
        mShippingInfoShippingCostTextView = view.findViewById(R.id.shipping_info_section_shipping_cost);
        mShippingInfoGlobalShippingLinearLayout = view.findViewById(R.id.shipping_info_section_global_shipping_layout);
        mShippingInfoGlobalShippingTextView = view.findViewById(R.id.shipping_info_section_global_shipping);
        mShippingInfoHandlingTimeLinearLayout = view.findViewById(R.id.shipping_info_section_handling_time_layout);
        mShippingInfoHandlingTimeTextView = view.findViewById(R.id.shipping_info_section_handling_time);
        mShippingInfoConditionLinearLayout = view.findViewById(R.id.shipping_info_section_condition_layout);
        mShippingInfoConditionTextView = view.findViewById(R.id.shipping_info_section_condition);

        mReturnPolicyLinearLayout = view.findViewById(R.id.return_policy_section);
        mReturnPolicyPolicyLinearLayout = view.findViewById(R.id.return_policy_section_policy_layout);
        mReturnPolicyPolicyTextView = view.findViewById(R.id.return_policy_section_policy);
        mReturnPolicyReturnsWithinLinearLayout = view.findViewById(R.id.return_policy_section_returns_within_layout);
        mReturnPolicyReturnsWithinTextView = view.findViewById(R.id.return_policy_section_returns_within);
        mReturnPolicyRefundModeLinearLayout = view.findViewById(R.id.return_policy_section_refund_mode_layout);
        mReturnPolicyRefundModeTextView = view.findViewById(R.id.return_policy_section_refund_mode);
        mReturnPolicyShippedByLinearLayout = view.findViewById(R.id.return_policy_section_shipped_by_layout);
        mReturnPolicyShippedByTextView = view.findViewById(R.id.return_policy_section_shipped_by);

        mErrorTextView = view.findViewById(R.id.error);

        mProductDataInstance = ProductData.getInstance();
        mProductDataInstance.registerCallback(dataFetchingListener);
        initiate();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mProductDataInstance.unregisterCallback(dataFetchingListener);
    }

    private void initiate() {
        mProgressBarContainer.setVisibility(View.GONE);
        mMainContainer.setVisibility(View.GONE);
        mErrorTextView.setVisibility(View.GONE);

        isFetched = mProductDataInstance.isProductDetailFetched();
        if (isFetched) {
            checkError();
        } else {
            mProgressBarContainer.setVisibility(View.VISIBLE);
        }
    }

    private void checkError() {
        String errorMessage = mProductDataInstance.getProductDetailError();
        if (errorMessage == null) {
            setUpViews();
            return;
        }
        mErrorTextView.setVisibility(View.VISIBLE);
    }

    private void setUpViews() {
        productDetail = mProductDataInstance.getProductDetail();
        searchResultItem = mProductDataInstance.getItem();

        boolean containerValid = setUpSoldBySection();

        containerValid = setUpShippingInfoSection() || containerValid;
        containerValid = setUpReturnPolicySection() || containerValid;

        if (containerValid) {
            mMainContainer.setVisibility(View.VISIBLE);
        } else {
            mErrorTextView.setVisibility(View.VISIBLE);
        }
    }

    private boolean setUpSoldBySection() {
        boolean sectionValid = false;
        SellerModel seller = productDetail.getSeller();

        String storeName = seller.getStoreName();

        if (storeName != null) {
            mSoldByStoreNameTextView.setText(storeName);
            mSoldByStoreNameTextView.setPaintFlags(mSoldByStoreNameTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            final String storeUrl = seller.getStoreUrl();
            if (storeUrl != null) {
                mSoldByStoreNameTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(storeUrl));
                        startActivity(browserIntent);

                    }
                });
            }
            mSoldByStoreNameLinearLayout.setVisibility(View.VISIBLE);
            sectionValid = true;
        }

        Double feedbackScore = seller.getFeedbackScore();
        if (feedbackScore != null) {
            mSoldByFeedbackScoreTextView.setText(Utils.doubleToString(feedbackScore));
            mSoldByFeedbackScoreLinearLayout.setVisibility(View.VISIBLE);
            sectionValid = true;
        }

        Double popularity = seller.getPopularity();
        if (feedbackScore != null) {
            mSoldByPopularityCircularScoreView.setBackgroundColor(Color.WHITE);
            mSoldByPopularityCircularScoreView.setTextColor(Color.BLACK);
            mSoldByPopularityCircularScoreView.setPrimaryColor(Color.parseColor("#FFD37159"));
            mSoldByPopularityCircularScoreView.setSecondaryColor(Color.parseColor("#A69256EE"));
            mSoldByPopularityCircularScoreView.setScore(Utils.doubleToInt(popularity));
            mSoldByPopularityLinearLayout.setVisibility(View.VISIBLE);

            String feedbackStarColor = seller.getFeedbackStarColor();
            if (feedbackStarColor != null) {
                Drawable drawable;
                if (feedbackScore > 10000) {
                    drawable = ContextCompat.getDrawable(getContext(), R.drawable.star_circle);
                } else {
                    drawable = ContextCompat.getDrawable(getContext(), R.drawable.star_circle_outline);
                }
                if (feedbackScore < 10) {
                    drawable.setColorFilter(new PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY));
                } else {
                    String[] colors = getResources().getStringArray(R.array.colors);
                    String[] colorCode = getResources().getStringArray(R.array.color_code);
                    for (int i = 0; i < colors.length; i++) {
                        if (feedbackStarColor.contains(colors[i])) {
                            drawable.setColorFilter(new PorterDuffColorFilter(Color.parseColor(colorCode[i]), PorterDuff.Mode.MULTIPLY));
                            break;
                        }
                    }
                }
                mSoldByFeedbackStarImageView.setImageDrawable(drawable);
                mSoldByFeedbackStarLinearLayout.setVisibility(View.VISIBLE);
            }

            sectionValid = true;
        }

        if (sectionValid) {
            mSoldByLinearLayout.setVisibility(View.VISIBLE);
        }
        return sectionValid;
    }

    private boolean setUpShippingInfoSection() {
        boolean sectionValid = false;

        ShippingModel shipping = searchResultItem.getShipping();

        Double shippingCost = shipping.getCost();
        if (shippingCost != null) {
            if (shippingCost == 0) {
                mShippingInfoShippingCostTextView.setText(R.string.free_shipping);
            } else {
                mShippingInfoShippingCostTextView.setText(getString(R.string.price, Utils.formatPriceToString(shippingCost)));
            }
            mShippingInfoShippingCostLinearLayout.setVisibility(View.VISIBLE);
            sectionValid = true;
        }

        Boolean globalShipping = productDetail.getGlobalShipping();
        if (globalShipping != null) {
            if (globalShipping) {
                mShippingInfoGlobalShippingTextView.setText(R.string.yes);
            } else {
                mShippingInfoGlobalShippingTextView.setText(R.string.no);
            }
            mShippingInfoGlobalShippingLinearLayout.setVisibility(View.VISIBLE);
            sectionValid = true;
        }

        String handlingTime = shipping.getHandlingTime();
        if (handlingTime != null) {
            mShippingInfoHandlingTimeTextView.setText(handlingTime);
            mShippingInfoHandlingTimeLinearLayout.setVisibility(View.VISIBLE);
            sectionValid = true;
        }

        String condition = productDetail.getCondition();
        if (condition != null) {
            mShippingInfoConditionTextView.setText(condition);
            mShippingInfoConditionLinearLayout.setVisibility(View.VISIBLE);
            sectionValid = true;
        }

        if (sectionValid) {
            mShippingInfoLinearLayout.setVisibility(View.VISIBLE);
        }
        return sectionValid;
    }

    private boolean setUpReturnPolicySection() {
        boolean sectionValid = false;
        ReturnPolicyModel returnPolicy = productDetail.getReturnDetail();

        String policy = returnPolicy.getPolicy();
        if (policy != null) {
            mReturnPolicyPolicyTextView.setText(policy);
            mReturnPolicyPolicyLinearLayout.setVisibility(View.VISIBLE);
            sectionValid = true;
        }

        String returnWithin = returnPolicy.getReturnWithin();
        if (returnWithin != null) {
            mReturnPolicyReturnsWithinTextView.setText(returnWithin);
            mReturnPolicyReturnsWithinLinearLayout.setVisibility(View.VISIBLE);
            sectionValid = true;
        }

        String refund = returnPolicy.getRefund();
        if (refund != null) {
            mReturnPolicyRefundModeTextView.setText(refund);
            mReturnPolicyRefundModeLinearLayout.setVisibility(View.VISIBLE);
            sectionValid = true;
        }

        String shippingPaidBy = returnPolicy.getShippingPaidBy();
        if (shippingPaidBy != null) {
            mReturnPolicyShippedByTextView.setText(shippingPaidBy);
            mReturnPolicyShippedByLinearLayout.setVisibility(View.VISIBLE);
            sectionValid = true;
        }

        if (sectionValid) {
            mReturnPolicyLinearLayout.setVisibility(View.VISIBLE);
        }
        return sectionValid;
    }
}
