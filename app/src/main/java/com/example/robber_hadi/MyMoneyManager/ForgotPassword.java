package com.example.robber_hadi.MyMoneyManager;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class ForgotPassword extends AppCompatActivity {

    String serverurl = "https://hardkidz.000webhostapp.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);

        Toolbar toolbar = findViewById(R.id.fptoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setTitle("Forgot Password");
        toolbar.setTitleTextColor(Color.WHITE);

        Button recover = findViewById(R.id.recoverB);
        recover.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TextView emailInsert = findViewById(R.id.emailR);
                String email = emailInsert.getText().toString();
                recoverPass(email);
            }
        });
    }

    private void recoverPass(final String email)
    {
        class RecoverPass extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("email",email);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(serverurl+"/recoverpass.php",hashMap);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s.equals("success")){
                    Toast.makeText(ForgotPassword.this, "Password sent to email",
                            Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(ForgotPassword.this, "Password not sent to email",
                            Toast.LENGTH_LONG).show();
                }
            }
        }
        try {
            RecoverPass rp = new RecoverPass();
            rp.execute();
        }catch(Exception e){}
    }
}