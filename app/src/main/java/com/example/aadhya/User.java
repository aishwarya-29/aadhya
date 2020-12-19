package com.example.aadhya;

import java.util.ArrayList;

class User {
    public String uname;
    public String uemail;
    public String umobno;
    public String upin;
    public String uadhaar;
    public ArrayList<String> contacts;
    public ArrayList<String> cname = new ArrayList<>();
    public static ArrayList<String> cno = new ArrayList<>();

    User() {
        uname = "";
        uemail = "";
        umobno = "";
        upin = "";
        uadhaar = "";
        contacts = new ArrayList<>();

    }

    User(String uname, String uemail, String umobno, String upin, String uadhaar) {
        this.uname = uname;
        this.uemail = uemail;
        this.umobno = umobno;
        this.upin = upin;
        this.uadhaar = uadhaar;
        this.contacts = new ArrayList<>();
    }
}
