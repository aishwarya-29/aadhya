package com.example.aadhya;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
    Integer count=1;
    Boolean readUserInput= false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sup);
        name = findViewById(R.id.textname);
        pno = findViewById(R.id.tvpno);
        pin = findViewById(R.id.textpin);
        email = findViewById(R.id.textmail);
        aadhar = findViewById(R.id.tvaadhar);
        pwd = findViewById(R.id.tvpass);
        pwd2 = findViewById(R.id.tvpassconf);
        btnSubmit = findViewById(R.id.btn_submit);
        Button clear;
        clear = findViewById(R.id.btn_clear);
        clear.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!readUserInput) {
                            name.setText("");
                            pno.setText("");
                            pin.setText("");
                            email.setText("");
                            aadhar.setText("");
                            pwd.setText("");
                            pwd2.setText("");
                        }
                    }
                }
        );

    }

    public void onClick(View v) {
        if (name.getText().toString().isEmpty() || pno.getText().toString().isEmpty()
                || pin.getText().toString().isEmpty() || email.getText().toString().isEmpty() || aadhar.getText().toString().isEmpty() || pwd.getText().toString().isEmpty() || pwd2.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter the details!!", Toast.LENGTH_LONG).show();
        } else {

            String r1 = String.valueOf(pwd.getText().toString());
            String str1 = String.valueOf(pwd2.getText().toString());
            Integer len = str1.length();
            if (r1.regionMatches(0, str1, 0, len)) {
                String str3 = String.valueOf(pwd.getText().toString());
                flaga = 0;
            } else {
                Toast.makeText(signUp.this, "Please confirm your password", Toast.LENGTH_SHORT).show();
            }
        }

        String pno1 = pno.getText().toString();
        if (pno1.length() != 10) {
            Toast.makeText(this, "Enter the phone number correctly!!", Toast.LENGTH_LONG).show();

        }else{
        flagb = 0;}

        String ad = aadhar.getText().toString();
        if (ad.length() != 12) {
            Toast.makeText(this, "Enter the Aadhar number correctly!!", Toast.LENGTH_LONG).show();
        } else {
            flagd = 0;
        }
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (email.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter email address", Toast.LENGTH_SHORT).show();
        } else {
            if (email.getText().toString().trim().matches(emailPattern)) {
                flage = 0;
            }
            else {
                Toast.makeText(getApplicationContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
            }
        }

        String pinval = pin.getText().toString();
        if (pinval.length() != 4) {
            Toast.makeText(getApplicationContext(), "Invalid PIN", Toast.LENGTH_SHORT).show();
        } else {
            flagc = 0;
        }
    }
    public void submit(View v)
    {
        String n= name.getText().toString();
        String e=email.getText().toString();
        String m = pno.getText().toString();
        String p= pin.getText().toString();
        String pd =pwd.getText().toString();
        String userId= "user ".concat(count.toString());
        User user1 = new User(n,e,m,p,pd);
        DatabaseReference reference ;
        reference = FirebaseDatabase.getInstance().getReference();
        reference.child(userId).setValue(user1);
        count++;
        Intent i = new Intent(signUp.this, LoginActivity.class);
        startActivity(i);
    }
}