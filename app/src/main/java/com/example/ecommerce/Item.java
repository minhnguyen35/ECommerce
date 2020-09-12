package com.example.ecommerce;


import java.io.Serializable;
import java.util.ArrayList;

public class Item implements Serializable {
    private String ID="";
    private int CategoryID;
    private String Name="";
    private double Price;
    private ArrayList<Integer> ImageArrayList = new ArrayList<>();
    private int Quantity;
    private String Description="";

    public Item(String ID, int categoryID, String name, double price, ArrayList<Integer> imageArrayList, int quantity, String description) {
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

    public int getCategoryID() {
        return CategoryID;
    }

    public void setCategoryID(int categoryID) {
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

    public ArrayList<Integer> getImageArrayList() {
        return ImageArrayList;
    }

    public void setImageArrayList(ArrayList<Integer> imageArrayList) {
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
