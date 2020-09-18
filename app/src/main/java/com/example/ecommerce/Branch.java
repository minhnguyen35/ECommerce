package com.example.ecommerce;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;

public class Branch extends Supermarket implements Serializable {
    private int branchID;
    private String address;
    private ArrayList<Category> categoryArrayList;

    public Branch(int id, String name,  Bitmap logo, int branchID, String address, ArrayList<Category> categoryArrayList) {
        super(id, name, logo);
        this.branchID = branchID;
        this.address = address;
        this.categoryArrayList = categoryArrayList;
    }

    public Branch(Supermarket supermarket, int branchID, String address, ArrayList<Category> categoryArrayList) {
        super(supermarket);
        this.branchID = branchID;
        this.address = address;
        this.categoryArrayList = categoryArrayList;
    }

    public Branch(Branch branch){
        super(branch.getSupermarketID(),branch.getName(),branch.getLogo());
        this.branchID = branch.branchID;
        this.address = branch.address;
        this.categoryArrayList = branch.categoryArrayList;
    }

    public int getBranchID() {
        return branchID;
    }

    public void setBranchID(int branchID) {
        this.branchID = branchID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ArrayList<Category> getCategoryArrayList() {
        return categoryArrayList;
    }

    public void setCategoryArrayList(ArrayList<Category> categoryArrayList) {
        this.categoryArrayList = categoryArrayList;
    }
}
