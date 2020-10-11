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

public class signUp<flag1, flag2, flag3> extends AppCompatActivity implements View.OnClickListener {
    Button btnSubmit;
    EditText name, pno , email, dob, aadhar, pwd, pwd2;
    Integer flaga=-1;
    Integer flagb=-1;
    Integer flagc=-1;
    Integer flagd=-1;
    Integer flage=-1;
    Boolean readUserInput= false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sup);
        name = findViewById(R.id.textname);
        pno = findViewById(R.id.tvpno);
        dob = findViewById(R.id.textdob);
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
                            dob.setText("");
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
                || dob.getText().toString().isEmpty() || email.getText().toString().isEmpty()||aadhar.getText().toString().isEmpty() ||pwd.getText().toString().isEmpty() ||pwd2.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter the details!!", Toast.LENGTH_LONG).show();
        } else {

            String r1 = String.valueOf(pwd.getText().toString());
            String str1 =String.valueOf(pwd2.getText().toString());
            Integer len = str1.length();
            if (r1.regionMatches(0, str1, 0, len)) {
                String str3 = String.valueOf(pwd.getText().toString());
                flaga = 0;
            } else {
                Toast.makeText(signUp.this, "Please confirm your password", Toast.LENGTH_SHORT).show();
            }
        }

        String pno1 =pno.getText().toString();
        if (pno1.length()!=10) {
            Toast.makeText(this, "Enter the phone number correctly!!", Toast.LENGTH_LONG).show();

        }
        flagb = 0;

        String ad= aadhar.getText().toString();
        if(ad.length()!=12){
            Toast.makeText(this, "Enter the Aadhar number correctly!!", Toast.LENGTH_LONG).show();
        }
        else{
            flagd=0;
        }
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if(email.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(),"Enter email address",Toast.LENGTH_SHORT).show();
        }
        else {
            if (email.getText().toString().trim().matches(emailPattern)) {
                flage=0;
            } else {
                Toast.makeText(getApplicationContext(),"Invalid email address", Toast.LENGTH_SHORT).show();
            }
        }

        String dobVal = dob.getText().toString();
        String dobComp[] = dobVal.split("/");
        if (dobComp != null && dobComp.length != 0) {
            int date = Integer.parseInt(dobComp[0]);
            int month = Integer.parseInt(dobComp[1]);
            int year = Integer.parseInt(dobComp[2]);
            if (date < 1 || date > 31) {
                Toast.makeText(this, "Enter a valid date!!", Toast.LENGTH_LONG).show();

            }
            if (month < 1 || month > 12) {
                Toast.makeText(this, "Enter a valid month!!", Toast.LENGTH_LONG).show();
            }
            if (year < 1932 || year > 2020) {
                Toast.makeText(this, "Enter a valid year!!", Toast.LENGTH_LONG).show();
            }
        }
        else {
            flagc = 0;
        }
        btnSubmit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (flaga == 0 && flagb == 0 && flagc == 0 && flagd == 0 && flage == 0) {
                            Intent i = new Intent(signUp.this, LoginActivity.class);
                            startActivity(i);
                        }
                    }
                });

    }


}