package com.example.ecommerce;

public class Order_Item {
    private String itemLogo;
    private String id;
    private String itemName;
    private int quantity;
    private int quantityPurchase;
    private double total;


    /****************************************************************************************/


    public Order_Item(String itemLogo, String id, String itemName, int quantity, int quantityPurchase, double total) {
        this.itemLogo = itemLogo;
        this.id = id;
        this.itemName = itemName;
        this.quantity = quantity;
        this.quantityPurchase = quantityPurchase;
        this.total = total;
    }

    public int getQuantityPurchase() {
        return quantityPurchase;
    }

    public void setQuantityPurchase(int quantityPurchase) {
        this.quantityPurchase = quantityPurchase;
    }


    public String getItemLogo() {
        return itemLogo;
    }

    public void setItemLogo(String itemLogo) {
        this.itemLogo = itemLogo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
