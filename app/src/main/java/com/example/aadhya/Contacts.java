package com.example.aadhya;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;

public class Contacts extends Fragment {
    View v;
    TabLayout tabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_contacts, container, false);
        tabLayout = v.findViewById(R.id.tabLayout);
        final FirstFragment firstFragment = new FirstFragment();
        final SecondFragment secondFragment = new SecondFragment();
        getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.frame, firstFragment).commit();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (position == 0) {
                    getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.frame, firstFragment).commit();
                } else if (position == 1) {
                    getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.frame, secondFragment).commit();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return v;
    }


}