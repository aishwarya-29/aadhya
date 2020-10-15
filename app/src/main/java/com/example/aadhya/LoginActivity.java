package com.example.aadhya;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Button login;
    EditText email, password;
    TextView su;
    String realpassword = "password";
    private FirebaseAuth auth;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        auth = FirebaseAuth.getInstance();
        login = findViewById(R.id.login);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        su= findViewById(R.id.sup);
        login.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Signing in");
        progressDialog.setContentView(R.layout.progress_dialog);
    }

    @Override
    public void onClick(View view) {
        progressDialog.show();
        if(email.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(),"Enter all details",Toast.LENGTH_SHORT).show();
        }
        else {
            String em = email.getText().toString();
            String pattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
            if(!(em.trim().matches(pattern))) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Invalid email address", Toast.LENGTH_SHORT).show();;
            }
            else {
                String login_email = email.getText().toString();
                String login_password = password.getText().toString();
                auth.signInWithEmailAndPassword(login_email, login_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            progressDialog.dismiss();
                            Intent i=new Intent(LoginActivity.this, MainScreen.class);
                            startActivity(i);
                            finish();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Login failed!!  " + task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }
    }
    public void su(View v)
    {
        Intent i = new Intent(LoginActivity.this,signUp.class);
        startActivity(i);
        finish();
    }
    public void forgotpass(View v)
    {
        Intent i = new Intent(LoginActivity.this,ForgotPassword.class);
        startActivity(i);
        finish();
    }
}

