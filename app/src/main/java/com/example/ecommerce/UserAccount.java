package com.example.ecommerce;

import java.util.ArrayList;


public class UserAccount {
    public UserAccount(User_Info userInfo, ArrayList<String> userOrders, String dateRegister) {
        this.userInfo = userInfo;
        this.userOrders = userOrders;
        this.dateRegister = dateRegister;
    }

    public User_Info getUserInfo() {
        return userInfo;
    }

    public ArrayList<String> getUserOrders() {
        return userOrders;
    }

    public String getDateRegister() {
        return dateRegister;
    }

    public void setUserInfo(User_Info userInfo) {
        this.userInfo = userInfo;
    }

    public void setUserOrders(ArrayList<String> userOrders) {
        this.userOrders = userOrders;
    }

    public void setDateRegister(String dateRegister) {
        this.dateRegister = dateRegister;
    }

    private User_Info userInfo;
    private ArrayList<String> userOrders;
    private String dateRegister;




}
