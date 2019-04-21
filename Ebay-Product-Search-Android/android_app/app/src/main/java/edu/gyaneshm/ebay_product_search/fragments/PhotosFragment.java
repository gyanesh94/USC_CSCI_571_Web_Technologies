package edu.gyaneshm.ebay_product_search.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import edu.gyaneshm.ebay_product_search.R;
import edu.gyaneshm.ebay_product_search.data.ProductData;
import edu.gyaneshm.ebay_product_search.listeners.DataFetchingListener;

public class PhotosFragment extends Fragment {
    private LayoutInflater layoutInflater;
    private LinearLayout mProgressBarContainer;
    private ScrollView mMainContainer;
    private LinearLayout mPhotosContainer;

    private TextView mErrorTextView;

    private ProductData mProductDataInstance;

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
        layoutInflater = inflater;
        View view = inflater.inflate(R.layout.photos_fragment_layout, container, false);

        mProgressBarContainer = view.findViewById(R.id.progress_bar_container);
        mMainContainer = view.findViewById(R.id.main_container);
        mPhotosContainer = view.findViewById(R.id.photos_container);

        mErrorTextView = view.findViewById(R.id.error);

        mProductDataInstance = ProductData.getInstance();
        mProductDataInstance.registerCallback(dataFetchingListener);
        initiate();

        return view;
    }

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
        String errorMessage = mProductDataInstance.getGooglePhotosError();
        if (errorMessage == null) {
            setUpViews();
            return;
        }
        mErrorTextView.setVisibility(View.VISIBLE);
    }

    private void setUpViews() {
        ArrayList<String> googlePhotos = mProductDataInstance.getGooglePhotos();

        for (String photo :
                googlePhotos) {
            View view = layoutInflater.inflate(R.layout.photos_fragment_photos_card_view, mPhotosContainer, false);
            ImageView imageView = view.findViewById(R.id.image);
            Glide.with(getContext())
                    .load(photo)
                    .into(imageView);
            mPhotosContainer.addView(view);
        }
        mMainContainer.setVisibility(View.VISIBLE);
    }
}
