package com.example.robber_hadi.MyMoneyManager;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import java.util.HashMap;

public class Register extends AppCompatActivity {

    Button rr,br,cr;
    EditText fnr,ur,pr,er,pnr;
    RadioGroup ggr;
    RadioButton mr,fr;
    String serverurl = "https://hardkidz.000webhostapp.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        Toolbar toolbar = findViewById(R.id.rtoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setTitle("Register");
        toolbar.setTitleTextColor(Color.WHITE);

        cr = findViewById(R.id.clear_reg);
        fnr = findViewById(R.id.fullname_reg);
        ur = findViewById(R.id.username_reg);
        pr = findViewById(R.id.password_reg);
        er = findViewById(R.id.email_reg);
        pnr = findViewById(R.id.phone_no_reg);
        ggr = findViewById(R.id.gender_group_reg);
        mr = findViewById(R.id.male_reg);
        fr = findViewById(R.id.female_reg);
        rr = findViewById(R.id.register_reg);

        cr.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                fnr.setText(null);
                ur.setText(null);
                pr.setText(null);
                er.setText(null);
                pnr.setText(null);
                ggr.clearCheck();

            }
        });

        rr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fullname = fnr.getText().toString();
                String username = ur.getText().toString();
                String password = pr.getText().toString();
                String email = er.getText().toString();
                String phone_no = pnr.getText().toString();

                int get_gender = ggr.getCheckedRadioButtonId();
                RadioButton radioButton = findViewById(get_gender);
                String gender = radioButton.getText().toString();

                registerUser(fullname,username,password,email,phone_no,gender);
            }
        });
    }

    //register the data to the database online
    private void registerUser(final String fullname,final String username, final String password, final String email, final String phone_no, final String gender) {
        class RegisterUser extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("FULLNAME",fullname);
                hashMap.put("USERNAME",username);
                hashMap.put("PASSWORD",password);
                hashMap.put("EMAIL",email);
                hashMap.put("PHONENO",phone_no);
                hashMap.put("GENDER",gender);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(serverurl+"/register.php",hashMap);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s.equals("success")){
                    Toast.makeText(Register.this, "Success to register",
                            Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(Register.this, "Failed to register",
                            Toast.LENGTH_LONG).show();
                }
            }
        }
        try {
            RegisterUser ruser = new RegisterUser();
            ruser.execute();
        }catch(Exception e){}
    }
}
