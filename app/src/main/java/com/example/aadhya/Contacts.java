package com.example.aadhya;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Contacts extends AppCompatActivity {
    public class CustomList extends ArrayAdapter {
        private String[] contactNames;
        private String[] contactStatus;
        private Integer[] imageid;
        private Activity context;

        public CustomList(Activity context, String[] contactNames, String[] contactStatus, Integer[] imageid) {
            super(context, R.layout.row, contactNames);
            this.context = context;
            this.contactNames = contactNames;
            this.contactStatus = contactStatus;
            this.imageid = imageid;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row=convertView;
            LayoutInflater inflater = context.getLayoutInflater();
            if(convertView==null)
                row = inflater.inflate(R.layout.row, null, true);
            TextView textViewCountry = (TextView) row.findViewById(R.id.ContactName);
            TextView textViewCapital = (TextView) row.findViewById(R.id.ContactStatus);
            ImageView imageFlag = (ImageView) row.findViewById(R.id.Dp);

            textViewCountry.setText(contactNames[position]);
            textViewCapital.setText(contactStatus[position]);
            imageFlag.setImageResource(imageid[position]);
            return  row;
        }
    }

    private ListView lv;
    private ArrayAdapter<String> adapter;

    private String contactNames[] = {"Apple", "Orange", "Minion", "Minion Again", "Minion Returns", "Minion Galore"};
    private String contactStatus[] = {"Not half bitten", "Tangerine","Potato", "Po-tah-to", "Tomato", "To-mah-to"};
    private Integer imageid[] = {
            R.drawable.download,
            R.drawable.orange,
            R.drawable.min1,
            R.drawable.min2,
            R.drawable.min3,
            R.drawable.min4
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        lv = (ListView) findViewById(R.id.listv);
        adapter = new ArrayAdapter<String>(this,R.layout.row, R.id.ContactName,contactNames);
        lv.setAdapter(adapter);
        CustomList customCountryList = new CustomList(this, contactNames, contactStatus, imageid);
        lv.setAdapter(customCountryList);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String value=adapter.getItem(position);
                Toast.makeText(getApplicationContext(),value,Toast.LENGTH_SHORT).show();

            }
        });
    }
}