package com.example.e_store;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class ShowCart extends AppCompatActivity {
    Product cur;
    int pos = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_cart);

        EditText name = (EditText) findViewById(R.id.editTextTextPersonName12);
        EditText price = (EditText) findViewById(R.id.editTextTextPersonName13);
        EditText quantity = (EditText) findViewById(R.id.editTextTextPersonName11);

        ListView lst_products = (ListView)findViewById(R.id.lst_product);
        final ArrayAdapter<String> lstAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        lst_products.setAdapter(lstAdapter);

        for (int i = 0; i < Cart.myCart.size(); i++) lstAdapter.add(Cart.myCart.get(i).proName);

        lst_products.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                name.setText(Cart.myCart.get(position).proName);
                price.setText(String.valueOf(Cart.myCart.get(position).price));
                quantity.setText(Cart.myCart.get(position).quantity);
                cur = new Product(Cart.myCart.get(position).proID, Cart.myCart.get(position).proName,
                        Cart.myCart.get(position).catID, Cart.myCart.get(position).quantity, Cart.myCart.get(position).price,
                        Cart.myCart.get(position).image);
                pos = position;
                name.setEnabled(false);
                price.setEnabled(false);
            }
        });

        Button update = (Button) findViewById(R.id.button10);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(quantity.getText().toString());
                if (count >= 0 && cur.quantity >= count && pos != -1) {
                    Cart.myCart.remove(pos);
                    if (count != 0) {
                        Cart.myCart.add(cur);
                    }
                    lstAdapter.clear();
                    for (int i = 0; i < Cart.myCart.size(); i++) lstAdapter.add(Cart.myCart.get(i).proName);
                    finish();
                }
            }
        });

        Button back = (Button) findViewById(R.id.button26);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}