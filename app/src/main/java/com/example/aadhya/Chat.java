package com.example.aadhya;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Chat extends AppCompatActivity implements CommentAdapter.OnCommentListener {
    TextView title;
    RecyclerView rc;
    public static RecyclerView.Adapter ca;
    EditText ed1;
    Button b1;
    int position = 0;
    ImageView post, back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        rc = findViewById(R.id.comments);
        title = findViewById(R.id.topic);
        ed1 = findViewById(R.id.reply);
        b1 = findViewById(R.id.addbtn);
        post = findViewById(R.id.postImg);
        back = findViewById(R.id.back);
        if (getIntent().hasExtra("Position")) {
            position = getIntent().getIntExtra("Position", 0);
            final ArrayList<String> chatTitles = new ArrayList<>(ChatFragment.comments.keySet());

            title.setText(chatTitles.get(position));
            ca = new CommentAdapter(getApplicationContext(), ChatFragment.comments.get(chatTitles.get(position)), this);
            rc.setAdapter(ca);
            rc.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            post.setImageResource(ChatFragment.bgImages.get(position));
            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String s1 = String.valueOf(ed1.getText());
                    if (!s1.equals("")) {
                        s1 = s1.replaceAll("\\.", "^");
                        ChatFragment.comments.get(chatTitles.get(position)).put(String.valueOf(s1), new ArrayList<String>() {{
                            add("");
                        }});
                        ca.notifyDataSetChanged();
                        ChatFragment.SetComments();
                        ChatFragment.ca.notifyDataSetChanged();
                        ed1.setText("");
                    }

                }
            });
        }

    }

    @Override
    public void OnCommentClick(int pos) {
        Intent intent = new Intent(this, Replies.class);
        intent.putExtra("Position", pos);
        intent.putExtra("Post position", position);
        startActivity(intent);
    }
}