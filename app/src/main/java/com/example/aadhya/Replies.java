package com.example.aadhya;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Replies extends AppCompatActivity {
    TextView tv1;
    ListView l1;
    ImageView img;
    Button b1;
    EditText ed1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replies);
        if (getIntent().hasExtra("Position")) {
            final int position = getIntent().getIntExtra("Position", 0);
            final int Postpos = getIntent().getIntExtra("Post Position", 0);
            final ArrayList<String> chatTitles = new ArrayList<>(ChatFragment.comments.keySet());
            final String post = chatTitles.get(Postpos);
            ArrayList<String> comment = new ArrayList<>(ChatFragment.comments.get(post).keySet());
            final String title = comment.get(position);
            final ArrayList<String> replies = ChatFragment.comments.get(post).get(title);
            l1 = findViewById(R.id.replies);
            img = findViewById(R.id.commentImg);
            tv1 = findViewById(R.id.commentHead);
            b1=findViewById(R.id.addReply);
            img.setImageResource(Home.userImg.get(Postpos % 35));
            ed1=findViewById(R.id.postReply);
            tv1.setText(title.replaceAll("\\^","."));
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.reply_row, R.id.replyRow, replies);
            l1.setAdapter(adapter);
            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!String.valueOf(ed1.getText()).equals("")){
                        ChatFragment.comments.get(post).get(title).add(String.valueOf(ed1.getText()));
                        Chat.ca.notifyDataSetChanged();
                        ChatFragment.SetComments();
                        ChatFragment.ca.notifyDataSetChanged();
                        ed1.setText("");
                    }
                }
            });
        }

    }
}
