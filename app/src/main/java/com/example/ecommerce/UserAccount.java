package com.example.ecommerce;

import java.util.Date;

public class UserAccount {
    private String username;
    private String password;
    private String phoneNumber;
    private String dateRegister;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }


    public UserAccount(String username, String password, String phoneNumber, String dateRegister) {
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.dateRegister = dateRegister;
    }


}
