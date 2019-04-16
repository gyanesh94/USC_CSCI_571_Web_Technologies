package edu.gyaneshm.ebay_product_search;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import edu.gyaneshm.ebay_product_search.adapters.SearchItemRecyclerViewAdapter;
import edu.gyaneshm.ebay_product_search.data.ProductData;
import edu.gyaneshm.ebay_product_search.data.SearchResultData;
import edu.gyaneshm.ebay_product_search.data.WishListData;
import edu.gyaneshm.ebay_product_search.listeners.ItemClickListener;
import edu.gyaneshm.ebay_product_search.listeners.WishListChangeListener;
import edu.gyaneshm.ebay_product_search.models.SearchFormModel;
import edu.gyaneshm.ebay_product_search.models.SearchResultModel;
import edu.gyaneshm.ebay_product_search.listeners.DataFetchingListener;
import edu.gyaneshm.ebay_product_search.shared.Utils;

public class SearchResultActivity extends AppCompatActivity {
    private ActionBar mActionBar;

    private LinearLayout mProgressBarContainer;
    private LinearLayout mSearchResultsContainer;
    private TextView mTotalItemsTextView;
    private TextView mErrorTextView;
    private RecyclerView mSearchItemsRecyclerView;

    private SearchResultData mSearchResultData;
    ArrayList<SearchResultModel> mSearchResults = null;

    private SearchItemRecyclerViewAdapter searchItemRecyclerViewAdapter = null;

    private DataFetchingListener dataFetchingListener = new DataFetchingListener() {
        @Override
        public void dataSuccessFullyFetched() {
            initiate();
        }
    };

    private WishListChangeListener wishListChangeListener = new WishListChangeListener() {
        @Override
        public void wishListItemChanged(Integer position) {
            if (searchItemRecyclerViewAdapter != null) {
                if (position != null) {
                    searchItemRecyclerViewAdapter.notifyItemChanged(position);
                } else {
                    searchItemRecyclerViewAdapter.notifyDataSetChanged();
                }
            }
        }
    };

    private ItemClickListener itemClickListener = new ItemClickListener() {
        @Override
        public void onItemClicked(int position) {
            if (mSearchResults != null) {
                ProductData.getInstance().setItem(mSearchResults.get(position));
                Intent intent = new Intent(getApplicationContext(), ProductDetailActivity.class);
                startActivity(intent);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.search_results_activity_layout);

        mActionBar = getSupportActionBar();
        setUpActionBar();

        mProgressBarContainer = findViewById(R.id.search_results_progress_bar_container);
        mSearchResultsContainer = findViewById(R.id.search_results_container);
        mTotalItemsTextView = findViewById(R.id.search_results_total_items);
        mSearchItemsRecyclerView = findViewById(R.id.search_results_items);
        mErrorTextView = findViewById(R.id.search_results_error);

        mSearchResultData = SearchResultData.getInstance();
        mSearchResultData.registerCallback(dataFetchingListener);

        WishListData.getInstance().registerCallback(wishListChangeListener);

        initiate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSearchResultData.unregisterCallback(dataFetchingListener);
        mSearchResultData.cancelRequest();
        WishListData.getInstance().unregisterCallback(wishListChangeListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void setUpActionBar() {
        mActionBar.setTitle(R.string.search_results_title);
        mActionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void initiate() {
        mErrorTextView.setVisibility(View.GONE);
        mSearchResultsContainer.setVisibility(View.GONE);
        mProgressBarContainer.setVisibility(View.GONE);
        mSearchResults = null;

        if (mSearchResultData.isDataFetched()) {
            checkForErrors();
        } else {
            mProgressBarContainer.setVisibility(View.VISIBLE);
            mSearchResultData.fetchData();
        }
    }

    private void checkForErrors() {
        String errorMessage = mSearchResultData.getErrorMessage();
        if (errorMessage == null) {
            setUpSearchItemRecycleView();
            return;
        }

        Utils.showToast(errorMessage);
        mErrorTextView.setVisibility(View.VISIBLE);
    }

    private void setUpSearchItemRecycleView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mSearchResults = mSearchResultData.getSearchResults();
        mSearchItemsRecyclerView.setHasFixedSize(true);
        mSearchItemsRecyclerView.setLayoutManager(gridLayoutManager);

        searchItemRecyclerViewAdapter = new SearchItemRecyclerViewAdapter(this, mSearchResults, itemClickListener);
        mSearchItemsRecyclerView.setAdapter(searchItemRecyclerViewAdapter);

        SearchFormModel searchFormModel = mSearchResultData.getSearchFormData();

        String color = "#" + Integer.toHexString(getColor(R.color.search_results_total_items_text_color)).substring(2);
        String totalItems = getString(R.string.search_results_total_items, color, mSearchResults.size(), color, searchFormModel.getKeyword());
        mTotalItemsTextView.setText(Html.fromHtml(totalItems, Html.FROM_HTML_MODE_LEGACY));

        mSearchResultsContainer.setVisibility(View.VISIBLE);
    }
}
