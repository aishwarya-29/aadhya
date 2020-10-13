package com.example.aadhya;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class Contacts extends Fragment {
    RecyclerView rc;
    RecyclerView.Adapter ra;
    private ListView lv;
    ArrayList<String> contactNames= new ArrayList<>();
    ArrayList <String> contactno = new ArrayList<>();
    ArrayList <Integer> imageid=new ArrayList<>();
    View v;
    FloatingActionButton b;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v=inflater.inflate(R.layout.fragment_contacts, container, false);
        rc=v.findViewById(R.id.list);
        ContactsAdapter ca= new ContactsAdapter(getContext(), contactNames,contactno,imageid);
        rc.setAdapter(ca);
        rc.setLayoutManager(new LinearLayoutManager(getContext()));

        b=v.findViewById(R.id.contactBtn);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(contactPickerIntent, 111);
            }
        });
        return v;
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 111:
                    Cursor cursor;
                    try {                        String name, number, picture;
                        Uri uri = data.getData();
                        assert uri != null;
                        cursor = Objects.requireNonNull(getActivity()).getContentResolver().query(uri, null, null, null, null);
                        if (cursor != null) {
                            cursor.moveToFirst();
                        }
                        int phno = cursor != null ? cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER) : 0;
                        assert cursor != null;
                        int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                        number = cursor.getString(phno);
                        name = cursor.getString(nameIndex);
                        contactNames.add(name);
                        contactno.add(number);
                        imageid.add(R.drawable.user);
                        ContactsAdapter ca= new ContactsAdapter(getContext(), contactNames,contactno,imageid);
                        rc.setAdapter(ca);
                        rc.setLayoutManager(new LinearLayoutManager(getContext()));
                        cursor.close();
                    }
                    catch (Exception e) {
                        StringWriter errors = new StringWriter();
                        e.printStackTrace(new PrintWriter(errors));
                        Log.i("hey",errors.toString());
                    }
                    break;
            }
        }
    }

}