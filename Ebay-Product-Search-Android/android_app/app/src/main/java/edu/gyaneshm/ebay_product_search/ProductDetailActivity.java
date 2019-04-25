package edu.gyaneshm.ebay_product_search;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import edu.gyaneshm.ebay_product_search.adapters.ViewPageAdapter;
import edu.gyaneshm.ebay_product_search.data.AppConfig;
import edu.gyaneshm.ebay_product_search.data.ProductData;
import edu.gyaneshm.ebay_product_search.data.WishListData;
import edu.gyaneshm.ebay_product_search.fragments.DetailFragment;
import edu.gyaneshm.ebay_product_search.fragments.PhotosFragment;
import edu.gyaneshm.ebay_product_search.fragments.ShippingFragment;
import edu.gyaneshm.ebay_product_search.fragments.SimilarProductFragment;
import edu.gyaneshm.ebay_product_search.listeners.WishListChangeListener;
import edu.gyaneshm.ebay_product_search.models.ProductModel;
import edu.gyaneshm.ebay_product_search.models.SearchResultModel;
import edu.gyaneshm.ebay_product_search.shared.Utils;

public class ProductDetailActivity extends AppCompatActivity {
    private ActionBar mActionBar;
    private ViewPageAdapter mViewPageAdapter;
    private TabLayout mTabLayout;
    private FloatingActionButton mWishListFloatingActionButton;

    private ProductData mProductDataInstance;

    private SearchResultModel item;

    private WishListChangeListener wishListChangeListener = new WishListChangeListener() {
        @Override
        public void wishListItemChanged(Integer position) {
            if (item != null) {
                updateFloatingIconView();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_activity_layout);

        mProductDataInstance = ProductData.getInstance();
        item = mProductDataInstance.getItem();

        mActionBar = getSupportActionBar();
        setUpActionBar();

        ViewPager viewPager = findViewById(R.id.product_container);
        setUpViewPager();
        viewPager.setAdapter(mViewPageAdapter);
        viewPager.setOffscreenPageLimit(3);

        mTabLayout = findViewById(R.id.product_tabs);
        mTabLayout.setupWithViewPager(viewPager);
        setTabIcons();

        mWishListFloatingActionButton = findViewById(R.id.wish_list_floating_button);
        setWishListFloatingActionButton();

        WishListData.getInstance().registerCallback(wishListChangeListener);

        initiateDataFetching();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mProductDataInstance.cancelRequest();
        WishListData.getInstance().unregisterCallback(wishListChangeListener);
    }

    private void setUpActionBar() {
        mActionBar.setTitle(item.getTitle());
        mActionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.product_data_menu, menu);
        MenuItem facebookItem = menu.getItem(0);
        facebookItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_facebook:
                shareFacebookPost();
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

    private void shareFacebookPost() {
        if (!mProductDataInstance.isProductDetailFetched()) {
            Utils.showToast(R.string.error_facebook_share_data_not_fetched);
            return;
        }
        if (mProductDataInstance.getProductDetailError() != null) {
            Utils.showToast(R.string.error_facebook_share_data_not_found);
            return;
        }
        ProductModel product = mProductDataInstance.getProductDetail();
        String productUrl = product.getProductUrl();
        if (productUrl == null) {
            productUrl = "https://www.ebay.com/";
        }

        Uri.Builder builder = Uri.parse("https://www.facebook.com/dialog/share").buildUpon();
        builder.appendQueryParameter("app_id", AppConfig.getFacebookAppId());
        builder.appendQueryParameter("display", "touch");
        builder.appendQueryParameter("href", productUrl);
        builder.appendQueryParameter(
                "quote",
                getString(
                        R.string.facebook_share_message,
                        product.getTitle(),
                        Utils.formatPriceToString(product.getPrice())
                )
        );
        builder.appendQueryParameter("hashtag", getString(R.string.facebook_share_hash_tag));

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, builder.build());
        startActivity(browserIntent);
    }

    private void setWishListFloatingActionButton() {
        updateFloatingIconView();
        mWishListFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.isInWishList()) {
                    WishListData.getInstance().removeItemFromWishList(item);
                } else {
                    WishListData.getInstance().addItemToWishList(item);
                }
            }
        });
    }

    private void updateFloatingIconView() {
        if (item.isInWishList()) {
            mWishListFloatingActionButton.setImageDrawable(getDrawable(R.drawable.cart_remove));
        } else {
            mWishListFloatingActionButton.setImageDrawable(getDrawable(R.drawable.cart_add));
        }
    }
}
