package com.example.ecommerce;

import java.util.ArrayList;
import java.util.Date;

public class UserAccount {
    private User_Info userInfo;
    private ArrayList<User_Order> userOrders;
    private String dateRegister;

    /****************************************************************************************/

    public UserAccount(User_Info userInfo, ArrayList<User_Order> userOrders, String dateRegister) {
        this.userInfo = userInfo;
        this.userOrders = userOrders;
        this.dateRegister = dateRegister;
    }

    public void addOrder(User_Order newOrder){
        userOrders.add(newOrder);
    }

    public User_Info getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(User_Info userInfo) {
        this.userInfo = userInfo;
    }

    public ArrayList<User_Order> getUserOrders() {
        return userOrders;
    }

    public void setUserOrders(ArrayList<User_Order> userOrders) {
        this.userOrders = userOrders;
    }

    public String getDateRegister() {
        return dateRegister;
    }

    public void setDateRegister(String dateRegister) {
        this.dateRegister = dateRegister;
    }

/*private String username;
    private String password;
    private String phoneNumber;*/
    /*public String getUsername() {
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
    }*/
}
