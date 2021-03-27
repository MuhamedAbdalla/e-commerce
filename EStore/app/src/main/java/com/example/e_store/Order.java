package com.example.e_store;

public class Order {
    public String ordDate, address;
    public int ordID, custID;

    Order (int ordID, String ordDate, int custID, String address) {
        this.ordID = ordID;
        this.ordDate = ordDate;
        this.custID = custID;
        this.address = address;
    }
}
