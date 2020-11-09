package com.example.aadhya;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    Button signIn, signUp, mainscreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signIn = findViewById(R.id.btn_signin);
        signUp = findViewById(R.id.btn_signup);
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        signIn.setVisibility(View.VISIBLE);
                        signUp.setVisibility(View.VISIBLE);
                    }
                },
                3000);

        mainscreen = findViewById(R.id.mainscreen);
        mainscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, MainScreen.class);
                startActivity(i);
            }
        });
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
    }

    public void signUp(View view) {
        Intent click = new Intent(MainActivity.this, signUp.class);
        startActivity(click);
        finish();
    }


}

