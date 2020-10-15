package com.example.aadhya;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {
    EditText ed;
    Button bu;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_pass);
        bu = findViewById(R.id.btn_mail);
        ed= findViewById(R.id.forgmail);
        mAuth= FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Sending reset link");
        progressDialog.setContentView(R.layout.progress_dialog);
        ed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String mail = ed.getText().toString();
                if(TextUtils.isEmpty(mail))
                {
                    progressDialog.dismiss();
                    Toast.makeText(ForgotPassword.this, "Please enter your email address", Toast.LENGTH_LONG).show();
                }
                else
                {
                    mAuth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                progressDialog.dismiss();
                                Toast.makeText(ForgotPassword.this, "Please check your email", Toast.LENGTH_LONG).show();
                                Intent i= new Intent(ForgotPassword.this, LoginActivity.class);
                                startActivity(i);
                                finish();
                            }
                            else
                            {
                                progressDialog.dismiss();
                                String msg = task.getException().getMessage();
                                Toast.makeText(ForgotPassword.this, "Error: "+msg, Toast.LENGTH_LONG).show();

                            }
                        }


                    });
                }
            }
        });
    }


}

