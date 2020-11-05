package com.example.aadhya;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;


public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyChatHolder> {
    ArrayList<String> chatTitles;
    ArrayList<Integer> replyCount, likeCount, bgImages;
    Context context;
    ArrayList <Integer> Liked;

    public ChatAdapter(Context ct,ArrayList<String> title, ArrayList<Integer> rc, ArrayList<Integer>lc,  ArrayList<Integer> bg, ArrayList<Integer> lk){
        context=ct;
        chatTitles=title;
        replyCount=rc;
        likeCount=lc;
        bgImages=bg;
        Liked=lk;
    }

    @NonNull
    @Override
    public MyChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.chat_adapter, parent, false);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new ChatAdapter.MyChatHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyChatHolder holder, final int position) {
        holder.title.setText(chatTitles.get(position));
        holder.likes.setText(likeCount.get(position).toString());
        holder.replies.setText(replyCount.get(position).toString());
        holder.bg.setImageResource(bgImages.get(position));
        holder.lk.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                ImageView lk= view.findViewById(R.id.imageView5);
                if(Liked.contains(position)){
                    DrawableCompat.setTint(lk.getDrawable(), ContextCompat.getColor(context, R.color.black));
                    Liked.remove(position);
                    likeCount.set(position, Math.max(0,likeCount.get(position)-1) );
                    
                }

                else{
                    Liked.add(position);
                    likeCount.set(position, likeCount.get(position)+1);
                    DrawableCompat.setTint(lk.getDrawable(), ContextCompat.getColor(context, R.color.pink));
                }


            }
        });
        if(Liked.contains(position))
            DrawableCompat.setTint(holder.lk.getDrawable(), ContextCompat.getColor(context, R.color.pink));
        else
            DrawableCompat.setTint(holder.lk.getDrawable(), ContextCompat.getColor(context, R.color.black));

    }

    @Override
    public int getItemCount() {
        return chatTitles.size();
    }

    public class MyChatHolder extends RecyclerView.ViewHolder {
        TextView title, likes, replies;
        ImageView bg, lk;
        public MyChatHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.chatTitle);
            likes=itemView.findViewById(R.id.chatLikes);
            replies=itemView.findViewById(R.id.chatReplies);
            bg=itemView.findViewById(R.id.chatBg);
            lk=itemView.findViewById(R.id.imageView5);

        }
    }
}