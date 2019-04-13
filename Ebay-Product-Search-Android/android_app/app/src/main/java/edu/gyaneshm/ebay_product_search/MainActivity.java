package edu.gyaneshm.ebay_product_search;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;

import edu.gyaneshm.ebay_product_search.adapters.ViewPageAdapter;
import edu.gyaneshm.ebay_product_search.fragments.SearchFragment;
import edu.gyaneshm.ebay_product_search.fragments.WishListFragment;

public class MainActivity extends AppCompatActivity {
    private ViewPageAdapter mViewPageAdapter;
    private ViewPager mViewPager;

    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewPager = findViewById(R.id.main_container);
        setUpViewPager();
        mViewPager.setAdapter(mViewPageAdapter);

        mTabLayout = findViewById(R.id.main_tabs);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void setUpViewPager() {
        mViewPageAdapter = new ViewPageAdapter(this, getSupportFragmentManager());
        mViewPageAdapter.addFragment(new SearchFragment(), R.string.tab_text_1);
        mViewPageAdapter.addFragment(new WishListFragment(), R.string.tab_text_2);
    }
}
