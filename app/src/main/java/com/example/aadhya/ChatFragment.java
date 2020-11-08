package com.example.aadhya;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.tasks.Task;
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
import java.util.Set;


public class ChatFragment extends Fragment implements ChatAdapter.OnChatListener {
    RecyclerView rc;
    RecyclerView.Adapter ca;
    ImageView lk;

    ArrayList<Integer> likeCount = new ArrayList<Integer>();
    ArrayList<Integer> liked = new ArrayList<>();
    ArrayList<Integer> bgImages = new ArrayList<Integer>(){{
        add(R.drawable.defense);
    }};
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String currentUserId;
    public static Map <String, Map<String, ArrayList<String>>>comments= new HashMap<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat, container, false);
        rc = v.findViewById(R.id.chats);
        ca = new ChatAdapter(getContext(), comments, likeCount, bgImages, liked, this);
        rc.setAdapter(ca);
        rc.setLayoutManager(new LinearLayoutManager(getContext()));
        currentUserId = currentUser.getUid();
        Query query = FirebaseDatabase.getInstance().getReference().child("User").child(currentUserId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("Liked").exists()) {
                    for (DataSnapshot item : snapshot.child("Liked").getChildren()) {
                        ;
                        if (!liked.contains(Math.toIntExact((Long) item.getValue()))) {
                            liked.add(Math.toIntExact((Long) item.getValue()));
                            ca.notifyDataSetChanged();
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Query query1 = FirebaseDatabase.getInstance().getReference().child("Posts");
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot item : snapshot.child("Like Count").getChildren()) {
                        likeCount.add(Math.toIntExact((Long) item.getValue()));
                        ca.notifyDataSetChanged();
                    }
                    for (DataSnapshot item : snapshot.child("Comments").getChildren()) {
                        comments.put(item.getKey(), (Map<String, ArrayList<String>>) item.getValue());
                        ca.notifyDataSetChanged();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return v;
    }

    @Override
    public void onChatClick(int position) {
        Intent intent=new Intent(getActivity(), Chat.class);
        intent.putExtra("Position", position);
        startActivity(intent);

    }
}