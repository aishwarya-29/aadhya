package com.example.aadhya;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    Button signIn,signUp, mainscreen;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signIn = findViewById(R.id.btn_signin);
        signUp = findViewById(R.id.btn_signup);
        auth = FirebaseAuth.getInstance();
        SharedPreferences shared = getSharedPreferences("shared", MODE_PRIVATE);
        if(shared.contains("email") && shared.contains("password")) {
            final String login_email = (shared.getString("email",""));
            final String login_password = (shared.getString("password",""));
            auth.signInWithEmailAndPassword(login_email, login_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
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
                Intent i= new Intent(MainActivity.this, MainScreen.class);
                startActivity(i);
            }
        });
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
    }
    public void signUp(View view)
    {
        Intent click= new Intent(MainActivity.this, signUp.class);
        startActivity(click);
        finish();
    }


}

