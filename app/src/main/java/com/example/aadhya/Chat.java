package com.example.aadhya;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Chat extends AppCompatActivity {
    TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        title=findViewById(R.id.topic);
        if(getIntent().hasExtra("Position")){
            int position= getIntent().getIntExtra("Position",0);
            Map <String, Map<String, ArrayList<String>>>comments= ChatFragment.comments;
            ArrayList<String> chatTitles = new ArrayList<>(comments.keySet());
            title.setText(chatTitles.get(position));
        }

    }
}