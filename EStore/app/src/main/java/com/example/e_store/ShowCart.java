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
    DatabaseManager DB = new DatabaseManager(this);

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
                quantity.setText(String.valueOf(Cart.myCart.get(position).quantity));
                Query query = new Query();
                query.columnNames = new String[] {"proID", "proName", "price", "quantity", "catID", "image"};
                query.where = "proID=? and catID=?";
                query.args = new String[]{String.valueOf(Cart.myCart.get(position).proID), String.valueOf(Cart.myCart.get(position).catID)};

                Cursor cursor = DB.fetchProduct(query);

                while (cursor != null && !cursor.isAfterLast()) {
                    cur = new Product(Integer.parseInt(cursor.getString(0)), cursor.getString(1), Integer.parseInt(cursor.getString(4)),
                            Integer.parseInt(cursor.getString(3)), Float.parseFloat(cursor.getString(2)), cursor.getString(5));
                    cursor.moveToNext();
                }
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
                    Product newProduct = Cart.myCart.get(pos);
                    Cart.myCart.remove(pos);
                    if (count != 0) {
                        newProduct.quantity = count;
                        Cart.myCart.add(newProduct);
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