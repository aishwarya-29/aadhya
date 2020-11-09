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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyChatHolder> {
    ArrayList<String> chatTitles= new ArrayList<>();
    ArrayList<Integer> replyCount, likeCount, bgImages;
    Context context;
    Map <String, Map<String, ArrayList<String>>>comments;
    ArrayList <Integer> Liked;
    OnChatListener onChatListener;


    public ChatAdapter(Context ct,  Map <String, Map<String, ArrayList<String>>>c, ArrayList<Integer>lc,  ArrayList<Integer> bg, ArrayList<Integer> lk,  OnChatListener onChatListener){
        context=ct;
        comments=c;
        likeCount=lc;
        bgImages=bg;
        Liked=lk;
        chatTitles.addAll(comments.keySet());
        this.onChatListener=onChatListener;
    }

    @NonNull
    @Override
    public MyChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.chat_adapter, parent, false);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new MyChatHolder(view, onChatListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyChatHolder holder, final int position) {

        chatTitles.addAll(comments.keySet());
        holder.title.setText(chatTitles.get(position));
        holder.replies.setText(String.valueOf(comments.get(chatTitles.get(position)).size()));
        holder.bg.setImageResource(bgImages.get(position));
        final TextView likes=holder.likes;
        holder.likes.setText(likeCount.get(position).toString());
        holder.lk.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                ImageView lk = view.findViewById(R.id.imageView5);
                if (Liked.contains(position)) {
                    DrawableCompat.setTint(lk.getDrawable(), ContextCompat.getColor(context, R.color.white));
                    Liked.remove(Integer.valueOf(position));
                    likeCount.set(position, Math.max(0, likeCount.get(position) - 1));
                    likes.setText(likeCount.get(position).toString());

                } else {
                    Liked.add(Integer.valueOf(position));
                    likeCount.set(position, likeCount.get(position) + 1);
                    DrawableCompat.setTint(lk.getDrawable(), ContextCompat.getColor(context, R.color.pink));
                    likes.setText(likeCount.get(position).toString());
                }
                String currentUserId;
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                assert currentUser != null;
                currentUserId = currentUser.getUid();
                Query query = FirebaseDatabase.getInstance().getReference().child("User").child(currentUserId);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            snapshot.getRef().child("Liked").setValue(Liked);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        if(Liked.contains(position))
            DrawableCompat.setTint(holder.lk.getDrawable(), ContextCompat.getColor(context, R.color.pink));
        else
            DrawableCompat.setTint(holder.lk.getDrawable(), ContextCompat.getColor(context, R.color.white));
        holder.likes.setText(likeCount.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public static class MyChatHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title, likes, replies;
        ImageView bg, lk;
        OnChatListener onChatListener;
        public MyChatHolder(@NonNull View itemView, OnChatListener onChatListener) {
            super(itemView);
            title=itemView.findViewById(R.id.chatTitle);
            likes=itemView.findViewById(R.id.chatLikes);
            replies=itemView.findViewById(R.id.chatReplies);
            bg=itemView.findViewById(R.id.chatBg);
            lk=itemView.findViewById(R.id.imageView5);
            this.onChatListener=onChatListener;

            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            onChatListener.onChatClick(getAdapterPosition());
        }
    }
    public interface OnChatListener{
        void onChatClick(int position);
    }

}