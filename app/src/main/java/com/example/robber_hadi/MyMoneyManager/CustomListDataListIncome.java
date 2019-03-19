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

public class CustomListDataListIncome extends SimpleAdapter {

    private Context mContext;
    public LayoutInflater inflater=null;
    public CustomListDataListIncome(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        mContext = context;
        inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.custom_list_datalist, null);
        HashMap<String, Object> data = (HashMap<String, Object>) getItem(position);
        TextView Date4 = vi.findViewById(R.id.tarikh);
        TextView Cat4 = vi.findViewById(R.id.jenis);
        TextView Amount4 = vi.findViewById(R.id.duit);
        TextView Notes4 = vi.findViewById(R.id.nota);
        String Date3 = (String) data.get("Date3");
        String Cat3 = (String) data.get("Cat3");
        String Amount3 = (String) data.get("Amount3");
        String Notes3 = (String) data.get("Notes3");
        Date4.setText(Date3);
        Cat4.setText(Cat3);
        Amount4.setText(Amount3);
        Notes4.setText(Notes3);
        return vi;
    }
}