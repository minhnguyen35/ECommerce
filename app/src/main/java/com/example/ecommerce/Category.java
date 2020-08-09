package com.example.ecommerce;

import java.io.Serializable;
import java.util.ArrayList;

public class Category implements Serializable {
    private int ID;
    private String name;
    private ArrayList<Item> itemArrayList;

    public Category(int ID, String name, ArrayList<Item> itemArrayList) {
        this.ID = ID;
        this.name = name;
        this.itemArrayList = itemArrayList;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
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
