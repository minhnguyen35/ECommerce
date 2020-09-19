package com.example.ecommerce;

import java.io.Serializable;

public class Supermarket implements Serializable {
    private int supermarketID;
    private String name;
    private String logo;

    public Supermarket(int id, String name, String logo) {
        this.supermarketID = id;
        this.name = name;
        this.logo = logo;
    }

    public Supermarket(Supermarket supermarket){
        this.supermarketID = supermarket.supermarketID;
        this.name = supermarket.name;
        this.logo = supermarket.logo;
    }

    public int getSupermarketID() {
        return supermarketID;
    }

    public void setSupermarketID(int supermarketID) {
        this.supermarketID = supermarketID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

}

