package com.example.e_store;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ForgetPassword extends AppCompatActivity {
    final public String SharedPreference = "Store";
    final public String ID = "ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password);

        EditText username = (EditText) findViewById(R.id.editTextTextPersonName5);
        EditText password = (EditText) findViewById(R.id.editTextTextPersonName);
        Button apply = (Button) findViewById(R.id.button2);

        DatabaseManager DB = new DatabaseManager(this);

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query query = new Query();
                query.columnNames = new String[] {"custID", "custName", "username", "password", "gender", "birthday", "job"};
                query.where = "username=?";
                query.args = new String[] {username.getText().toString()};

                ContentValues cv = new ContentValues();
                cv.put("password", password.getText().toString());

                int n = DB.updateCustomer(query, cv);

                if (n != 0) {
                    Toast.makeText(getApplicationContext(), "Apply", Toast.LENGTH_LONG).show();
                    Intent signUp = new Intent(ForgetPassword.this, SignUp.class);
                    startActivity(signUp);
                }
            }
        });

        Button back = (Button) findViewById(R.id.button24);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}