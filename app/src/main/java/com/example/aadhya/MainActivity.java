package com.example.aadhya;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    Button b1;
    ConstraintLayout constraintLayout;
    TextView tvTimeMsg;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
//        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_main);
        b1=findViewById(R.id.btn_signin);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
        constraintLayout=findViewById(R.id.container);
        tvTimeMsg=findViewById(R.id.tv_time_msg);
        Calendar c= Calendar.getInstance();
        int toD= c.get(Calendar.HOUR_OF_DAY);
        if(toD>=0 && toD<12)
        {
            //morning
            constraintLayout.setBackground(getDrawable(R.drawable.good_morning_img));
            tvTimeMsg.setText("Good Morning");

        }
        else if(toD>=12 && toD<16)
        {
            constraintLayout.setBackground(getDrawable(R.drawable.good_morning_img));
            tvTimeMsg.setText("Good Afternoon");
            //afternoon
        }
        else if(toD>=16 && toD<21)
        {
            constraintLayout.setBackground(getDrawable(R.drawable.good_night_img));
            tvTimeMsg.setText("Good Evening");
            //evening
        }
        else if(toD>=21 && toD<24)
        {
            //night
            constraintLayout.setBackground(getDrawable(R.drawable.good_night_img));
            tvTimeMsg.setText("Good Night");

        }
    }
    public void signUp(View v)
    {
        Intent click= new Intent(this, signUp.class);
        startActivity(click);
    }


}