package com.example.aadhya;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Button login;
    EditText email, password;
    String realpassword = "password";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        login = findViewById(R.id.login);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(email.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(),"Enter all details",Toast.LENGTH_SHORT).show();
        }
        else {
            String em = email.getText().toString();
            String pattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
            if(!(em.trim().matches(pattern))) {
                Toast.makeText(getApplicationContext(), "Invalid email address", Toast.LENGTH_SHORT).show();;
            }
            else {
                if(password.getText().toString().equals(realpassword)) {
                    Intent i=new Intent(LoginActivity.this, MainScreen.class);
                    Toast.makeText(getApplicationContext(),"Succesfully Logged in", Toast.LENGTH_SHORT).show();
                    startActivity(i);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(),"Password does not match",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
