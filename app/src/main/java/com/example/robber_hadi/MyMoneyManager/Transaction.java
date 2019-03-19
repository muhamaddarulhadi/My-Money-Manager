package com.example.robber_hadi.MyMoneyManager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.IntStream;


public class Transaction  extends Fragment /*implements Updateable*/ {

    DataExpense dataexpense[];
    DataIncome dataincome[];
    //Data data[];
    //Data2 data2[];
    //Status status[];
    //AmountExpense amountExpense[];
    String serverurl= "https://hardkidz.000webhostapp.com";

    ArrayList<HashMap<String, String>> datalist;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.transaction, container, false);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        loadExpense();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        datalist = new ArrayList<>();

        final TextView statu;
        Button ge,gi;

        statu = getActivity().findViewById(R.id.status);

        ge = getActivity().findViewById(R.id.getExpe);
        ge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadExpense();
                statu.setText("Expense");
            }
        });

        gi = getActivity().findViewById(R.id.getInco);
        gi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadIncome();
                statu.setText("Income");
            }
        });



        ListView lvv = getActivity().findViewById(R.id.datalistall);
        lvv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                getStatus();

                if (statu.getText().toString() == "Expense")
                {
                    String Date2 = dataexpense[position].Date;
                    String Cat2 = dataexpense[position].Cat;
                    String Amount2 = dataexpense[position].Amount;
                    String Notes2 = dataexpense[position].Notes;

                    loadDialog(Date2,Cat2,Amount2,Notes2);
                }
                else
                {
                    String Date4 = dataincome[position].Date3;
                    String Cat4 = dataincome[position].Cat3;
                    String Amount4 = dataincome[position].Amount3;
                    String Notes4 = dataincome[position].Notes3;

                    loadDialog(Date4,Cat4,Amount4,Notes4);
                }
            }
        });
    }


    public String getStatus() {
        TextView statu = getActivity().findViewById(R.id.status);
        String sta = statu.getText().toString();
        return sta;
    }


    /*public void update() {
        loadExpense();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            update();
        }
    }*/

    public void loadExpense()
    {
        TextView statu = getActivity().findViewById(R.id.status);
        statu.setText("Expense");
        final ListView lvv = getActivity().findViewById(R.id.datalistall);
        class LoadExpense extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(serverurl +"/loadExpense.php",hashMap);
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
                        dataexpense = new DataExpense[dataarray.length()];
                        for (int i = 0; i < dataarray.length(); i++) {
                            JSONObject c = dataarray.getJSONObject(i);
                            String Date2 = c.getString("Date");
                            String Cat2 = c.getString("Cat");
                            String Amount2 = c.getString("Amount");
                            String Notes2 = c.getString("Notes");

                            dataexpense[i] = new DataExpense(Date2,Cat2,Amount2,Notes2);
                            HashMap<String, String> datals = new HashMap<>();
                            datals.put("Date",Date2);
                            datals.put("Cat",Cat2);
                            datals.put("Amount",Amount2);
                            datals.put("Notes",Notes2);
                            datalist.add(datals);
                        }
                    }catch (final JSONException e) {}

                    ListAdapter adapter = new CustomListDataListExpense(
                            Transaction.this.getActivity(), datalist,
                            R.layout.custom_list_datalist, new String[]
                            {"Date","Cat","Amount","Notes"}, new int[]
                            {R.id.tarikh,R.id.jenis,R.id.duit,R.id.nota});
                    lvv.setAdapter(adapter);
                }
            }
        }
        try {
            LoadExpense lexpens = new LoadExpense();
            lexpens.execute();
        }catch(Exception e){}
    }

    public void loadIncome()
    {
        TextView statu = getActivity().findViewById(R.id.status);
        statu.setText("Income");
        final ListView lvv = getActivity().findViewById(R.id.datalistall);
        class LoadIncome extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(serverurl +"/loadIncome.php",hashMap);
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
                        dataincome = new DataIncome[dataarray.length()];
                        for (int i = 0; i < dataarray.length(); i++) {
                            JSONObject c = dataarray.getJSONObject(i);
                            String Cat4 = c.getString("Cat3");
                            String Date4 = c.getString("Date3");
                            String Amount4 = c.getString("Amount3");
                            String Notes4 = c.getString("Notes3");

                            dataincome[i] = new DataIncome(Cat4,Date4,Amount4,Notes4);
                            HashMap<String, String> datals = new HashMap<>();
                            datals.put("Cat3",Cat4);
                            datals.put("Date3",Date4);
                            datals.put("Amount3",Amount4);
                            datals.put("Notes3",Notes4);
                            datalist.add(datals);
                        }
                    }catch (final JSONException e) {}

                    ListAdapter adapter = new CustomListDataListIncome(
                            Transaction.this.getActivity(), datalist,
                            R.layout.custom_list_datalist, new String[]
                            {"Date3","Cat3","Amount3","Notes3"}, new int[]
                            {R.id.tarikh,R.id.jenis,R.id.duit,R.id.nota});
                    lvv.setAdapter(adapter);
                }
            }
        }
        try {
            LoadIncome lincom = new LoadIncome();
            lincom.execute();
        }catch(Exception e){}
    }


    private void loadDialog(final String Dateee, final String Cattt, final String Amounttt, final String Notesss)
    {
        final Dialog aboutDialog = new Dialog(this.getActivity(), R.style.custom);
        aboutDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        aboutDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        aboutDialog.getWindow().setGravity(0);
        aboutDialog.setContentView(R.layout.delete_expense_income_dialog);
        View v = aboutDialog.getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        aboutDialog.show();

        final TextView Datee = aboutDialog.findViewById(R.id.dateud);
        final TextView Catt = aboutDialog.findViewById(R.id.catud);
        final TextView Amountt = aboutDialog.findViewById(R.id.amountud);
        final TextView Notess = aboutDialog.findViewById(R.id.notesud);
        final RadioButton btnIncomeRad = aboutDialog.findViewById(R.id.incomeRadButt);
        final RadioButton btnExpenseRad = aboutDialog.findViewById(R.id.expenseRadButt);

        Datee.setText(Dateee);
        Catt.setText(Cattt);
        Amountt.setText(Amounttt);
        Notess.setText(Notesss);

        //final String da = Datee.getText().toString();
        //final String ca = Catt.getText().toString();
        //final String am = Amountt.getText().toString();
        //final String no = Notess.getText().toString();

        /*Datee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(Transaction.this.getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        Datee.setText(day + "/" + month + "/" + year);
                    }}, year, month, dayOfMonth);
                datePickerDialog.show();
            }
        });*/


        /*Catt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView statu = getActivity().findViewById(R.id.status);
                if (statu.getText().toString() == "Expense")
                {
                    final Dialog aboutDialog = new Dialog(Transaction.this.getActivity(), R.style.Theme_AppCompat_Light_Dialog_Alert);
                    aboutDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    aboutDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                    aboutDialog.setContentView(R.layout.add_category_expense);
                    aboutDialog.show();

                    final ListView lvin = aboutDialog.findViewById(R.id.expenselist);
                    lvin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String Cat2 = data[position].Cat;
                            Catt.setText(Cat2);
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
                                        Transaction.this.getActivity(), datalist,
                                        R.layout.custom_list_expense, new String[]
                                        {"Cat"}, new int[]
                                        {R.id.customListInsertExpense});
                                lvin.setAdapter(adapter);
                            }
                        }
                    }
                    try {
                        LoadCat lcat = new LoadCat();
                        lcat.execute();
                    }catch(Exception e){}


                    final TextView inCat = aboutDialog.findViewById(R.id.insertExpenseCategory);
                    Button addIn = aboutDialog.findViewById(R.id.addExpenseButton);
                    addIn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String Cat = inCat.getText().toString();
                            Catt.setText(Cat);
                            insertCategoryEx(Cat);
                            LoadCat lcat = new LoadCat();
                            lcat.execute();
                        }
                    });
                }
                else
                {
                    final Dialog aboutDialog = new Dialog(Transaction.this.getActivity(), R.style.Theme_AppCompat_Light_Dialog_Alert);
                    aboutDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    aboutDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                    aboutDialog.setContentView(R.layout.add_category_income);
                    aboutDialog.show();

                    final ListView lvin = aboutDialog.findViewById(R.id.incomeList);
                    lvin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String Cat4 = data2[position].Cat3;
                            Catt.setText(Cat4);
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
                                        Transaction.this.getActivity(), datalist,
                                        R.layout.custom_list_income, new String[]
                                        {"Cat3"}, new int[]
                                        {R.id.customListInsertIncome});
                                lvin.setAdapter(adapter);
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
                            Catt.setText(Cat3);
                            insertCategoryIn(Cat3);
                            LoadCat lcat = new LoadCat();
                            lcat.execute();
                        }
                    });
                }
            }
        });*/


        final Button btndelete = aboutDialog.findViewById(R.id.delud);
        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(btnExpenseRad.isChecked())
                {
                    deleteDialogEx(Dateee,Cattt,Amounttt,Notesss);
                }
                else if(btnIncomeRad.isChecked())
                {
                    deleteDialogIn(Dateee,Cattt,Amounttt,Notesss);
                }
            }
        });

        /*final Button btnupdate = aboutDialog.findViewById(R.id.upud);
        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnExpenseRad.isChecked())
                {
                    //updateDialogEx(Dateee,Cattt,Amounttt,Notesss);
                    updateDialogEx(da,ca,am,no);
                }
                else if(btnIncomeRad.isChecked())
                {
                    updateDialogIn(da,ca,am,no);
                }
            }
        });*/
    }

    /*private void insertCategoryEx(final String Cat) {
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
                    Toast.makeText(Transaction.this.getActivity(), "Success", Toast.LENGTH_LONG).show();

                }else{
                    Toast.makeText(Transaction.this.getActivity(), "Failed", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(Transaction.this.getActivity(), "Success", Toast.LENGTH_LONG).show();

                }else{
                    Toast.makeText(Transaction.this.getActivity(), "Failed", Toast.LENGTH_LONG).show();
                }
            }
        }
        try {
            InsertCategory icategory = new InsertCategory();
            icategory.execute();
        }catch(Exception e){}
    }*/


    private void deleteDialogEx(final String Dateee, final String Cattt, final String Amounttt, final String Notesss) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getActivity());
        alertDialogBuilder
                .setMessage("Are you sure you want to delete?")
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        deleteExpense(Dateee,Cattt,Amounttt,Notesss);
                        deleteExpenseAmount(Amounttt);
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

    private void deleteExpense (final String Date, final String Cat, final String Amount, final String Notes) {
        class DeleteEx extends AsyncTask<Void,Void,String> {
            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("Date",Date);
                hashMap.put("Cat",Cat);
                hashMap.put("Amount",Amount);
                hashMap.put("Notes",Notes);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(serverurl + "/deleteexpense.php",hashMap);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s.equals("success")){
                    Toast.makeText(Transaction.this.getActivity(), "Delete Success",
                            Toast.LENGTH_LONG).show();
                    loadExpense();
                }else{
                    Toast.makeText(Transaction.this.getActivity(), "Delete Failed",
                            Toast.LENGTH_LONG).show();
                }
            }
        }
        try {
            DeleteEx dex = new DeleteEx();
            dex.execute();
        }catch(Exception e){}
    }

    private void deleteExpenseAmount (final String Amount) {
        class DeleteExAm extends AsyncTask<Void,Void,String> {
            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("Amount",Amount);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(serverurl + "/deleteExpenseAmount.php",hashMap);
                return s;
            }
        }
        try {
            DeleteExAm dexam = new DeleteExAm();
            dexam.execute();
        }catch(Exception e){}
    }


    private void deleteDialogIn(final String Dateee, final String Cattt, final String Amounttt, final String Notesss) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getActivity());
        alertDialogBuilder
                .setMessage("Are you sure you want to delete?")
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        deleteIncome(Dateee,Cattt,Amounttt,Notesss);
                        deleteIncomeAmount(Amounttt);
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

    private void deleteIncome (final String Date3, final String Cat3, final String Amount3, final String Notes3) {
        class DeleteIn extends AsyncTask<Void,Void,String> {
            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("Date3",Date3);
                hashMap.put("Cat3",Cat3);
                hashMap.put("Amount3",Amount3);
                hashMap.put("Notes3",Notes3);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(serverurl + "/deleteincome.php",hashMap);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s.equals("success")){
                    Toast.makeText(Transaction.this.getActivity(), "Delete Success",
                            Toast.LENGTH_LONG).show();
                    loadIncome();
                }else{
                    Toast.makeText(Transaction.this.getActivity(), "Delete Failed",
                            Toast.LENGTH_LONG).show();
                }
            }
        }
        try {
            DeleteIn din = new DeleteIn ();
            din.execute();
        }catch(Exception e){}
    }

    private void deleteIncomeAmount (final String Amount3) {
        class DeleteInAm extends AsyncTask<Void,Void,String> {
            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("Amount3",Amount3);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(serverurl + "/deleteIncomeAmount.php",hashMap);
                return s;
            }
        }
        try {
            DeleteInAm dinam = new DeleteInAm();
            dinam.execute();
        }catch(Exception e){}
    }

    /*private void updateDialogEx(final String Dateee, final String Cattt, final String Amounttt, final String Notesss) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getActivity());
        alertDialogBuilder
                .setMessage("Are you sure you want to update?")
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        updateExpense(Dateee,Cattt,Amounttt,Notesss);
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


    private void updateExpense(final String Date, final String Cat, final String Amount, final String Notes) {
        class Update extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("Date",Date);
                hashMap.put("Cat",Cat);
                hashMap.put("Amount",Amount);
                hashMap.put("Notes",Notes);

                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(serverurl +"/updateExpense.php",hashMap);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s.equals("success")){
                    Toast.makeText(Transaction.this.getActivity(), "Update success",
                            Toast.LENGTH_LONG).show();
                    loadExpense();
                }else{
                    Toast.makeText(Transaction.this.getActivity(), "Update Failed",
                            Toast.LENGTH_LONG).show();
                }
            }
        }
        try {
            Update upe = new Update();
            upe.execute();
        }catch(Exception e){}
    }


    private void updateDialogIn(final String Dateee, final String Cattt, final String Amounttt, final String Notesss) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getActivity());
        alertDialogBuilder
                .setMessage("Are you sure you want to update?")
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        updateIncome(Dateee,Cattt,Amounttt,Notesss);
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

    private void updateIncome(final String Date3, final String Cat3, final String Amount3, final String Notes3) {
        class Update extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("Date3",Date3);
                hashMap.put("Cat3",Cat3);
                hashMap.put("Amount3",Amount3);
                hashMap.put("Notes3",Notes3);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(serverurl +"/updateIncome.php",hashMap);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s.equals("success")){
                    Toast.makeText(Transaction.this.getActivity(), "Update success",
                            Toast.LENGTH_LONG).show();
                    loadIncome();
                }else{
                    Toast.makeText(Transaction.this.getActivity(), "Update Failed",
                            Toast.LENGTH_LONG).show();
                }
            }
        }
        try {
            Update upe = new Update();
            upe.execute();
        }catch(Exception e){}
    }*/


    /*public void loadAmountExpense()
    {
        final TextView expensetotal = getActivity().findViewById(R.id.ExpenseTotal);
        class LoadExpense extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(serverurl +"/loadAmount.php",hashMap);
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
                        amountExpense = new AmountExpense[dataarray.length()];
                        for (int i = 0; i < dataarray.length(); i++) {
                            JSONObject c = dataarray.getJSONObject(i);
                            String Amount2 = c.getString("Amount");

                            amountExpense[i] = new AmountExpense(Amount2);
                            HashMap<String, String> datals = new HashMap<>();
                            datals.put("Amount",Amount2);
                            datalist.add(datals);

                            Double a = Double.parseDouble(Amount2);
                            double total = 0;
                            total += a;
                            a++;
                            String to = String.valueOf(total);
                            expensetotal.setText(to);
                        }
                    }catch (final JSONException e) {}

                }
            }
        }
        try {
            LoadExpense lexpens = new LoadExpense();
            lexpens.execute();
        }catch(Exception e){}
    }*/
}
