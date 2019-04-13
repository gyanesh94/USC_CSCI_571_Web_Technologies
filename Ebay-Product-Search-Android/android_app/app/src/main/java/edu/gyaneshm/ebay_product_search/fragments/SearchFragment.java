package edu.gyaneshm.ebay_product_search.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import edu.gyaneshm.ebay_product_search.R;

public class SearchFragment extends Fragment {
    private EditText mKeywordEditText;
    private TextView mKeywordErrorTextView;
    private Spinner mCategorySpinner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment_layout, container, false);

        mKeywordEditText = view.findViewById(R.id.search_keyword);
        mKeywordErrorTextView = view.findViewById(R.id.search_keyword_error);

        mCategorySpinner = view.findViewById(R.id.search_category);
        initializeCategorySpinner();

        return view;
    }

    private void initializeCategorySpinner() {
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(
                getActivity(),
                R.array.search_categories,
                android.R.layout.simple_spinner_dropdown_item
        );
        mCategorySpinner.setAdapter(categoryAdapter);
        mCategorySpinner.setSelection(0);
    }
}
