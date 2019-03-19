package com.example.robber_hadi.MyMoneyManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomListExpense extends SimpleAdapter {

    private Context mContext;
    public LayoutInflater inflater=null;
    public CustomListExpense(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        mContext = context;
        inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.custom_list_expense, null);
        HashMap<String, Object> data = (HashMap<String, Object>) getItem(position);
        TextView Cat2 = vi.findViewById(R.id.customListInsertExpense);
        String Cat = (String) data.get("Cat");
        Cat2.setText(Cat);
        return vi;
    }
}
