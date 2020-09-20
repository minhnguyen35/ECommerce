package com.example.ecommerce;

import java.io.Serializable;
import java.util.ArrayList;

public class Branch extends Supermarket implements Serializable {
    private String branchID;
    private String address;
    private double[] latLng;
    private ArrayList<Integer> categoryArrayList;

    public Branch(String id, String name, String logo, String branchID, String address, double[] latLng, ArrayList<Integer> categoryArrayList) {
        super(id, name, logo);
        this.branchID = branchID;
        this.address = address;
        this.latLng = latLng;
        this.categoryArrayList = categoryArrayList;
    }

    public Branch(Supermarket supermarket, String branchID, String address, double[] latLng, ArrayList<Integer> categoryArrayList) {
        super(supermarket);
        this.branchID = branchID;
        this.address = address;
        this.latLng = latLng;
        this.categoryArrayList = categoryArrayList;
    }


    public String getBranchID() {
        return branchID;
    }

    public void setBranchID(String branchID) {
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

    public ArrayList<Integer> getCategoryArrayList() {
        return categoryArrayList;
    }

    public void setCategoryArrayList(ArrayList<Integer> categoryArrayList) {
        this.categoryArrayList = categoryArrayList;
    }
}
