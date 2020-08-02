package com.example.ecommerce;

public class Item {
    private int Image;
    private String Name;
    private double Price;

    public Item(int image, String name, double price) {
        Image = image;
        Name = name;
        Price = price;
    }

    public int getImage() {
        return Image;
    }

    public void setImage(int image) {
        Image = image;
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
}
