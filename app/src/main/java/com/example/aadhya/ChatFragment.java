package com.example.aadhya;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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


public class ChatFragment extends Fragment implements ChatAdapter.OnChatListener {
    //    static Map<String, ArrayList<String>> replies = new HashMap<String, ArrayList<String>>() {
//        {
//            put("Some Comment", new ArrayList<String>() {{
//                add("I totally agree");
//            }});
//        }
//    };
    public static Map<String, Map<String, ArrayList<String>>> comments = new HashMap<String, Map<String, ArrayList<String>>>();
//    {{
//        put("Top Self Defense Techniques", replies);
//        put("Have you ever been a victim of abuse? Let your feelings out here!", replies);
//        put("Safety gadgets you know and want to share", replies);
//
//    }};

    public static void SetComments() {
        Query q = FirebaseDatabase.getInstance().getReference().child("Posts");
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    snapshot.getRef().child("Comments").setValue(comments);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void setLikes() {
        Query q = FirebaseDatabase.getInstance().getReference().child("Posts");
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    snapshot.getRef().child("Like Count").setValue(likeCount);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    RecyclerView rc;
    Boolean open = false;
    public static RecyclerView.Adapter ca;
    ImageView lk;
    public static ArrayList<Integer> likeCount = new ArrayList<Integer>();
    //    {{
//        add(1);
//        add(0);
//        add(0);
//    }};
    ArrayList<Integer> liked = new ArrayList<>();

    public static ArrayList<Integer> bgImages = new ArrayList<Integer>() {{
        add(R.drawable.defense);
        add(R.drawable.abuse);
        add(R.drawable.safety);
    }};
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String currentUserId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat, container, false);
        rc = v.findViewById(R.id.chats);
//        SetComments();
//        FirebaseDatabase.getInstance().getReference().child("Posts").child("Comments").setValue(comments);
//        FirebaseDatabase.getInstance().getReference().child("Posts").child("Like Count").setValue(likeCount);
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
        if (!open) {
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
                        open = true;

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


        return v;
    }

    @Override
    public void onChatClick(int position) {
        Intent intent = new Intent(getActivity(), Chat.class);
        intent.putExtra("Position", position);
        startActivity(intent);

    }
}