package com.example.aadhya;

import android.os.Bundle;
import android.util.Log;
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
            final int position = (int) getIntent().getExtras().get("Position");
            final int Postpos = (int) getIntent().getExtras().get("Post position");
            Log.i("hey", String.valueOf(position) + " " + Postpos);
            final ArrayList<String> chatTitles = new ArrayList<>(ChatFragment.comments.keySet());
            final String post = chatTitles.get(Postpos);
            ArrayList<String> comment = new ArrayList<>(ChatFragment.comments.get(post).keySet());
            final String title = comment.get(position);
            final ArrayList<String> replies = ChatFragment.comments.get(post).get(title);
            l1 = findViewById(R.id.replies);
            img = findViewById(R.id.commentImg);
            tv1 = findViewById(R.id.commentHead);
            b1 = findViewById(R.id.addReply);
            img.setImageResource(Home.userImg.get(position % 35));
            ed1 = findViewById(R.id.postReply);
            tv1.setText(title.replaceAll("\\^", "."));
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.reply_row, R.id.replyRow, replies);
            l1.setAdapter(adapter);
            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!String.valueOf(ed1.getText()).equals("")) {
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
