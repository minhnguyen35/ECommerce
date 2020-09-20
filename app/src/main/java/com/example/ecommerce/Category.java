package com.example.ecommerce;

import java.io.Serializable;
import java.util.ArrayList;

public class Category implements Serializable {
    private String ID;
    private String name;
    private ArrayList<String> itemArrayList;
    private String branchID;
    public String getBranchID() {
        return branchID;
    }

    public void setBranchID(String branchID) {
        this.branchID = branchID;
    }


    public Category(String ID, String name, ArrayList<String> itemArrayList, String brID) {
        this.ID = ID;
        this.name = name;
        this.itemArrayList = itemArrayList;
        this.branchID = brID;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getItemArrayList() {
        return itemArrayList;
    }

    public void setItemArrayList(ArrayList<String> itemArrayList) {
        this.itemArrayList = itemArrayList;
    }
}
