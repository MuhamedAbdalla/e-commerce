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
import android.widget.ListView;

import java.io.PrintWriter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    final public String SharedPreference = "MSSStore";
    final public String ID = "ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String id = get();

        if (id.equals("") && Cart.custID == -1) {
            Intent login = new Intent(MainActivity.this, Login.class);
            startActivity(login);
        }
        else {
            DatabaseManager DB = new DatabaseManager(this);

            if (!id.equals("")) {
                Cart.custID = Integer.parseInt(id);
            }

            Query query = new Query();
            query.columnNames = new String[]{"custID", "custName", "username", "password", "gender", "birthday", "job"};
            query.where = "custID=?";
            query.args = new String[]{String.valueOf(Cart.custID)};

            Cursor cursor = DB.fetchCustomer(query);

            Customer customer = new Customer(Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1), cursor.getString(2), cursor.getString(3),
                    cursor.getString(5), cursor.getString(6), (cursor.getString(4).equals("1")));

            ArrayList<Product> products = new ArrayList<>();
            ArrayList<Category> categories = new ArrayList<>();

            ListView lst_categories = (ListView) findViewById(R.id.list_categories);
            ListView lst_products = (ListView) findViewById(R.id.list_products);

            final ArrayAdapter<String> ClstAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
            final ArrayAdapter<String> PlstAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

            lst_categories.setAdapter(ClstAdapter);
            lst_products.setAdapter(PlstAdapter);


            query.columnNames = new String[]{"proID", "proName", "price", "quantity", "catID", "image"};
            query.where = "";
            query.args = new String[]{};

            cursor = DB.fetchProduct(query);

            while (cursor != null && !cursor.isAfterLast()) {
                products.add(new Product(Integer.parseInt(cursor.getString(0)), cursor.getString(1), Integer.parseInt(cursor.getString(4)),
                        Integer.parseInt(cursor.getString(3)), Float.parseFloat(cursor.getString(2)), cursor.getString(5)));
                cursor.moveToNext();
            }

            query.columnNames = new String[]{"catID", "catName"};
            query.where = "";
            query.args = new String[]{};

            cursor = DB.fetchCategory(query);

            categories.add(new Category(0, "All"));

            while (cursor != null && !cursor.isAfterLast()) {
                categories.add(new Category(Integer.parseInt(cursor.getString(0)), cursor.getString(1)));
                cursor.moveToNext();
            }

            ClstAdapter.clear();
            PlstAdapter.clear();

            for (int i = 0; i < products.size(); i++) PlstAdapter.add(products.get(i).proName);
            for (int i = 0; i < categories.size(); i++) ClstAdapter.add(categories.get(i).catName);

            lst_categories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Query filter = new Query();

                    if (!categories.get(position).catName.equals("All")) {
                        filter.columnNames = new String[]{"proID", "proName", "price", "quantity", "catID", "image"};
                        filter.where = "catID=?";
                        filter.args = new String[]{String.valueOf(categories.get(position).catID)};
                    } else {
                        filter.columnNames = new String[]{"proID", "proName", "price", "quantity", "catID", "image"};
                        filter.where = "";
                        filter.args = new String[]{};
                    }

                    Cursor lst = DB.fetchProduct(filter);

                    products.clear();

                    while (lst != null && !lst.isAfterLast()) {
                        products.add(new Product(Integer.parseInt(lst.getString(0)), lst.getString(1), Integer.parseInt(lst.getString(4)),
                                Integer.parseInt(lst.getString(3)), Float.parseFloat(lst.getString(2)),
                                lst.getString(5)));
                        lst.moveToNext();
                    }

                    PlstAdapter.clear();
                    for (int i = 0; i < products.size(); i++)
                        PlstAdapter.add(products.get(i).proName);
                }
            });

            lst_products.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent details = new Intent(MainActivity.this, ProductDetails.class);
                    details.putExtra("proID", String.valueOf(products.get(position).proID));
                    details.putExtra("catID", String.valueOf(products.get(position).catID));
                    details.putExtra("custID", String.valueOf(id));
                    startActivity(details);
                }
            });

            Button submit = (Button) findViewById(R.id.button7);
            Button show = (Button) findViewById(R.id.button6);
            Button text = (Button) findViewById(R.id.button5);
            Button camera = (Button) findViewById(R.id.button15);
            Button voice = (Button) findViewById(R.id.button14);

            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent page = new Intent(MainActivity.this, CartSum.class);
                    startActivity(page);
                }
            });

            show.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent page = new Intent(MainActivity.this, ShowCart.class);
                    startActivity(page);
                }
            });

            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent page = new Intent(MainActivity.this, text.class);
                    startActivity(page);
                }
            });

            camera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent page = new Intent(MainActivity.this, Camera.class);
                    startActivity(page);
                }
            });

            voice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent page = new Intent(MainActivity.this, Voice.class);
                    startActivity(page);
                }
            });
        }
    }

    public String get() {
        SharedPreferences preferences = getSharedPreferences(SharedPreference, MODE_PRIVATE);
        return preferences.getString(ID, "");
    }

    public void clear() {
        SharedPreferences preferences = getSharedPreferences(SharedPreference, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(ID, "");
        editor.apply();
    }
}