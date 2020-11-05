package com.example.aadhya;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

class User {
    public String uname;
    public String uemail;
    public String umobno;
    public String upin;
    public String uadhaar, key;
    public ArrayList<String> contacts;
    public ArrayList<String> cname= new ArrayList<>();
    public static ArrayList <String> cno = new ArrayList<>();
    User()
    {
        uname = "";
        uemail="";
        umobno="";
        upin="";
        uadhaar="";
        key="";
        contacts= new ArrayList<>();
    }

    User(String uname, String uemail, String umobno, String upin,String uadhaar)
    {
        this.uname=uname;
        this.uemail=uemail;
        this.umobno=umobno;
        this.upin=upin;
        this.uadhaar=uadhaar;
        this.contacts= new ArrayList<>();
    }
}
