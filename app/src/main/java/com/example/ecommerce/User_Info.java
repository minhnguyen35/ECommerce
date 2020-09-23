package com.example.ecommerce;

import android.graphics.Bitmap;

import java.io.Serializable;

public class User_Info implements Serializable {
    private String userImage;
    private String username;
    private String password;
    private String phone;
    private String mail;
    private String bankNumber;
    private String address;


    public User_Info() { }

    public User_Info(String username, String password, String phone) {
        this.userImage = null;
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.mail = null;
        this.bankNumber = null;
        this.address = null;
    }

    public User_Info(String username, String password, String phone, String mail, String bankNumber, String address) {
        this.userImage = null;
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.mail = mail;
        this.bankNumber = bankNumber;
        this.address = address;
    }

    public User_Info(String userImage, String username, String password, String phone, String mail, String bankNumber, String address) {
        this.userImage = userImage;
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.mail = mail;
        this.bankNumber = bankNumber;
        this.address = address;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getBankNumber() {
        return bankNumber;
    }

    public void setBankNumber(String bankNumber) {
        this.bankNumber = bankNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
