package edu.gyaneshm.ebay_product_search.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.gyaneshm.ebay_product_search.R;
import edu.gyaneshm.ebay_product_search.shared.Utils;

public class PhotosFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.photos_fragment_layout, container, false);

        return view;
    }
}
