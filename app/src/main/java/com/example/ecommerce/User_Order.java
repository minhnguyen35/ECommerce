package com.example.ecommerce;

import java.util.ArrayList;

public class User_Order {
    private String superMarketLogo;
    private String id;
    private String superMarketName;
    private String date;
    private int checkOutType; /* 1 for COD, 2 for Bank */
    private int shipType; /* 1 for take over, 2 for delivery */
    private double total;
    private boolean status;
    public User_Order()
    {

    }
    public User_Order(String superMarketLogo, String id, String superMarketName, String date, int checkOutType, int shipType, double total, boolean status, ArrayList<Order_Item> itemArrayList) {
        this.superMarketLogo = superMarketLogo;
        this.id = id;
        this.superMarketName = superMarketName;
        this.date = date;
        this.checkOutType = checkOutType;
        this.shipType = shipType;
        this.total = total;
        this.status = status;
        this.itemArrayList = itemArrayList;
    }

    public String getSuperMarketLogo() {
        return superMarketLogo;
    }

    public String getId() {
        return id;
    }

    public String getSuperMarketName() {
        return superMarketName;
    }

    public String getDate() {
        return date;
    }

    public int getCheckOutType() {
        return checkOutType;
    }

    public int getShipType() {
        return shipType;
    }

    public double getTotal() {
        return total;
    }

    public boolean isStatus() {
        return status;
    }

    public ArrayList<Order_Item> getItemArrayList() {
        return itemArrayList;
    }

    public void setSuperMarketLogo(String superMarketLogo) {
        this.superMarketLogo = superMarketLogo;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSuperMarketName(String superMarketName) {
        this.superMarketName = superMarketName;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setCheckOutType(int checkOutType) {
        this.checkOutType = checkOutType;
    }

    public void setShipType(int shipType) {
        this.shipType = shipType;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setItemArrayList(ArrayList<Order_Item> itemArrayList) {
        this.itemArrayList = itemArrayList;
    }



    private ArrayList<Order_Item> itemArrayList;
}
