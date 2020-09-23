package com.example.ecommerce;

import java.io.Serializable;

public class Supermarket implements Serializable {
    private String supermarketID;
    private String name;
    private String logo;

    public Supermarket(){}

    public Supermarket(String id, String name, String logo) {
        this.supermarketID = id;
        this.name = name;
        this.logo = logo;
    }

    public Supermarket(Supermarket supermarket){
        this.supermarketID = supermarket.supermarketID;
        this.name = supermarket.name;
        this.logo = supermarket.logo;
    }

    public String getSupermarketID() {
        return supermarketID;
    }

    public void setSupermarketID(String supermarketID) {
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

