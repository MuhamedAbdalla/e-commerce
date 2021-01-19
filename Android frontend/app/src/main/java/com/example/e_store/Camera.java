package com.example.e_store;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class Camera extends AppCompatActivity {
    private final int CAMERA_REQUEST = 1;
    ListView lst_product;
    DatabaseManager DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera);

        lst_product = (ListView) findViewById(R.id.lst_cam);
        Button upload = (Button) findViewById(R.id.button12);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera, CAMERA_REQUEST);
            }
        });

        Button back = (Button) findViewById(R.id.button18);

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
        if (requestCode == CAMERA_REQUEST && resultCode == this.RESULT_OK) {
            Bitmap myImage = (Bitmap) data.getExtras().get("data");

            ArrayAdapter<String> proAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
            lst_product.setAdapter(proAdapter);

            proAdapter.clear();
            DB = new DatabaseManager(this);

            Query query = new Query();
            query.columnNames = new String[]{"proID", "proName", "price", "quantity", "catID", "image"};
            query.where = "image=?";
            query.args = new String[]{myImage.toString()};

            Cursor cursor = DB.fetchProduct(query);

            while (cursor != null && !cursor.isAfterLast()) {
                proAdapter.add(cursor.getString(1));
                cursor.moveToNext();
            }
        }
    }
}