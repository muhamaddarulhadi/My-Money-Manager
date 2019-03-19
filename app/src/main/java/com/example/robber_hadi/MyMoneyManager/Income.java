package com.example.robber_hadi.MyMoneyManager;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class Income extends AppCompatActivity {

    Button si,cin;
    TextView di,ci,ai,ni;
    Data2 data2[];
    String serverurl= "https://hardkidz.000webhostapp.com";
    ArrayList<HashMap<String, String>> datalist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.income);
        datalist = new ArrayList<>();

        Toolbar toolbar = findViewById(R.id.incometoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setTitle("Income");
        toolbar.setTitleTextColor(Color.WHITE);


        di = findViewById(R.id.dateIncome);
        di.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(Income.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        di.setText(day + "/" + month + "/" + year);
                    }}, year, month, dayOfMonth);
                datePickerDialog.show();
            }
        });

        cin = findViewById(R.id.clearincome);
        cin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                di.setText(null);
                ci.setText(null);
                ai.setText(null);
                ni.setText(null);
            }
        });


        ci = findViewById(R.id.categoryIncome);
        ci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDialog();
            }
        });

        ai = findViewById(R.id.amountIncome);
        ni = findViewById(R.id.notesIncome);

        si = findViewById(R.id.saveIncome);
        si.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try
                {
                    String Date3 = di.getText().toString();
                    String Cat3 = ci.getText().toString();
                    String Amount3 = ai.getText().toString();
                    String Notes3 = ni.getText().toString();

                    if (Date3.equals("") || Cat3.equals("") || Amount3.equals("") && Notes3.equals(""))
                    {
                        Toast.makeText(Income.this, "Please insert value", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        insertIncome(Date3,Cat3,Amount3,Notes3);
                        insertIncomeAmount(Amount3);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    private void loadDialog()
    {
        final Dialog aboutDialog = new Dialog(this, R.style.custom);
        aboutDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        aboutDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        aboutDialog.setContentView(R.layout.add_category_income);
        View v = aboutDialog.getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        aboutDialog.show();

        final ListView lv = aboutDialog.findViewById(R.id.incomeList);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String Cat4 = data2[position].Cat3;
                ci.setText(Cat4);
                aboutDialog.hide();
            }
        });


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

                    ListAdapter adapter = new CustomListIncome(
                            Income.this, datalist,
                            R.layout.custom_list_income, new String[]
                            {"Cat3"}, new int[]
                            {R.id.customListInsertIncome});
                    lv.setAdapter(adapter);
                }
            }
        }
        try {
            LoadCat lcat = new LoadCat();
            lcat.execute();
        }catch(Exception e){}


        final TextView inCat = aboutDialog.findViewById(R.id.insertIncomeCategory);
        Button addIn = aboutDialog.findViewById(R.id.addIncomeButton);
        addIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Cat3 = inCat.getText().toString();
                ci.setText(Cat3);
                insertCategory(Cat3);
                LoadCat lcat = new LoadCat();
                lcat.execute();
            }
        });
    }

    private void insertCategory(final String Cat3) {
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
                    Toast.makeText(Income.this, "Success add income category", Toast.LENGTH_LONG).show();

                }else{
                    Toast.makeText(Income.this, "Failed add income category", Toast.LENGTH_LONG).show();
                }
            }
        }
        try {
            InsertCategory icategory = new InsertCategory();
            icategory.execute();
        }catch(Exception e){}
    }


    private void insertIncome(final String Date3, final String Cat3, final String Amount3, final String Notes3) {

        class InsertIn extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("Date3",Date3);
                hashMap.put("Cat3",Cat3);
                hashMap.put("Amount3",Amount3);
                hashMap.put("Notes3",Notes3);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(serverurl +"/insertIncome.php",hashMap);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                String[] sarray = s.split("\\s*,\\s*");
                String success = sarray[0];
                if (success.equals("success")){
                    Toast.makeText(Income.this, "Success add income", Toast.LENGTH_LONG).show();

                }else{
                    Toast.makeText(Income.this, "Failed add income", Toast.LENGTH_LONG).show();
                }
            }
        }
        try {
            InsertIn insertIn = new InsertIn();
            insertIn.execute();
        }catch(Exception e){}
    }

    private void insertIncomeAmount(final String Amount3) {
        class InsertAmount extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("Amount3",Amount3);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(serverurl +"/insertIncomeAmount.php",hashMap);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                String[] sarray = s.split("\\s*,\\s*");
                String success = sarray[0];
                if (success.equals("success"))
                {


                }

                else
                {

                }
            }
        }
        try {
            InsertAmount insertAmount = new InsertAmount();
            insertAmount.execute();
        }catch(Exception e){}
    }
}
