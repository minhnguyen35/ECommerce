package com.example.ecommerce;

import java.util.ArrayList;

public class User_Order {
    private String id; //order id
    private String supermarketName;
    private String date;
    private int checkOutType; /* 1 for COD, 2 for Bank */
    private int shipType; /* 1 for take over, 2 for delivery */
    private long total;
    private boolean status;
    private String account;

    /****************************************************************************************/

    public User_Order()
    {}

    public User_Order(String id, String superMarketName, String date, int checkOutType, int shipType, long total, boolean status, String account) {
        this.id = id;
        this.supermarketName = superMarketName;
        this.date = date;
        this.checkOutType = checkOutType;
        this.shipType = shipType;
        this.total = total;
        this.status = status;
        this.account = account;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }




    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSuperMarketName() {
        return supermarketName;
    }

    public void setSuperMarketName(String superMarketName) {
        this.supermarketName = superMarketName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCheckOutType() {
        return checkOutType;
    }

    public void setCheckOutType(int checkOutType) {
        this.checkOutType = checkOutType;
    }

    public int getShipType() {
        return shipType;
    }

    public void setShipType(int shipType) {
        this.shipType = shipType;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
