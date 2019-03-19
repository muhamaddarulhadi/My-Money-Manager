package com.example.robber_hadi.MyMoneyManager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.autofill.AutofillValue;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends AppCompatActivity {

    public static FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Toolbar toolbar = findViewById(R.id.mtoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("My Money Manager");
        toolbar.setTitleTextColor(Color.WHITE);

        TabLayout tabLayout =  findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("SPENDING"));
        tabLayout.addTab(tabLayout.newTab().setText("TRANSACTION"));
        tabLayout.addTab(tabLayout.newTab().setText("CATEGORIES"));

        final ViewPager viewPager = findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(0);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

                /*FragmentTransaction ft;

                Spending spending = ((Spending) Main.manager.findFragmentById(R.id.spending));
                Transaction transaction = ((Transaction) Main.manager.findFragmentById(R.id.transaction));
                Categories categories = ((Categories) Main.manager.findFragmentById(R.id.categories));

                Spending spending = ((Spending) Main.manager.findFragmentByTag("spending"));
                Transaction transaction = ((Transaction) Main.manager.findFragmentByTag("transaction"));
                Categories categories = ((Categories) Main.manager.findFragmentByTag("categories"));

                ft = manager.beginTransaction();

                if(spending!=null)
                {
                    ft.remove(spending);
                    ft.commit();
                }
                else if (transaction!=null)
                {
                    ft.remove(transaction);
                    ft.commit();
                }
                else if (categories!=null)
                {
                    ft.remove(categories);
                    ft.commit();
                }*/
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());
                adapter.notifyDataSetChanged();

                /*switch (tab.getPosition()) {
                    case 0:
                        Spending tab1 = new Spending();
                        tab1.onStart();
                    case 1:
                        Transaction tab2 = new Transaction();
                        tab2.onStart();
                    case 2:
                        Categories tab3 = new Categories();
                        tab3.onStart();
                }*/
            }
        });
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.item1:
                loadDialog();
                return true;
            case R.id.item2:
                new AppEula(this).show2();
                return true;
            case R.id.item3:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logout() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setMessage("Are you sure you want to logout?")
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        Toast.makeText(Main.this, "Logout Success", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Main.this, Login.class);
                        startActivity(intent);
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

    private void loadDialog()
    {
        String title = "About";
        String ab = "This application is a project that are build by Hadi for the Mobile Programming subjet. " +
                "The title for this application is My Money Manager. " +
                "This application is build on the A181 session for Universiti Utara Malaysia. " +
                "Copyright 2018. ";
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setTitle(title)
                .setMessage(ab)
                .setCancelable(false)
                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        alertDialogBuilder.setCancelable(true);
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}