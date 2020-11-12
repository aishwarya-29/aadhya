package com.example.aadhya;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;


public class Home extends Fragment {
    public static ArrayList<Integer> userImg = new ArrayList<Integer>() {{
        add(R.drawable.user1);
        add(R.drawable.user4);
        add(R.drawable.user5);
        add(R.drawable.user7);
        add(R.drawable.user8);
        add(R.drawable.user2);
        add(R.drawable.user3);
        add(R.drawable.user6);
        add(R.drawable.user9);
        add(R.drawable.user10);
        add(R.drawable.user11);
        add(R.drawable.user12);
        add(R.drawable.user13);
        add(R.drawable.user14);
        add(R.drawable.user15);
        add(R.drawable.user16);
        add(R.drawable.user17);
        add(R.drawable.user18);
        add(R.drawable.user19);
        add(R.drawable.user20);
        add(R.drawable.user21);
        add(R.drawable.user22);
        add(R.drawable.user23);
        add(R.drawable.user24);
        add(R.drawable.user25);
        add(R.drawable.user26);
        add(R.drawable.user27);
        add(R.drawable.user28);
        add(R.drawable.user29);
        add(R.drawable.user30);
        add(R.drawable.user31);
        add(R.drawable.user32);
        add(R.drawable.user33);
        add(R.drawable.user34);
        add(R.drawable.user35);
    }};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_home, container, false);
    }
}
