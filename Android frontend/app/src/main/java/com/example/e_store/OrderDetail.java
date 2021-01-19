package com.example.e_store;

public class OrderDetail {
    int ordID, proID;
    int quantity;

    OrderDetail (int ordID, int proID, int quantity) {
        this.ordID = ordID;
        this.proID = proID;
        this.quantity = quantity;
    }
}
