package edu.gyaneshm.ebay_product_search.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Comparator;

import edu.gyaneshm.ebay_product_search.R;
import edu.gyaneshm.ebay_product_search.adapters.SimilarItemRecyclerViewAdapter;
import edu.gyaneshm.ebay_product_search.data.ProductData;
import edu.gyaneshm.ebay_product_search.listeners.DataFetchingListener;
import edu.gyaneshm.ebay_product_search.listeners.ItemClickListener;
import edu.gyaneshm.ebay_product_search.models.SimilarProductModel;

public class SimilarProductFragment extends Fragment {
    private LinearLayout mProgressBarContainer;
    private LinearLayout mMainContainer;

    private Spinner mSortBySpinner;
    private Spinner mOrderSpinner;
    private RecyclerView mSimilarItemsRecyclerView;

    private TextView mErrorTextView;

    private ProductData mProductDataInstance;
    private ArrayList<SimilarProductModel> mSimilarProducts = new ArrayList<>();
    SimilarItemRecyclerViewAdapter similarItemRecyclerViewAdapter;

    private boolean isFetched = false;

    private DataFetchingListener dataFetchingListener = new DataFetchingListener() {
        @Override
        public void dataSuccessFullyFetched() {
            if (!isFetched) {
                initiate();
            }
        }
    };

    private ItemClickListener itemClickListener = new ItemClickListener() {
        @Override
        public void onItemClicked(int position) {
            if (mSimilarProducts != null) {
                String productUrl = mSimilarProducts.get(position).getProductUrl();
                if (productUrl != null) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(productUrl));
                    startActivity(browserIntent);
                }
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.similar_fragment_layout, container, false);

        mProgressBarContainer = view.findViewById(R.id.progress_bar_container);
        mMainContainer = view.findViewById(R.id.main_container);

        mSortBySpinner = view.findViewById(R.id.sort_by);
        mOrderSpinner = view.findViewById(R.id.order);
        initializeSpinner();

        mSimilarItemsRecyclerView = view.findViewById(R.id.similar_products);

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

    private void initializeSpinner() {
        ArrayAdapter<CharSequence> sortByAdapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.sort_by,
                android.R.layout.simple_spinner_dropdown_item
        );
        mSortBySpinner.setAdapter(sortByAdapter);
        mSortBySpinner.setSelection(0);
        mSortBySpinner.setEnabled(false);

        ArrayAdapter<CharSequence> orderAdapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.order,
                android.R.layout.simple_spinner_dropdown_item
        );
        mOrderSpinner.setAdapter(orderAdapter);
        mOrderSpinner.setSelection(0);
        mOrderSpinner.setEnabled(false);
    }

    private void initiate() {
        mProgressBarContainer.setVisibility(View.GONE);
        mMainContainer.setVisibility(View.GONE);
        mErrorTextView.setVisibility(View.GONE);

        isFetched = mProductDataInstance.isSimilarItemsFetched();
        if (isFetched) {
            checkError();
        } else {
            mProgressBarContainer.setVisibility(View.VISIBLE);
        }
    }

    private void checkError() {
        String errorMessage = mProductDataInstance.getSimilarItemsError();
        if (errorMessage == null) {
            setUpViews();
            return;
        }
        mErrorTextView.setVisibility(View.VISIBLE);
    }

    private void setUpViews() {
        getDefaultSimilarProducts();
        setUpSearchItemRecycleView();
        setUpSpinners();

        mMainContainer.setVisibility(View.VISIBLE);
    }

    private void setUpSearchItemRecycleView() {
        mSimilarItemsRecyclerView.setHasFixedSize(true);
        mSimilarItemsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        similarItemRecyclerViewAdapter = new SimilarItemRecyclerViewAdapter(getContext(), mSimilarProducts, itemClickListener);
        mSimilarItemsRecyclerView.setAdapter(similarItemRecyclerViewAdapter);
    }

    private void getDefaultSimilarProducts() {
        mSimilarProducts.clear();
        mSimilarProducts.addAll(mProductDataInstance.getSimilarItems());
    }

    private void setUpSpinners() {
        mSortBySpinner.setEnabled(true);

        mSortBySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    mOrderSpinner.setEnabled(false);
                    getDefaultSimilarProducts();
                    similarItemRecyclerViewAdapter.notifyDataSetChanged();
                    return;
                }
                mOrderSpinner.setEnabled(true);
                sortData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mOrderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sortData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void sortData() {
        final int sortBy = mSortBySpinner.getSelectedItemPosition();
        final int order = mOrderSpinner.getSelectedItemPosition();

        final int value;
        if (order == 0) {
            value = -1;
        } else {
            value = 1;
        }

        mSimilarProducts.sort(new Comparator<SimilarProductModel>() {
            @Override
            public int compare(SimilarProductModel o1, SimilarProductModel o2) {
                Double valueLeft = 0.0;
                Double valueRight = 0.0;
                switch (sortBy) {
                    case 1:
                        if (order == 0) {
                            return o2.getTitle().compareTo(o1.getTitle());
                        } else {
                            return o1.getTitle().compareTo(o2.getTitle());
                        }
                    case 2:
                        valueLeft = o1.getPrice();
                        valueRight = o2.getPrice();
                        break;
                    case 3:
                        valueLeft = o1.getDaysLeft();
                        valueRight = o2.getDaysLeft();
                        break;
                }
                if (valueLeft < valueRight) {
                    return value;
                } else {
                    return -1 * value;
                }
            }
        });

        similarItemRecyclerViewAdapter.notifyDataSetChanged();
    }
}
