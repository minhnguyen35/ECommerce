package com.example.ecommerce;

public class Order_Item {
    private String itemLogo;
    private String orderId;
    private String id;
    private String itemName;
    private int quantity;
    private int quantityPurchase;
    private long total;


    /****************************************************************************************/




    public Order_Item(String itemLogo, String id, String itemName, int quantity, int quantityPurchase, long total) {
        this.itemLogo = itemLogo;
        this.id = id;
        this.itemName = itemName;
        this.quantity = quantity;
        this.quantityPurchase = quantityPurchase;
        this.total = total;
    }


    public Order_Item(String itemLogo, String orderId, String id, String itemName, int quantity, int quantityPurchase, long total) {
        this.itemLogo = itemLogo;
        this.orderId = orderId;
        this.id = id;
        this.itemName = itemName;
        this.quantity = quantity;
        this.quantityPurchase = quantityPurchase;
        this.total = total;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
