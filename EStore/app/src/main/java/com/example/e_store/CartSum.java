package com.example.e_store;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartSum extends AppCompatActivity {
    TextView addressView;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sum_cart);

        addressView = (TextView) findViewById(R.id.textView20);

        DatabaseManager DB = new DatabaseManager(this);

        ListView lst_names = (ListView) findViewById(R.id.names);
        ListView lst_prices = (ListView) findViewById(R.id.prices);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        final ArrayAdapter<String> NlstAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        final ArrayAdapter<String> PlstAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        lst_names.setAdapter(NlstAdapter);
        lst_prices.setAdapter(PlstAdapter);

        float sum = 0;

        for (int i = 0; i < Cart.myCart.size(); i++) {
            NlstAdapter.add(Cart.myCart.get(i).proName);
            NlstAdapter.add(String.valueOf(Cart.myCart.get(i).price));
            sum += Cart.myCart.get(i).price * Cart.myCart.get(i).quantity;
        }

        TextView summation = (TextView) findViewById(R.id.textView5);

        summation.setText(String.valueOf(sum));

        Button confirm = (Button) findViewById(R.id.button11);

        confirm.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(CartSum.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                }
                else {
                    ActivityCompat.requestPermissions(CartSum.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }
                if (!Cart.curAddress.isEmpty()) {
                    for (int i = 0; i < Cart.myCart.size(); i++) {
                        String date = "14-05-2021";
                        DB.insertOrder(new Order(0, date, Cart.custID, Cart.curAddress));
                        Query query = new Query();
                        query.columnNames = new String[]{"ordID"};
                        query.where = "ordDate=? and custID=? and address=?";
                        query.args = new String[]{date, String.valueOf(Cart.custID), Cart.curAddress};
                        Cursor cursor = DB.fetchOrder(query);
                        DB.insertDetails(new OrderDetail(Integer.parseInt(cursor.getString(0)), Cart.myCart.get(i).proID,
                                Cart.myCart.get(i).quantity));
                        query.columnNames = new String[]{"proID", "proName", "price", "quantity", "catID", "image"};
                        query.where = "proID=? and catID=?";
                        query.args = new String[]{String.valueOf(Cart.myCart.get(i).proID), String.valueOf(Cart.myCart.get(i).catID)};

                        cursor = DB.fetchProduct(query);
                        Product curProduct = new Product(Integer.parseInt(cursor.getString(0)), cursor.getString(1), Integer.parseInt(cursor.getString(4)),
                                Integer.parseInt(cursor.getString(3)), Float.parseFloat(cursor.getString(2)), cursor.getString(5));
                        curProduct.quantity -= Cart.myCart.get(i).quantity;

                        query.columnNames = new String[]{"proID", "proName", "price", "quantity", "catID", "image"};
                        query.where = "proID=? and catID=?";
                        query.args = new String[]{String.valueOf(Cart.myCart.get(i).proID), String.valueOf(Cart.myCart.get(i).catID)};

                        DB.deleteProduct(query);

                        if (curProduct.quantity > 0) {
                            DB.insertProduct(curProduct);
                        }
                    }
                    Cart.myCart.clear();
                    Cart.curAddress = "";
                    finish();
                }
            }
        });

        Button back = (Button) findViewById(R.id.button19);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    Geocoder geocoder = new Geocoder(CartSum.this, Locale.getDefault());
                    try {
                        List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        Cart.curAddress = addressList.get(0).getAddressLine(0);
                        addressView.setText(Cart.curAddress);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Address null", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}