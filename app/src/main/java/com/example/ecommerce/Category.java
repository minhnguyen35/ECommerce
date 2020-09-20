package com.example.ecommerce;

import java.io.Serializable;
import java.util.ArrayList;

public class Category implements Serializable {
    private String categoryID;
    private String name;
    private ArrayList<Item> itemArrayList;

    public Category(String ID, String name, ArrayList<Item> itemArrayList) {
        this.categoryID = ID;
        this.name = name;
        this.itemArrayList = itemArrayList;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Item> getItemArrayList() {
        return itemArrayList;
    }

    public void setItemArrayList(ArrayList<Item> itemArrayList) {
        this.itemArrayList = itemArrayList;
    }
}
