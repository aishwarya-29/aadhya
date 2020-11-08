package com.example.aadhya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainScreen extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView bnav;
    public static Context contextOfApplication;
    public static Context getContextOfApplication()
    {
        return contextOfApplication;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen);
        bnav= findViewById(R.id.bottomNavigation);
        contextOfApplication = getApplicationContext();

        bnav.setOnNavigationItemSelectedListener(this);
        bnav.setSelectedItemId(R.id.nav_home);
    }
    Contacts contacts= new Contacts();
    Home home = new Home();
    ChatFragment chatFragment = new ChatFragment();
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,home).commit();
                return true;
            case R.id.nav_contacts:
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, contacts).commit();
                return true;
            case R.id.nav_fav:
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, chatFragment).commit();
                return true;
        }
        return false;
    }
}

