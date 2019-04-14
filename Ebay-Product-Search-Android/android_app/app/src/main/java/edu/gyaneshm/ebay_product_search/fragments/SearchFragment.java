package edu.gyaneshm.ebay_product_search.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import edu.gyaneshm.ebay_product_search.R;
import edu.gyaneshm.ebay_product_search.data.ZipcodeSuggestionsData;

public class SearchFragment extends Fragment {
    private EditText mKeywordEditText;
    private TextView mKeywordErrorTextView;
    private Spinner mCategorySpinner;

    private CheckBox mConditionNewCheckbox;
    private CheckBox mConditionUsedCheckbox;
    private CheckBox mConditionUnspecifiedCheckbox;

    private CheckBox mShippingLocalCheckbox;
    private CheckBox mShippingFreeCheckbox;

    private CheckBox mEnableSearchLocation;
    private LinearLayout mLocationContainer;
    private EditText mMilesEditText;
    private TextView mMilesErrorTextView;
    private RadioGroup mZipcodePreferenceRadioGroup;
    private RadioButton mZipcodeRadioButton;
    private AutoCompleteTextView mZipcodeAutoCompleteTextView;
    private TextView mZipcodeErrorTextView;

    private Button mSearchButton;
    private Button mClearButton;

    private ZipcodeSuggestionsData mZipcodeSuggestionsData = null;
    private RequestQueue mRequestQueue = null;

    private ArrayAdapter<String> mZipcodeAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment_layout, container, false);

        mKeywordEditText = view.findViewById(R.id.search_keyword);
        mKeywordErrorTextView = view.findViewById(R.id.search_keyword_error);

        mCategorySpinner = view.findViewById(R.id.search_category);
        initializeCategorySpinner();

        mConditionNewCheckbox = view.findViewById(R.id.search_condition_new);
        mConditionUsedCheckbox = view.findViewById(R.id.search_condition_used);
        mConditionUnspecifiedCheckbox = view.findViewById(R.id.search_condition_unspecified);

        mShippingLocalCheckbox = view.findViewById(R.id.search_shipping_local);
        mShippingFreeCheckbox = view.findViewById(R.id.search_shipping_free);

        mEnableSearchLocation = view.findViewById(R.id.search_enable_nearby_location);
        mLocationContainer = view.findViewById(R.id.search_location_container);
        mMilesEditText = view.findViewById(R.id.search_location_miles);
        mMilesErrorTextView = view.findViewById(R.id.search_miles_error);
        mZipcodePreferenceRadioGroup = view.findViewById(R.id.search_from_radio_group);
        mZipcodeAutoCompleteTextView = view.findViewById(R.id.search_location_zipcode);
        mZipcodeErrorTextView = view.findViewById(R.id.search_zipcode_error);
        setUpLocationListeners();

        mSearchButton = view.findViewById(R.id.search_button);
        setUpSearchButton();
        mClearButton = view.findViewById(R.id.clear_button);
        setUpClearButton();

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll("zipcode");
        }
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

    private void setUpLocationListeners() {
        mEnableSearchLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEnableSearchLocation.isChecked()) {
                    mLocationContainer.setVisibility(View.VISIBLE);
                } else {
                    mLocationContainer.setVisibility(View.GONE);
                    mMilesEditText.setText("");
                    mMilesErrorTextView.setVisibility(View.GONE);
                    mZipcodePreferenceRadioGroup.check(R.id.search_radio_here);
                }
            }
        });

        mZipcodeAdapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{}
        );

        mZipcodeAutoCompleteTextView.setEnabled(false);
        mZipcodeAutoCompleteTextView.setThreshold(1);
        mZipcodeAutoCompleteTextView.setAdapter(mZipcodeAdapter);

        mZipcodeAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().matches("^\\d+$")) {
                    if (mZipcodeSuggestionsData == null) {
                        mZipcodeSuggestionsData = ZipcodeSuggestionsData.getInstance();
                    }

                    Response.Listener<JSONObject> successListener = new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            ArrayList<String> suggestions = new ArrayList<>();
                            if (response.has("postalCodes")) {
                                try {
                                    JSONArray postalCodes = response.getJSONArray("postalCodes");
                                    for (int i = 0; i < postalCodes.length(); i++) {
                                        JSONObject postalCode = postalCodes.getJSONObject(i);
                                        if (postalCode.has("postalCode")) {
                                            suggestions.add(
                                                    postalCode.getString("postalCode")
                                            );
                                        }
                                    }
                                } catch (Exception e) {
                                }
                            }
                            mZipcodeAdapter.clear();
                            mZipcodeAdapter.addAll(suggestions);
                            mZipcodeAdapter.notifyDataSetChanged();
                        }
                    };
                    Response.ErrorListener errorListener = new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    };

                    if (mRequestQueue == null) {
                        mRequestQueue = Volley.newRequestQueue(getContext());
                    }

                    mRequestQueue.add(mZipcodeSuggestionsData.fetchZipcodeSuggestions(
                            s.toString(),
                            successListener,
                            errorListener
                    ));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mZipcodePreferenceRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.search_radio_here:
                        mZipcodeAutoCompleteTextView.setText("");
                        mZipcodeAutoCompleteTextView.setEnabled(false);
                        mZipcodeErrorTextView.setVisibility(View.GONE);
                        break;
                    case R.id.search_radio_other:
                        mZipcodeAutoCompleteTextView.setEnabled(true);
                        break;
                }
            }
        });
    }

    private void setUpSearchButton() {

    }

    private void setUpClearButton() {
        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mKeywordEditText.setText("");
                mKeywordErrorTextView.setVisibility(View.GONE);

                mCategorySpinner.setSelection(0);

                mConditionNewCheckbox.setChecked(false);
                mConditionUsedCheckbox.setChecked(false);
                mConditionUnspecifiedCheckbox.setChecked(false);

                mShippingLocalCheckbox.setChecked(false);
                mShippingFreeCheckbox.setChecked(false);

                mEnableSearchLocation.setChecked(false);
                mLocationContainer.setVisibility(View.GONE);

                mMilesEditText.setText("");
                mMilesErrorTextView.setVisibility(View.GONE);

                mZipcodePreferenceRadioGroup.check(R.id.search_radio_here);
            }
        });
    }
}
