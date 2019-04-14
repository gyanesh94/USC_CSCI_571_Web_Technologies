package edu.gyaneshm.ebay_product_search;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class SearchResultActivity extends AppCompatActivity {
    private ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActionBar = getSupportActionBar();
        setUpActionBar();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    private void setUpActionBar() {
        mActionBar.setTitle(R.string.search_results_title);
        mActionBar.setDisplayHomeAsUpEnabled(true);
    }
}
