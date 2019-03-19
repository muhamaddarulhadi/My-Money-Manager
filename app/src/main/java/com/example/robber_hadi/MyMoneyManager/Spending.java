package com.example.robber_hadi.MyMoneyManager;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static android.os.Build.ID;
import static android.provider.Telephony.Mms.Part.TEXT;


/*interface Updateable {
    void update();
}*/

public class Spending extends Fragment /*implements Updateable*/ {

    String serverurl= "https://hardkidz.000webhostapp.com";

    spendData spenddata[];
    ExpenseAmount expenseAmount[];
    IncomeAmount incomeAmount[];

    ArrayList<HashMap<String, String>> datalist;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.spending, container, false);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        totalIncome();
        totalExpense();
        loadSpend();
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.refresh, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.refresh:
                update();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        datalist = new ArrayList<>();

        Button ex = getActivity().findViewById(R.id.expenseSpend);
        ex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Spending.this.getActivity(), Expense.class);
                startActivity(i);
            }
        });

        Button in = getActivity().findViewById(R.id.incomeSpend);
        in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Spending.this.getActivity(), Income.class);
                startActivity(i);
            }
        });

        Button bal = getActivity().findViewById(R.id.getBal);
        bal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Balance();
            }
        });
    }


    public void Balance()
    {
        try
        {
            TextView et = getActivity().findViewById(R.id.ExTotal);
            TextView it = getActivity().findViewById(R.id.InTotal);
            TextView ba = getActivity().findViewById(R.id.balanceT);

            String e = et.getText().toString();
            String i = it.getText().toString();

            Double ext = Double.parseDouble(e);
            Double into = Double.parseDouble(i);

            Double balance = into - ext;
            String bal = String.format("%.2f", balance);
            ba.setText("RM "+bal);
        }
        catch (NumberFormatException e)
        {
            e.printStackTrace();
        }
    }


    public void loadSpend()
    {
        final ListView sl = getActivity().findViewById(R.id.spendList);
        class LoadSpend extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(serverurl +"/loadSpend.php",hashMap);
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
                        spenddata = new spendData[dataarray.length()];
                        for (int i = 0; i < dataarray.length(); i++) {
                            JSONObject c = dataarray.getJSONObject(i);
                            String Cat2 = c.getString("Cat");
                            String Amount2 = c.getString("Amount");

                            spenddata[i] = new spendData(Cat2,Amount2);
                            HashMap<String, String> datals = new HashMap<>();
                            datals.put("Cat",Cat2);
                            datals.put("Amount",Amount2);
                            datalist.add(datals);
                        }
                    }catch (final JSONException e) {}

                    ListAdapter adapter = new CustomListSpendingExpense(
                            Spending.this.getActivity(), datalist,
                            R.layout.custom_list_spending_expense, new String[]
                            {"Cat","Amount"}, new int[]
                            {R.id.catego,R.id.amou});
                    sl.setAdapter(adapter);
                }
            }
        }
        try {
            LoadSpend loadSpends = new LoadSpend();
            loadSpends.execute();
        }catch(Exception e){}
    }

    public void totalExpense()
    {
        final TextView et = getActivity().findViewById(R.id.ExTotal);
        class totalEx extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(serverurl +"/totalexpense.php",hashMap);
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
                        expenseAmount = new ExpenseAmount[dataarray.length()];
                        for (int i = 0; i < dataarray.length(); i++) {
                            JSONObject c = dataarray.getJSONObject(i);
                            String Amount2 = c.getString("Amount");
                            expenseAmount[i] = new ExpenseAmount(Amount2);
                            HashMap<String, String> datals = new HashMap<>();
                            datals.put("Amount",Amount2);
                            datalist.add(datals);

                            String expen = expenseAmount[i].Amount;
                            et.setText(expen);

                        }
                    }catch (final JSONException e) {}
                }
            }
        }
        try {
            totalEx tot = new totalEx();
            tot.execute();
        }catch(Exception e){}
    }

    public void totalIncome()
    {
        final TextView et = getActivity().findViewById(R.id.InTotal);
        class totalIn extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(serverurl +"/totalincome.php",hashMap);
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
                        incomeAmount = new IncomeAmount[dataarray.length()];
                        for (int i = 0; i < dataarray.length(); i++) {
                            JSONObject c = dataarray.getJSONObject(i);
                            String Amount4 = c.getString("Amount3");
                            incomeAmount[i] = new IncomeAmount(Amount4);
                            HashMap<String, String> datals = new HashMap<>();
                            datals.put("Amount3",Amount4);
                            datalist.add(datals);

                            String incom = incomeAmount[i].Amount3;
                            et.setText(incom);
                        }
                    }catch (final JSONException e) {}
                }
            }
        }
        try {
            totalIn tot = new totalIn();
            tot.execute();
        }catch(Exception e){}
    }

    public void update()
    {
        totalIncome();
        totalExpense();
        loadSpend();
        Toast.makeText(Spending.this.getActivity(), "Success refresh value", Toast.LENGTH_SHORT).show();
    }

    /*@Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            update();
        }
    }*/
}
