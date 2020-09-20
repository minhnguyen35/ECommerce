package com.example.ecommerce;

public class User_Info {
    private String userImage;
    private String username;
    private String password;
    private String phone;
    private String mail;
    private String bankNumber;
    private String address;
    public User_Info()
    {

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

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public String getMail() {
        return mail;
    }

    public String getBankNumber() {
        return bankNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setBankNumber(String bankNumber) {
        this.bankNumber = bankNumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }


}
