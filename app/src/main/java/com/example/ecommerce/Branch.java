package com.example.ecommerce;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;

public class Branch extends Supermarket implements Serializable {
    private int branchID;
    private String address;
    private double[] latLng;
    private ArrayList<Category> categoryArrayList;

    public Branch(int id, String name, String logo, int branchID, String address, double[] latLng, ArrayList<Category> categoryArrayList) {
        super(id, name, logo);
        this.branchID = branchID;
        this.address = address;
        this.latLng = latLng;
        this.categoryArrayList = categoryArrayList;
    }

    public Branch(Supermarket supermarket, int branchID, String address, double[] latLng, ArrayList<Category> categoryArrayList) {
        super(supermarket);
        this.branchID = branchID;
        this.address = address;
        this.latLng = latLng;
        this.categoryArrayList = categoryArrayList;
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

    public double[] getLatLng() {
        return latLng;
    }

    public void setLatLng(double[] latLng) {
        this.latLng = latLng;
    }

    public ArrayList<Category> getCategoryArrayList() {
        return categoryArrayList;
    }

    public void setCategoryArrayList(ArrayList<Category> categoryArrayList) {
        this.categoryArrayList = categoryArrayList;
    }
}
