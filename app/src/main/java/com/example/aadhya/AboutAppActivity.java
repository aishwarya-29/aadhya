package com.example.aadhya;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class AboutAppActivity extends AppCompatActivity {
    ListView lv;
    String howto [] = {
            "Register an account",
            "Add upto 10 emergency contacts",
            "Verify your identity by any Government approved ID Card",
            "In case of an emergency, click the 'scream' button to notify your emergency contacts."
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_app_layout);
        lv  = findViewById(R.id.howto);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,howto);
        lv.setAdapter(adapter);
    }
}
