package com.example.ecommerce;

public class Order_Item {
    public Order_Item(String itemLogo, String id, String itemName, int quantity, int quantityPurchase, double total, String order_id) {
        this.itemLogo = itemLogo;
        this.id = id;
        this.itemName = itemName;
        this.quantity = quantity;
        this.quantityPurchase = quantityPurchase;
        this.total = total;
        this.order_id = order_id;
    }
    public Order_Item()
    {

    }
    public String getItemLogo() {
        return itemLogo;
    }

    public String getId() {
        return id;
    }

    public String getItemName() {
        return itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getQuantityPurchase() {
        return quantityPurchase;
    }

    public double getTotal() {
        return total;
    }

    public String getOrder_id() {
        return order_id;
    }

    private String itemLogo;
    private String id;
    private String itemName;
    private int quantity;
    private int quantityPurchase;
    private double total;
    private String order_id;

}
