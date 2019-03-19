package com.example.robber_hadi.MyMoneyManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomListCategoriesExpense extends SimpleAdapter {

    private Context mContext;
    public LayoutInflater inflater=null;
    public CustomListCategoriesExpense(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        this.mContext = context;
        this.inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.custom_list_categories, null);
        HashMap<String, Object> data = (HashMap<String, Object>) getItem(position);
        TextView Cat2 = vi.findViewById(R.id.customListView);
        String Cat = (String) data.get("Cat");
        Cat2.setText(Cat);
        return vi;
    }
}
