package edu.gyaneshm.ebay_product_search.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import java.util.ArrayList;

public class ZipCodeAdapter extends ArrayAdapter<String> {
    private ArrayList<String> zipCodes;

    public ZipCodeAdapter(Context context, int resource, ArrayList<String> zipCodes) {
        super(context, resource, zipCodes);
        this.zipCodes = zipCodes;
    }

    @Override
    public int getCount() {
        return zipCodes.size();
    }

    @Override
    public String getItem(int position) {
        return zipCodes.get(position);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    filterResults.values = zipCodes;
                    filterResults.count = zipCodes.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && (results.count > 0)) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
    }
}
