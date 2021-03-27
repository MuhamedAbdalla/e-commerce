package com.example.e_store;

public class Product {
    public int proID;
    public String proName, catID, image;
    public int quantity;
    public float price;

    Product (int proID, String proName, String catID,
                int quantity, float price, String image) {
        this.proID = proID;
        this.proName = proName;
        this.catID = catID;
        this.quantity = quantity;
        this.price = price;
        this.image = image;
    }
}
