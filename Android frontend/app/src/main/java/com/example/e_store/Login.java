package com.example.e_store;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class Login extends AppCompatActivity {
    final public String SharedPreference = "Store";
    final public String ID = "ID";
    boolean remeberMe = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        DatabaseManager DB = new DatabaseManager(this);

        EditText username = (EditText) findViewById(R.id.editTextTextPersonName2);
        EditText password = (EditText) findViewById(R.id.editTextTextPassword);
        CheckBox remember = (CheckBox) findViewById(R.id.checkBox);

        remember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remeberMe = remember.isChecked();
            }
        });

        Button login = (Button) findViewById(R.id.button);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!username.getText().toString().equals("") && !password.getText().toString().equals("")) {
                    Query query = new Query();
                    query.columnNames = new String[] {"custID", "custName", "username", "password", "gender", "birthday", "job"};
                    query.where = "username=? and password=?";
                    query.args = new String[] {username.getText().toString(), password.getText().toString()};

                    Cursor cursor = DB.fetchCustomer(query);
                    if (cursor != null && !cursor.isAfterLast()) {
                        if (remeberMe) {
                            insert(Integer.parseInt(cursor.getString(0)));
                        }
                        Cart.custID = Integer.parseInt(cursor.getString(0));
                        Intent main = new Intent(Login.this, MainActivity.class);
                        startActivity(main);
                    }
                }
            }
        });

        Button register = (Button) findViewById(R.id.button22);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent main = new Intent(Login.this, SignUp.class);
                startActivity(main);
            }
        });

        Button forget = (Button) findViewById(R.id.button21);

        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent main = new Intent(Login.this, ForgetPassword.class);
                startActivity(main);
            }
        });
    }

    public void insert(int custID) {
        SharedPreferences preferences = getSharedPreferences(SharedPreference, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(ID, String.valueOf(custID));
        editor.apply();
    }

    public String get() {
        SharedPreferences preferences = getSharedPreferences(SharedPreference, MODE_PRIVATE);
        return preferences.getString(ID, "");
    }

    public void clear() {
        SharedPreferences preferences = getSharedPreferences(SharedPreference, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
    }
}