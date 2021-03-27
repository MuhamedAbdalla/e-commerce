package com.example.e_store;

public class Customer {
    public int custID;
    public String custName, username, password, birthday, job;
    boolean isMale;

    Customer (int custID, String custName, String username,
                String password, String birthday, String job, boolean isMale) {
        this.custID = custID;
        this.birthday = birthday;
        this.custName = custName;
        this.username = username;
        this.password = password;
        this.job = job;
        this.isMale = isMale;
    }
}
