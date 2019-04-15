package edu.gyaneshm.ebay_product_search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import edu.gyaneshm.ebay_product_search.adapters.ViewPageAdapter;
import edu.gyaneshm.ebay_product_search.fragments.DetailFragment;
import edu.gyaneshm.ebay_product_search.fragments.PhotosFragment;
import edu.gyaneshm.ebay_product_search.fragments.ShippingFragment;
import edu.gyaneshm.ebay_product_search.fragments.SimilarProductFragment;

public class ProductDetailActivity extends AppCompatActivity {
    private ActionBar mActionBar;
    private ViewPageAdapter mViewPageAdapter;
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_activity_layout);

        ViewPager viewPager = findViewById(R.id.product_container);
        setUpViewPager();
        viewPager.setAdapter(mViewPageAdapter);

        mTabLayout = findViewById(R.id.product_tabs);
        mTabLayout.setupWithViewPager(viewPager);
        setTabIcons();
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
}
