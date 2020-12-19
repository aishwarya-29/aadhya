package com.example.aadhya;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    Button signIn, signUp, mainscreen;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION, Manifest.permission.CAMERA, Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CONTACTS, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SEND_SMS, Manifest.permission.RECORD_AUDIO}, 0);
        setContentView(R.layout.activity_main);
        signIn = findViewById(R.id.btn_signin);
        signUp = findViewById(R.id.btn_signup);
        auth = FirebaseAuth.getInstance();
        SharedPreferences shared = getSharedPreferences("shared", MODE_PRIVATE);
        if (shared.contains("email") && shared.contains("password")) {
            final String login_email = (shared.getString("email", ""));
            final String login_password = (shared.getString("password", ""));
            auth.signInWithEmailAndPassword(login_email, login_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Intent i = new Intent(MainActivity.this, MainScreen.class);
                        startActivity(i);
                        finish();
                    } else {
                    }
                }
            });
        }
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
    }


}
