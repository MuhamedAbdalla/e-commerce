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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ProductDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_details);

        final DatabaseManager DB = new DatabaseManager(this);

        String proID = getIntent().getStringExtra("proID");
        String catID = getIntent().getStringExtra("catID");
        String custID = getIntent().getStringExtra("custID");

        Query query = new Query();
        query.columnNames = new String[] {"proID", "proName", "price", "quantity", "catID", "image"};
        query.where = "proID=? and catID=?";
        query.args = new String[]{proID, catID};

        Cursor cursor = DB.fetchProduct(query);

        EditText proName = (EditText) findViewById(R.id.editTextTextPersonName8);
        EditText price = (EditText) findViewById(R.id.editTextTextPersonName10);
        EditText quantity = (EditText) findViewById(R.id.editTextTextPersonName7);
        TextView count = (TextView) findViewById(R.id.textView17);

        if (cursor != null && !cursor.isAfterLast()) {
            proName.setText(cursor.getString(1));
            price.setText(cursor.getString(2));
            quantity.setText(cursor.getString(3));
            count.setText("0");
            proName.setEnabled(false);
            price.setEnabled(false);
            quantity.setEnabled(false);
        }

        Button add = (Button) findViewById(R.id.button4);
        Button plus = (Button) findViewById(R.id.button8);
        Button miens = (Button) findViewById(R.id.button9);

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int c = Integer.parseInt(count.getText().toString());
                c++;
                c = Math.min(c, Integer.parseInt(quantity.getText().toString()));
                count.setText(String.valueOf(c));
            }
        });

        miens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int c = Integer.parseInt(count.getText().toString());
                c--;
                c = Math.max(c, 0);
                count.setText(String.valueOf(c));
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(count.getText().toString()) > 0) {
                    Cart.myCart.add(new Product(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                            Integer.parseInt(cursor.getString(4)), Integer.parseInt(count.getText().toString()),
                            Float.parseFloat(cursor.getString(2)), cursor.getString(5)));
                    Cart.custID = Integer.parseInt(custID);
                    Toast.makeText(getApplicationContext(), "Added!!", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });

        Button back = (Button) findViewById(R.id.button25);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}