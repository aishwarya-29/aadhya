package com.example.aadhya;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
    static String name, phone;
    String currentUserEmail;
    FloatingActionButton button;

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

        button = v.findViewById(R.id.edit_about);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String set_name = editName.getText().toString();
                final String set_email = editEmail.getText().toString();
                final String set_phone = editPhone.getText().toString();
                if (set_name == "" || set_email == "" || set_phone == "") {
                    Toast.makeText(getContext(), "Enter all Details", Toast.LENGTH_SHORT).show();
                } else {
                    if (set_phone.length() != 10) {
                        Toast.makeText(getContext(), "Invalid Phone number", Toast.LENGTH_SHORT).show();
                    } else {
                        String pattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                        if (!(set_email.trim().matches(pattern))) {
                            Toast.makeText(getContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
                            ;
                        } else {
                            databaseReference.child("User").orderByChild("uemail").equalTo(currentUserEmail).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot ds : snapshot.getChildren()) {
                                        ds.getRef().child("uname").setValue(set_name);
                                        ds.getRef().child("umobno").setValue(set_phone);
                                        ds.getRef().child("uemail").setValue(set_email);
                                        Toast.makeText(getContext(), "Changed", Toast.LENGTH_LONG).show();
                                        currentUserEmail = set_email;
                                        name = set_name;
                                        phone = set_phone;
                                        setDetails();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }
                }
            }
        });


        return v;
    }

    public static void setDetails(String n, String p) {
        name = n;
        phone = p;

    }

    private void setDetails() {
        editName.setText(name);
        editEmail.setText(currentUserEmail);
        editPhone.setText(phone);
    }

}