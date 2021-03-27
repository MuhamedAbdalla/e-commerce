package com.example.e_store;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.PrintWriter;

public class DatabaseManager extends SQLiteOpenHelper {
    private static final String Database = "ProjectTani";
    SQLiteDatabase database;

    public DatabaseManager(Context context) {
        super(context, Database, null, 355);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table customers(custID integer primary key autoincrement , " +
                "custName text not null, " +
                "username text not null, " +
                "password text not null, " +
                "gender boolean not null, " +
                "birthday text not null, " +
                "job text not null);" +
                "");
        db.execSQL("create table categories(catID integer primary key autoincrement , " +
                "catName text not null);" +
                "");
        db.execSQL("create table orders(ordID integer primary key autoincrement, " +
                "ordDate text not null, " +
                "address text not null, " +
                "custID integer, " +
                "FOREIGN KEY (custID) REFERENCES customer(custID));" +
                "");
        db.execSQL("create table products(proID integer primary key autoincrement, " +
                "proName text not null, " +
                "quantity integer not null, " +
                "price float not null, " +
                "image text not null, " +
                "catID text, " +
                "FOREIGN KEY (catID) REFERENCES categories(catID));" +
                "");
        db.execSQL("create table orderdetails(proID integer, " +
                "ordID integer, " +
                "quantity integer not null, " +
                "FOREIGN KEY (proID) REFERENCES products(proID), " +
                "FOREIGN KEY (ordID) REFERENCES orders(ordID), " +
                "PRIMARY KEY (proID, ordID));" +
                "");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("drop table if exists orderdetails");
            db.execSQL("drop table if exists products");
            db.execSQL("drop table if exists orders");
            db.execSQL("drop table if exists categories");
            db.execSQL("drop table if exists customers");
            onCreate(db);
        }
    }

    public void insertCustomer(Customer customer) {
        try {
            ContentValues row = new ContentValues();
            row.put("custName", customer.custName);
            row.put("username", customer.username);
            row.put("password", customer.password);
            row.put("gender", customer.isMale);
            row.put("birthday", customer.birthday);
            row.put("job", customer.job);
            database = getWritableDatabase();
            database.insert("customers", null, row);
            database.close();
        }
        catch (Exception e) {
            PrintWriter P = new PrintWriter(System.out);
            P.println(e.toString());
        }
    }

    public void insertCategory(Category category) {
        ContentValues row = new ContentValues();
        row.put("catName", category.catName);
        database = getWritableDatabase();
        database.insert("categories", null, row);
        database.close();
    }

    public void insertOrder(Order order) {
        ContentValues row = new ContentValues();
        row.put("ordDate", order.ordDate);
        row.put("address", order.address);
        row.put("custID", order.custID);
        database = getWritableDatabase();
        database.insert("orders", null, row);
        database.close();
    }

    public void insertProduct(Product product) {
        ContentValues row = new ContentValues();
        row.put("proName", product.proName);
        row.put("price", product.price);
        row.put("quantity", product.quantity);
        row.put("image", product.image);
        row.put("catID", product.catID);
        database = getWritableDatabase();
        database.insert("products", null, row);
        database.close();
    }

    public void insertDetails(OrderDetail orderDetail) {
        ContentValues row = new ContentValues();
        row.put("ordID", orderDetail.ordID);
        row.put("proID", orderDetail.proID);
        row.put("quantity", orderDetail.quantity);
        database = getWritableDatabase();
        database.insert("orderdetails", null, row);
        database.close();
    }

    public Cursor fetchCustomer(Query query) {
        database = getReadableDatabase();
        Cursor cursor = database.query("customers", query.columnNames, query.where, query.args,
                                    null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        database.close();
        return cursor;
    }

    public Cursor fetchOrder(Query query) {
        database = getReadableDatabase();
        Cursor cursor = database.query("orders", query.columnNames, query.where, query.args,
                null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        database.close();
        return cursor;
    }

    public Cursor fetchProduct(Query query) {
        database = getReadableDatabase();
        Cursor cursor = database.query("products", query.columnNames, query.where, query.args,
                null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        database.close();
        return cursor;
    }

    public Cursor fetchCategory(Query query) {
        database = getReadableDatabase();
        Cursor cursor = database.query("categories", query.columnNames, query.where, query.args,
                null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        database.close();
        return cursor;
    }

    public Cursor fetchDetails(Query query) {
        database = getReadableDatabase();
        Cursor cursor = database.query("orderdetails", query.columnNames, query.where, query.args,
                null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        database.close();
        return cursor;
    }

    public int updateCustomer(Query query, ContentValues cv) {
        database = getReadableDatabase();
        int n = database.update("customers", cv, query.where, query.args);
        database.close();
        return n;
    }

    public void deleteProduct(Query query) {
        database = getReadableDatabase();
        database.delete("products", query.where, query.args);
        database.close();
    }
}
