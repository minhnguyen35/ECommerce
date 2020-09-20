package com.example.ecommerce;


import java.io.Serializable;
import java.util.ArrayList;

public class Item implements Serializable {
    private String ID="";
    private String CategoryID;
    private String Name="";
    private double Price;
    private ArrayList<String> ImageArrayList;
    private int Quantity;
    private String Description="";

    public Item(String ID, String categoryID, String name, double price, ArrayList<String> imageArrayList, int quantity, String description) {
        this.ID = ID;
        CategoryID = categoryID;
        Name = name;
        Price = price;
        ImageArrayList = imageArrayList;
        Quantity = quantity;
        Description = description;
    }



    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getCategoryID() {
        return CategoryID;
    }

    public void setCategoryID(String categoryID) {
        CategoryID = categoryID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public ArrayList<String> getImageArrayList() {
        return ImageArrayList;
    }

    public void setImageArrayList(ArrayList<String> imageArrayList) {
        ImageArrayList = imageArrayList;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
