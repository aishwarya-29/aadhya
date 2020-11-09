package com.example.aadhya;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Map;


public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyHolder> {
    Context context;
    Map<String, ArrayList<String>> comments;
    ArrayList<String> commentHead;
    ArrayList<Integer> userDp;
    OnCommentListener onCommentListener;


    public CommentAdapter(Context ct, Map<String, ArrayList<String>> c, OnCommentListener oc) {
        context = ct;
        comments = c;
        userDp = Home.userImg;
        onCommentListener=oc;
        commentHead = new ArrayList<>(comments.keySet());
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.comment_adapter, parent, false);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new CommentAdapter.MyHolder(view, onCommentListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        commentHead = new ArrayList<>(comments.keySet());
        holder.comment.setText(commentHead.get(position).replaceAll("\\^","."));
        holder.img.setImageResource(userDp.get(position % 35));
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView comment;
        ImageView img;
        OnCommentListener onCommentListener;
        public MyHolder(@NonNull View itemView, OnCommentListener onCommentListener) {
            super(itemView);
            img = itemView.findViewById(R.id.commentImg);
            comment = itemView.findViewById(R.id.commentTitle);
            this.onCommentListener=onCommentListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onCommentListener.OnCommentClick(getAdapterPosition());
        }
    }
    public interface OnCommentListener{
        void OnCommentClick(int position);
    }
}