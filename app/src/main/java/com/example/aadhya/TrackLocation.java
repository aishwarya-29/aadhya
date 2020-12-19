package com.example.aadhya;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class TrackLocation extends AppCompatActivity {
    Uri uri;
    String userID;
    Button openMap;
    DatabaseReference databaseReference;
    Double latitude1, longitude1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        uri = getIntent().getData();
        List<String> params = uri.getPathSegments();
        userID = params.get(1);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("User").orderByChild("key").equalTo(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    latitude1 = (Double) ds.child("Location").child("Latitude").getValue();
                    longitude1 = (Double) ds.child("Location").child("Longitude").getValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        setLocation();
                    }
                },
                2000);
        openMap = findViewById(R.id.openMap);
    }

    private void setLocation() {
        openMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String label = "Current Location";
                String uriBegin = "geo:" + latitude1 + "," + longitude1;
                String query = latitude1 + "," + longitude1 + "(" + label + ")";
                String encodedQuery = Uri.encode(query);
                String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
                Uri uri = Uri.parse(uriString);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

    }
}
