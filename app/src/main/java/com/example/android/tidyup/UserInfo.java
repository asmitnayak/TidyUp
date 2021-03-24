package com.example.android.tidyup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserInfo {

    private FirebaseUser current_UID;
    private String name;
    private String email;
    private String password;
    private String address;
    private String phonenumber;
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();

    private String role;


    public UserInfo(FirebaseAuth fAuth, FirebaseUser current_UID, String name, String email, String password, String address, String phonenumber,String role) {
        this.fAuth = fAuth;
        this.current_UID = current_UID;
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;
        this.phonenumber = phonenumber;
        this.role = role;
    }

    protected FirebaseUser getCurrent_UID() {
        return current_UID;
    }

    protected void setCurrent_UID(FirebaseUser current_UID) {
        this.current_UID = current_UID;
    }

    protected String getName() {
        return name;
    }

    protected void setName(String name) {
        this.name = name;
    }

    protected String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    protected String getPassword() {
        return password;
    }

    protected void setPassword(String password) {
        this.password = password;
    }

    protected String getAddress() {
        return address;
    }

    protected void setAddress(String address) {
        this.address = address;
    }

    protected String getPhonenumber() {
        return phonenumber;
    }

    protected void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
