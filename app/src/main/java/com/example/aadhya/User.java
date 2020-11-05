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
<<<<<<< HEAD
    public String uadhaar, key;
    public String[] contacts;
||||||| d7d0a3a
    public String uadhaar;
    public String[] contacts;
=======
    public String uadhaar;
    public ArrayList<String> contacts;
>>>>>>> eba2e19b82d0885e355937a49f4430795e74f2c3
    public ArrayList<String> cname= new ArrayList<>();
    public static ArrayList <String> cno = new ArrayList<>();
    User()
    {
        uname = "";
        uemail="";
        umobno="";
        upin="";
        uadhaar="";
<<<<<<< HEAD
        key="";
        contacts= new String[0];
||||||| d7d0a3a
        contacts= new String[0];
=======
        contacts= new ArrayList<>();
>>>>>>> eba2e19b82d0885e355937a49f4430795e74f2c3
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
