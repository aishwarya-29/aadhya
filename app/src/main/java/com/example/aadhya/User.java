package com.example.aadhya;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

class User {
    public String uname;
    public String uemail;
    public String umobno;
    public String upin;
    public String upass;

    User()
    {
        uname = "";
        uemail="";
        umobno="";
        upin="";
        upass="";
    }

    User(String uname, String uemail, String umobno, String upin, String upass)
    {
        this.uname=uname;
        this.uemail=uemail;
        this.umobno=umobno;
        this.upin=upin;
        this.upass=upass;
    }
}
//class User
//{
//    String name;
//    String address;
//
//    User()
//    {
//        name = "";
//        address = "";
//    }
//
//    User(String name, String address)
//    {
//        this.name = name;
//        this.address = address;
//    }
//
//}

