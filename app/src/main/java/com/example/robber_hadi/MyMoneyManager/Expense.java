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

public class Expense extends AppCompatActivity {

    Button se;
    TextView de,ce,ae,ne,cex;
    Data data[];
    String serverurl= "https://hardkidz.000webhostapp.com";
    ArrayList<HashMap<String, String>> datalist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expense);
        datalist = new ArrayList<>();

        Toolbar toolbar = findViewById(R.id.expensetoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setTitle("Expense");
        toolbar.setTitleTextColor(Color.WHITE);


        de = findViewById(R.id.dateExpense);
        de.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(Expense.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        de.setText(day + "/" + month + "/" + year);
                    }}, year, month, dayOfMonth);
                datePickerDialog.show();
            }
        });

        cex = findViewById(R.id.clearexpense);
        cex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                de.setText(null);
                ce.setText(null);
                ae.setText(null);
                ne.setText(null);
            }
        });

        ce = findViewById(R.id.categoryExpense);
        ce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDialog();
            }
        });

        ae = findViewById(R.id.amountExpense);

        ne = findViewById(R.id.notesExpense);

        se = findViewById(R.id.saveExpense);
        se.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try
                {
                    String Date = de.getText().toString();
                    String Cat = ce.getText().toString();
                    String Amount = ae.getText().toString();
                    String Notes = ne.getText().toString();

                    if (Date.equals("") || Cat.equals("") || Amount.equals("") && Notes.equals(""))
                    {
                        Toast.makeText(Expense.this, "Please insert value", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        insertExpense(Date,Cat,Amount,Notes);
                        insertExpenseAmount(Amount);
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
        aboutDialog.setContentView(R.layout.add_category_expense);
        View v = aboutDialog.getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        aboutDialog.show();

        final ListView lv = aboutDialog.findViewById(R.id.expenselist);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String Cat2 = data[position].Cat;
                ce.setText(Cat2);
                aboutDialog.hide();
            }
        });


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

                    ListAdapter adapter = new CustomListExpense(
                            Expense.this, datalist,
                            R.layout.custom_list_expense, new String[]
                            {"Cat"}, new int[]
                            {R.id.customListInsertExpense});
                    lv.setAdapter(adapter);
                }
            }
        }
        try {
            LoadCat lcat = new LoadCat();
            lcat.execute();
        }catch(Exception e){}


        final TextView exCat = aboutDialog.findViewById(R.id.insertExpenseCategory);
        Button addEx = aboutDialog.findViewById(R.id.addExpenseButton);
        addEx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Cat = exCat.getText().toString();
                ce.setText(Cat);
                insertCategory(Cat);
                LoadCat lcat = new LoadCat();
                lcat.execute();
            }
        });
    }

    private void insertCategory(final String Cat) {
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
                    Toast.makeText(Expense.this, "Success add expense category", Toast.LENGTH_LONG).show();

                }else{
                    Toast.makeText(Expense.this, "Failed  add expense category", Toast.LENGTH_LONG).show();
                }
            }
        }
        try {
            InsertCategory icategory = new InsertCategory();
            icategory.execute();
        }catch(Exception e){}
    }


    private void insertExpense(final String Date, final String Cat, final String Amount, final String Notes) {

        class InsertEx extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("Date",Date);
                hashMap.put("Cat",Cat);
                hashMap.put("Amount",Amount);
                hashMap.put("Notes",Notes);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(serverurl +"/insertExpense.php",hashMap);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                String[] sarray = s.split("\\s*,\\s*");
                String success = sarray[0];
                if (success.equals("success")){
                    Toast.makeText(Expense.this, "Success add expense", Toast.LENGTH_LONG).show();

                }else{
                    Toast.makeText(Expense.this, "Failed add expense", Toast.LENGTH_LONG).show();
                }
            }
        }
        try {
            InsertEx insertEx = new InsertEx();
            insertEx.execute();
        }catch(Exception e){}
    }

    private void insertExpenseAmount(final String Amount) {
        class InsertAmount extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("Amount",Amount);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(serverurl +"/insertExpenseAmount.php",hashMap);
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
            InsertAmount insertExAmount = new InsertAmount();
            insertExAmount.execute();
        }catch(Exception e){}
    }
}
