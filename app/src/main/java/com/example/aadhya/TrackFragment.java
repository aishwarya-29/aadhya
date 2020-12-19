package com.example.aadhya;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class TrackFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    View view;
    private static final int REQUEST_CODE = 102;
    String userID;
    private DatabaseReference databaseReference;
    private LatLng currentLocation;
    Double latitude, longitude;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_track, container, false);
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }
        Uri uri = getActivity().getIntent().getData();
        List<String> params = uri.getPathSegments();
        userID = params.get(1);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("User").orderByChild("key").equalTo(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    latitude = (Double) ds.child("Location").child("Latitude").getValue();
                    longitude = (Double) ds.child("Location").child("Longitude").getValue();
                    setLocation();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }

    private void setLocation() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        currentLocation = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(currentLocation).title("Marker in Sydney"));
        mMap.animateCamera(CameraUpdateFactory.newLatLng(currentLocation));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 14));
    }
}