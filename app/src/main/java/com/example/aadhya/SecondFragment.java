package com.example.aadhya;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SecondFragment extends Fragment {
    View v;
    EditText editName, editEmail, editPhone;
    DatabaseReference databaseReference;
    String name, phone, currentUserEmail;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_second, container, false);
        editName = v.findViewById(R.id.editname);
        editEmail = v.findViewById(R.id.editemail);
        editPhone = v.findViewById(R.id.editphone);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        currentUserEmail = currentUser.getEmail();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("User").orderByChild("uemail").equalTo(currentUserEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    name = ds.child("uname").getValue(String.class);
                    phone = ds.child("umobno").getValue(String.class);
                    setDetails();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return v;
    }

    private void setDetails() {
        editName.setText(name);
        editEmail.setText(currentUserEmail);
        editPhone.setText(phone);
    }

}