package com.example.aadhya;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

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
    public class CustomList extends ArrayAdapter {
        ArrayList<String> contactNames;
        ArrayList<String> contactno;
        ArrayList<Integer> imageid;
        private Activity context;

        public CustomList(Activity context, ArrayList<String> contactNames, ArrayList<String> contactno, ArrayList<Integer> imageid) {
            super(context, R.layout.row, contactNames);
            this.context = context;
            this.contactNames = contactNames;
            this.contactno = contactno;
            this.imageid = imageid;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row=convertView;
            LayoutInflater inflater = context.getLayoutInflater();
            if(convertView==null)
                row = inflater.inflate(R.layout.row, null, true);
            TextView contactNameTv = (TextView) row.findViewById(R.id.ContactName);
            TextView contactNoTv = (TextView) row.findViewById(R.id.ContactStatus);
            ImageView img = (ImageView) row.findViewById(R.id.Dp);

            contactNameTv.setText(contactNames.get(position));
            contactNoTv.setText(contactno.get(position));
            img.setImageResource(imageid.get(position));
            return  row;
        }
    }

    private ListView lv;
    private ArrayAdapter<String> adapter;
    ArrayList<String> contactNames= new ArrayList<>();
    ArrayList <String> contactno = new ArrayList<>();
    ArrayList <Integer> imageid=new ArrayList<>();
    View v;
    FloatingActionButton b;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v= inflater.inflate(R.layout.fragment_contacts, container, false);
        lv = (ListView) v.findViewById(R.id.listv);
        adapter = new ArrayAdapter<String>(getActivity(),R.layout.row, R.id.ContactName,contactNames);
        lv.setAdapter(adapter);
        CustomList customList = new CustomList(getActivity(), contactNames, contactno, imageid);
        lv.setAdapter(customList);
        b=v.findViewById(R.id.contactBtn);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(contactPickerIntent, 111);
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String value = adapter.getItem(position);
                Toast.makeText(v.getContext(), value, Toast.LENGTH_SHORT).show();
            }});
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
                        adapter = new ArrayAdapter<String>(getActivity(),R.layout.row, R.id.ContactName,contactNames);
                        lv.setAdapter(adapter);
                        CustomList customList = new CustomList(getActivity(), contactNames, contactno, imageid);
                        lv.setAdapter(customList);
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