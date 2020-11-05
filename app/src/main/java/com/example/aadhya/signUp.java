package com.example.aadhya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class signUp<flag1, flag2, flag3> extends AppCompatActivity implements View.OnClickListener {
    Button btnSubmit;
    EditText name, pno , email, pin, aadhar, pwd, pwd2;
    Integer flaga=-1;
    Integer flagb=-1;
    Integer flagc=-1;
    Integer flagd=-1;
    Integer flage=-1;
    Boolean readUserInput= false;
    private FirebaseAuth auth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sup);
        auth = FirebaseAuth.getInstance();
        name = findViewById(R.id.textname);
        pno = findViewById(R.id.tvpno);
        pin = findViewById(R.id.textpin);
        email = findViewById(R.id.textmail);
        aadhar = findViewById(R.id.tvaadhar);
        pwd = findViewById(R.id.tvpass);
        pwd2 = findViewById(R.id.tvpassconf);
        btnSubmit = findViewById(R.id.btn_submit);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Signing up");
        progressDialog.setContentView(R.layout.progress_dialog);
    }

    public void onClick(View v) {
        progressDialog.show();
        if (name.getText().toString().isEmpty() || pno.getText().toString().isEmpty()
                || pin.getText().toString().isEmpty() || email.getText().toString().isEmpty() || aadhar.getText().toString().isEmpty() || pwd.getText().toString().isEmpty() || pwd2.getText().toString().isEmpty()) {
            progressDialog.dismiss();
            Toast.makeText(this, "Enter the details!!", Toast.LENGTH_LONG).show();
        } else {

            String r1 = pwd.getText().toString();
            String str1 = pwd2.getText().toString();
            if (r1.equals(str1)) {
                flaga = 1;
            } else {
                progressDialog.dismiss();
                Toast.makeText(signUp.this, "Please confirm your password", Toast.LENGTH_SHORT).show();
            }
        }

        String pno1 = pno.getText().toString();
        if (pno1.length() != 10) {
            progressDialog.dismiss();
            Toast.makeText(this, "Enter the phone number correctly!!", Toast.LENGTH_LONG).show();
        }else{
        flagb = 1;}

        String ad = aadhar.getText().toString();
        if (ad.length() != 12) {
            progressDialog.dismiss();
            Toast.makeText(this, "Enter the Aadhar number correctly!!", Toast.LENGTH_LONG).show();
        } else {
            flagd = 1;
        }
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (email.getText().toString().isEmpty()) {
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Enter email address", Toast.LENGTH_SHORT).show();
        } else {
            if (email.getText().toString().trim().matches(emailPattern)) {
                flage = 1;
            }
            else {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
            }
        }

        String pinval = pin.getText().toString();
        if (pinval.length() != 4) {
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Invalid PIN", Toast.LENGTH_SHORT).show();
        } else {
            flagc = 1;
        }
    }
    public void submit(View v)
    {
        String n= name.getText().toString();
        String e=email.getText().toString();
        String m = pno.getText().toString();
        String p= pin.getText().toString();
        String pd =pwd.getText().toString();
        String aa = aadhar.getText().toString();
        User user1 = new User(n,e,m,p,aa);
        String userid= n;
        DatabaseReference reference;
        reference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference newRef = reference.child("User").push();
        newRef.setValue(user1);
        String key = newRef.getKey();
        StringBuilder sb = new StringBuilder(key);
        sb.deleteCharAt(0);
        newRef.child("key").setValue(sb.toString());
        auth.createUserWithEmailAndPassword(e,pd);
        progressDialog.dismiss();
        Intent i = new Intent(signUp.this, MainScreen.class);
        startActivity(i);
        final User user1 = new User(n,e,m,p,aa);
        auth.createUserWithEmailAndPassword(e,pd).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user=auth.getCurrentUser();
                    assert user != null;
                    String uid = user.getUid();
                    DatabaseReference reference;
                    reference = FirebaseDatabase.getInstance().getReference();
                    reference.child("User").child(uid).setValue(user1);
                    progressDialog.dismiss();
                    Intent i = new Intent(signUp.this, MainScreen.class);
                    startActivity(i);
                }
            }
        });

    }
}