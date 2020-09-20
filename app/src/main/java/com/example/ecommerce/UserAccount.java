package com.example.ecommerce;

import java.util.ArrayList;
import java.util.Date;

public class UserAccount {
    private String userID;
    private User_Info userInfo;
    private ArrayList<String> userOrderID;
    private String dateRegister;

    /****************************************************************************************/

    public UserAccount(User_Info userInfo, ArrayList<String> userOrderID, String dateRegister) {
        this.userInfo = userInfo;
        this.userOrderID = userOrderID;
        this.dateRegister = dateRegister;
    }

    public void addOrder(String newOrderID){
        userOrderID.add(newOrderID);
    }

    public User_Info getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(User_Info userInfo) {
        this.userInfo = userInfo;
    }

    public ArrayList<String> getUserOrders() {
        return userOrderID;
    }

    public void setUserOrders(ArrayList<String> userOrders) {
        this.userOrderID = userOrders;
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
}
