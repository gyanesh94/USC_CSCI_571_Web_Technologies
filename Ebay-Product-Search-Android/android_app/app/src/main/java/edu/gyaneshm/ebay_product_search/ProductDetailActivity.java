package edu.gyaneshm.ebay_product_search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import edu.gyaneshm.ebay_product_search.adapters.ViewPageAdapter;
import edu.gyaneshm.ebay_product_search.data.ProductData;
import edu.gyaneshm.ebay_product_search.fragments.DetailFragment;
import edu.gyaneshm.ebay_product_search.fragments.PhotosFragment;
import edu.gyaneshm.ebay_product_search.fragments.ShippingFragment;
import edu.gyaneshm.ebay_product_search.fragments.SimilarProductFragment;
import edu.gyaneshm.ebay_product_search.models.SearchResultModel;
import edu.gyaneshm.ebay_product_search.shared.Utils;

public class ProductDetailActivity extends AppCompatActivity {
    private ActionBar mActionBar;
    private ViewPageAdapter mViewPageAdapter;
    private TabLayout mTabLayout;

    private ProductData mProductDataInstance;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_activity_layout);

        mProductDataInstance = ProductData.getInstance();

        mActionBar = getSupportActionBar();
        setUpActionBar();

        ViewPager viewPager = findViewById(R.id.product_container);
        setUpViewPager();
        viewPager.setAdapter(mViewPageAdapter);

        mTabLayout = findViewById(R.id.product_tabs);
        mTabLayout.setupWithViewPager(viewPager);
        setTabIcons();

        initiateDataFetching();
    }

    private void setUpActionBar() {
        SearchResultModel item = mProductDataInstance.getItem();
        mActionBar.setTitle(Utils.truncateString(item.getTitle(), 25));
        mActionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.product_data_menu, menu);
        return super.onCreateOptionsMenu(menu);
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

    private void setUpViewPager() {
        mViewPageAdapter = new ViewPageAdapter(this, getSupportFragmentManager());
        mViewPageAdapter.addFragment(new DetailFragment(), R.string.product_tab_title_product);
        mViewPageAdapter.addFragment(new ShippingFragment(), R.string.product_tab_title_shipping);
        mViewPageAdapter.addFragment(new PhotosFragment(), R.string.product_tab_title_photos);
        mViewPageAdapter.addFragment(new SimilarProductFragment(), R.string.product_tab_title_similar);
    }

    private void setTabIcons() {
        int[] icons = new int[]{
                R.drawable.information_variant_selector,
                R.drawable.truck_delivery_selector,
                R.drawable.google_selector,
                R.drawable.equal_selector
        };

        for (int i = 0; i < icons.length; i++) {
            mTabLayout.getTabAt(i).setIcon(icons[i]);
        }
    }

    private void initiateDataFetching() {
        if (!mProductDataInstance.isProductDetailFetched()) {
            mProductDataInstance.fetchProductDetail();
        }

        if (!mProductDataInstance.isGooglePhotosFetched()) {
            mProductDataInstance.fetchGooglePhotos();
        }

        if (!mProductDataInstance.isSimilarItemsFetched()) {
            mProductDataInstance.fetchSimilarItems();
        }
    }
}
