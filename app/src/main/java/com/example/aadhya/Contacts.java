package com.example.aadhya;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
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
    private ListView lv;
    ContactsAdapter ca;
    ArrayList<String> contactNames= new ArrayList<>();
    public static ArrayList <String> contactno = new ArrayList<>();
    ArrayList <Integer> imageid=new ArrayList<>();
    View v;
    FloatingActionButton b;
    Button dummy;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v=inflater.inflate(R.layout.fragment_contacts, container, false);
        rc=v.findViewById(R.id.list);
        ca= new ContactsAdapter(getContext(), contactNames,contactno,imageid);
        rc.setAdapter(ca);
        rc.setLayoutManager(new LinearLayoutManager(getContext()));
        new ItemTouchHelper(itemTouchHelper).attachToRecyclerView(rc);
        b=v.findViewById(R.id.contactBtn);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(contactPickerIntent, 111);
            }
        });

        dummy=v.findViewById(R.id.button);
        dummy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.SEND_SMS}, 0);
                }
                else{
                    SmsManager sms = SmsManager.getDefault();
                    String message="Testing this let's see if it works";
                    for(String no: contactno){
                        sms.sendTextMessage(no,null, message,null, null);
                    }
                }
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

                        if(contactno.indexOf(number)!=-1){
                           Toast.makeText(getContext(), "Contact Number already added", Toast.LENGTH_LONG).show();
                        }
                        else {

                            contactNames.add(name);
                            contactno.add(number);
                            imageid.add(R.drawable.user);
                            ca.notifyDataSetChanged();
                        }
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
    ItemTouchHelper.SimpleCallback itemTouchHelper = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                contactNames.remove(viewHolder.getAdapterPosition());
                contactno.remove(viewHolder.getAdapterPosition());
                imageid.remove(viewHolder.getAdapterPosition());
                ca.notifyDataSetChanged();
        }
    };

}