package com.example.e_store;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class text extends AppCompatActivity {
    DatabaseManager DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text);


        ListView lst_product = (ListView) findViewById(R.id.lst_text);
        EditText text = (EditText) findViewById(R.id.editTextTextPersonName14);
        Button search = (Button) findViewById(R.id.button16);


        ArrayAdapter<String> proAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        lst_product.setAdapter(proAdapter);

        DB = new DatabaseManager(this);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proAdapter.clear();

                Query query = new Query();
                query.columnNames = new String[]{"proID", "proName", "price", "quantity", "catID", "image"};
                query.where = "proName=?";
                query.args = new String[]{text.getText().toString()};

                Cursor cursor = DB.fetchProduct(query);

                while (cursor != null && !cursor.isAfterLast()) {
                    proAdapter.add(cursor.getString(1));
                    cursor.moveToNext();
                }
            }
        });

        Button back = (Button) findViewById(R.id.button20);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}