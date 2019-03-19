package com.example.robber_hadi.MyMoneyManager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class Categories extends Fragment /*implements Updateable*/ {

    Data data[];
    Data2 data2[];
    String serverurl= "https://hardkidz.000webhostapp.com";
    ArrayList<HashMap<String, String>> datalist;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.categories, container, false);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        final TextView st = getActivity().findViewById(R.id.showStat);
        loadCat();
        st.setText("Expense");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        datalist = new ArrayList<>();

        final TextView st = getActivity().findViewById(R.id.showStat);

        Button ExBut = getActivity().findViewById(R.id.getExpenseBut);
        ExBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadCat();
                st.setText("Expense");

            }
        });

        Button InBut = getActivity().findViewById(R.id.getIncomeBut);
        InBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadCat2();
                st.setText("Income");
            }
        });

        FloatingActionButton flo = getActivity().findViewById(R.id.floatBut);
        flo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDialog();
            }
        });

        Button delCat = getActivity().findViewById(R.id.delButCat);
        delCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDialogCatDel();
            }
        });
    }



    /*public void update() {
        final TextView st = getActivity().findViewById(R.id.showStat);
        loadCat();
        st.setText("Expense");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            update();
        }
    }*/

    public void loadCat()
    {
        final ListView lv = getActivity().findViewById(R.id.listDataboth);
        class LoadCat extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(serverurl +"/loadCat.php",hashMap);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                datalist.clear();
                if (s != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(s);
                        JSONArray dataarray = jsonObj.getJSONArray("data");
                        data = new Data[dataarray.length()];
                        for (int i = 0; i < dataarray.length(); i++) {
                            JSONObject c = dataarray.getJSONObject(i);
                            String Cat2 = c.getString("Cat");
                            data[i] = new Data(Cat2);
                            HashMap<String, String> datals = new HashMap<>();
                            datals.put("Cat",Cat2);
                            datalist.add(datals);
                        }
                    }catch (final JSONException e) {}

                    ListAdapter adapter = new CustomListCategoriesExpense(
                            Categories.this.getActivity(), datalist,
                            R.layout.custom_list_categories, new String[]
                            {"Cat"}, new int[]
                            {R.id.customListView});
                    lv.setAdapter(adapter);
                }
            }
        }
        try {
            LoadCat lcat = new LoadCat();
            lcat.execute();
        }catch(Exception e){}
    }


    public void loadCat2()
    {
        final ListView lv = getActivity().findViewById(R.id.listDataboth);
        class LoadCat extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(serverurl +"/loadCat2.php",hashMap);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                datalist.clear();
                if (s != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(s);
                        JSONArray dataarray = jsonObj.getJSONArray("data2");
                        data2 = new Data2[dataarray.length()];
                        for (int i = 0; i < dataarray.length(); i++) {
                            JSONObject c = dataarray.getJSONObject(i);
                            String Cat4 = c.getString("Cat3");
                            data2[i] = new Data2(Cat4);
                            HashMap<String, String> datals = new HashMap<>();
                            datals.put("Cat3",Cat4);
                            datalist.add(datals);
                        }
                    }catch (final JSONException e) {}

                    ListAdapter adapter = new CustomListCategoriesIncome(
                            Categories.this.getActivity(), datalist,
                            R.layout.custom_list_categories, new String[]
                            {"Cat3"}, new int[]
                            {R.id.customListView});
                    lv.setAdapter(adapter);
                }
            }
        }
        try {
            LoadCat lcat = new LoadCat();
            lcat.execute();
        }catch(Exception e){}
    }

    private void loadDialog()
    {
        final Dialog aboutDialog = new Dialog(this.getActivity(), R.style.custom);
        aboutDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        aboutDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        aboutDialog.getWindow().setGravity(0);
        aboutDialog.setContentView(R.layout.add_new_category_dialog);
        View v = aboutDialog.getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        aboutDialog.show();

        final TextView tv = aboutDialog.findViewById(R.id.categoryName);
        final ListView lv = getActivity().findViewById(R.id.listDataboth);
        Button btnadd = aboutDialog.findViewById(R.id.addButt);
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RadioButton Ex = aboutDialog.findViewById(R.id.radButEX);
                RadioButton In = aboutDialog.findViewById(R.id.radButIn);

                if (Ex.isChecked())
                {
                    String Cat = tv.getText().toString();
                    insertCategoryEx(Cat);
                    loadCat();
                }
                else if (In.isChecked())
                {
                    String Cat3 = tv.getText().toString();
                    insertCategoryIn(Cat3);
                    loadCat2();
                }
            }
        });
    }


    private void insertCategoryEx(final String Cat) {
        class InsertCategory extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("Cat",Cat);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(serverurl +"/insertCat.php",hashMap);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                String[] sarray = s.split("\\s*,\\s*");
                String success = sarray[0];
                if (success.equals("success")){
                    Toast.makeText(Categories.this.getActivity(), "Success add expense category", Toast.LENGTH_LONG).show();

                }else{
                    Toast.makeText(Categories.this.getActivity(), "Failed  add expense category", Toast.LENGTH_LONG).show();
                }
            }
        }
        try {
            InsertCategory icategory = new InsertCategory();
            icategory.execute();
        }catch(Exception e){}
    }


    private void insertCategoryIn(final String Cat3) {
        class InsertCategory extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("Cat3",Cat3);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(serverurl +"/insertCat2.php",hashMap);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                String[] sarray = s.split("\\s*,\\s*");
                String success = sarray[0];
                if (success.equals("success")){
                    Toast.makeText(Categories.this.getActivity(), "Success add income category", Toast.LENGTH_LONG).show();

                }else{
                    Toast.makeText(Categories.this.getActivity(), "Failed add income category", Toast.LENGTH_LONG).show();
                }
            }
        }
        try {
            InsertCategory icategory = new InsertCategory();
            icategory.execute();
        }catch(Exception e){}
    }


    private void deleteCatDialog(final String Cat) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getActivity());
        alertDialogBuilder.setTitle("Delete category "+Cat+"?");
        alertDialogBuilder
                .setMessage("Are you sure?")
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        deleteCat(Cat);
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void deleteCat(final String Cat) {
        class DeleteCat extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("Cat",Cat);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(serverurl + "/deleteCat.php",hashMap);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s.equals("success")){
                    Toast.makeText(Categories.this.getActivity(), "Delete Success",
                            Toast.LENGTH_LONG).show();
                    loadCat();
                }else{
                    Toast.makeText(Categories.this.getActivity(), "Delete Failed",
                            Toast.LENGTH_LONG).show();
                }
            }
        }
        try {
            DeleteCat dcat = new DeleteCat();
            dcat.execute();
        }catch(Exception e){}
    }


    private void deleteCat2Dialog(final String Cat3) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getActivity());
        alertDialogBuilder.setTitle("Delete category "+Cat3+"?");
        alertDialogBuilder
                .setMessage("Are you sure?")
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        deleteCat2(Cat3);
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void deleteCat2(final String Cat3) {
        class DeleteCat2 extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("Cat3",Cat3);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(serverurl + "/deleteCat2.php",hashMap);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s.equals("success")){
                    Toast.makeText(Categories.this.getActivity(), "Delete Success",
                            Toast.LENGTH_LONG).show();
                    loadCat2();
                }else{
                    Toast.makeText(Categories.this.getActivity(), "Delete Failed",
                            Toast.LENGTH_LONG).show();
                }
            }
        }
        try {
            DeleteCat2 dcat2 = new DeleteCat2();
            dcat2.execute();
        }catch(Exception e){}
    }


    private void loadDialogCatDel()
    {
        final Dialog aboutDialog = new Dialog(this.getActivity(), R.style.custom);
        aboutDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        aboutDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        aboutDialog.getWindow().setGravity(0);
        aboutDialog.setContentView(R.layout.delete_category_dialog);
        View v = aboutDialog.getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        aboutDialog.show();

        final TextView tv1 = aboutDialog.findViewById(R.id.categoryNameDel);
        Button btndel = aboutDialog.findViewById(R.id.delButt);
        btndel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RadioButton Ex = aboutDialog.findViewById(R.id.radButExp);
                RadioButton In = aboutDialog.findViewById(R.id.radButInc);

                if (Ex.isChecked())
                {
                    String Cat = tv1.getText().toString();
                    deleteCatDialog(Cat);
                    loadCat();
                }
                else if (In.isChecked())
                {
                    String Cat3 = tv1.getText().toString();
                    deleteCat2Dialog(Cat3);
                    loadCat2();
                }
            }
        });
    }
}
