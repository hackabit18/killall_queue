package com.killall_queuehackabit.smartshop;


public class UserProfile {

    public String userName;
    public String address;
    public String contact;
    public String email;

    public UserProfile(){

    }

    public UserProfile(String userName, String address, String contact, String email) {
        this.userName = userName;
        this.address = address;
        this.contact = contact;
        this.email = email;
    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
