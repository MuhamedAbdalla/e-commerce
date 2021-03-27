package com.example.e_store;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class Voice extends AppCompatActivity {
    private final int VOICE_CODE = 1;
    ListView lst_product;
    DatabaseManager DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voice);

        DB = new DatabaseManager(this);

        lst_product = (ListView) findViewById(R.id.lst_voice);
        Button talk = (Button) findViewById(R.id.button13);

        talk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                startActivityForResult(intent, VOICE_CODE);
            }
        });

        Button back = (Button) findViewById(R.id.button17);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VOICE_CODE && resultCode == this.RESULT_OK) {
            ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            ArrayAdapter<String> proAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
            lst_product.setAdapter(proAdapter);

            proAdapter.clear();
            DB = new DatabaseManager(this);

            Query query = new Query();
            query.columnNames = new String[]{"proID", "proName", "price", "quantity", "catID", "image"};
            query.where = "proName=?";
            query.args = new String[]{text.get(0)};

            Cursor cursor = DB.fetchProduct(query);

            while (cursor != null && !cursor.isAfterLast()) {
                proAdapter.add(cursor.getString(1));
                cursor.moveToNext();
            }
        }
    }
}