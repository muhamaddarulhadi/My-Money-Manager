package com.example.robber_hadi.MyMoneyManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class Login extends AppCompatActivity {

    TextView fb;
    Button sil,sul;
    EditText ul,pl;
    //CheckBox r;

    private CheckBox saveLoginCheckBox;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;

    String serverurl = "https://hardkidz.000webhostapp.com";

    //public static final String MyPREFERENCES = "Prefs" ;
    //public static final String Name = "nameKey";
    //public static final String Password = "passwordKey";

    //SharedPreferences sharedpreferences;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        //sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        new AppEula(this).show();

        sil = findViewById(R.id.sign_in_log);
        sul = findViewById(R.id.sign_up_log);
        ul = findViewById(R.id.username_log);
        pl = findViewById(R.id.password_log);
        fb = findViewById(R.id.forgotButton);
        //r = findViewById(R.id.remember);

        //fb.setPaintFlags(fb.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        pl.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:

                            String username = ul.getText().toString();
                            String password = pl.getText().toString();

                            loginUser(username, password);

                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        fb.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i2 = new Intent(Login.this, ForgotPassword.class);
                startActivity(i2);
            }
        });

        sul.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i1 = new Intent(Login.this, Register.class);
                startActivity(i1);
            }
        });

        sil.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String username = ul.getText().toString();
                String password = pl.getText().toString();

                loginUser(username, password);


                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(ul.getWindowToken(), 0);

                if (saveLoginCheckBox.isChecked()) {
                    loginPrefsEditor.putBoolean("saveLogin", true);
                    loginPrefsEditor.putString("username", username);
                    loginPrefsEditor.putString("password", password);
                    loginPrefsEditor.commit();
                } else {
                    loginPrefsEditor.clear();
                    loginPrefsEditor.commit();
                }

                /*if (r.isSelected())
                {
                    String n  = username;
                    String p  = password;

                    SharedPreferences.Editor editor = sharedpreferences.edit();

                    editor.putString(Name, n);
                    editor.putString(Password, p);
                    editor.commit();
                }*/
            }
        });

        saveLoginCheckBox = findViewById(R.id.remember);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            ul.setText(loginPreferences.getString("username", ""));
            pl.setText(loginPreferences.getString("password", ""));
            saveLoginCheckBox.setChecked(true);
        }

        /*if (sharedpreferences.contains(Name)) {
            ul.setText(sharedpreferences.getString(Name, ""));
        }

        if (sharedpreferences.contains(Password)) {
            pl.setText(sharedpreferences.getString(Password, ""));
        }*/
    }

    //get data from the database online from table register
    private void loginUser(final String username, final String password) {
        class LoginUser extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("USERNAME",username);
                hashMap.put("PASSWORD",password);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(serverurl+"/login.php",hashMap);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s.equals("success")){
                    Toast.makeText(Login.this, "Login Success",
                            Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Login.this, Main.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(Login.this, "Login Failed",
                            Toast.LENGTH_LONG).show();
                }
            }
        }
        try {
            LoginUser luser = new LoginUser();
            luser.execute();
        }catch(Exception e){}
    }
}
