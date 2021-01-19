package com.example.e_store;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartSum extends AppCompatActivity implements LocationListener {
    LocationManager locationManager;
    String cur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sum_cart);

        //Runtime permissions
        if (ContextCompat.checkSelfPermission(CartSum.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(CartSum.this,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            },100);
        }

        DatabaseManager DB = new DatabaseManager(this);

        ListView lst_names = (ListView)findViewById(R.id.names);
        ListView lst_prices = (ListView)findViewById(R.id.prices);

        final ArrayAdapter<String> NlstAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        final ArrayAdapter<String> PlstAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        lst_names.setAdapter(NlstAdapter);
        lst_prices.setAdapter(PlstAdapter);

        float sum = 0;

        for (int i = 0; i < Cart.myCart.size(); i++) {
            NlstAdapter.add(Cart.myCart.get(i).proName);
            NlstAdapter.add(String.valueOf(Cart.myCart.get(i).price));
            sum += Cart.myCart.get(i).price;
        }

        TextView summation = (TextView) findViewById(R.id.textView5);

        summation.setText(String.valueOf(sum));

        Button confirm = (Button) findViewById(R.id.button11);

        confirm.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                getLocation();
                for (int i = 0; i < Cart.myCart.size(); i++) {
                    String date = java.time.LocalDate.now().toString();
                    DB.insertOrder(new Order(0, date, Cart.custID, cur));
                    Query query = new Query();
                    query.columnNames = new String[] {"ordID"};
                    query.where = "ordDate=? and custID=? and address=?";
                    query.args = new String[]{date, String.valueOf(Cart.custID), cur};
                    Cursor cursor = DB.fetchDetails(query);
                    DB.insertDetails(new OrderDetail(Integer.parseInt(cursor.getString(0)), Cart.myCart.get(i).proID, Cart.myCart.get(i).quantity));
                }
                Cart.myCart.clear();
                finish();
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
        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, CartSum.this);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            Geocoder geocoder = new Geocoder(CartSum.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            String address = addresses.get(0).getAddressLine(0);
            cur = address;
            Toast.makeText(this, address, Toast.LENGTH_SHORT).show();

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}