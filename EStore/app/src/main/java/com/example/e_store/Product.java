package com.example.e_store;

public class Product {
    public int proID, catID;
    public String proName, image;
    public int quantity;
    public float price;

    Product (int proID, String proName, int catID,
                int quantity, float price, String image) {
        this.proID = proID;
        this.proName = proName;
        this.catID = catID;
        this.quantity = quantity;
        this.price = price;
        this.image = image;
    }
}
