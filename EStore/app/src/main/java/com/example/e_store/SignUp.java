package com.example.e_store;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.RadioButton;

import java.io.PrintWriter;

public class SignUp extends AppCompatActivity {
    boolean isMale = false;
    String date = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        EditText realName = findViewById(R.id.editTextTextPersonName4);
        EditText username = findViewById(R.id.editTextTextPersonName9);
        EditText password = findViewById(R.id.editTextTextPersonName3);
        RadioButton male = findViewById(R.id.radioButton4);
        RadioButton female = findViewById(R.id.radioButton5);
        EditText job = findViewById(R.id.editTextTextPersonName6);

        isMale = false;
        date = getIntent().getStringExtra("date");;

        final DatabaseManager DB = new DatabaseManager(this);

        Button register = findViewById(R.id.button3);

        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMale = true;
            }
        });

        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMale = false;
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!realName.getText().toString().equals("") && !username.getText().toString().equals("")
                    && !password.getText().toString().equals("") && !job.getText().toString().equals("")
                    && !date.isEmpty()) {
                    Query query = new Query();
                    Customer customer = new Customer(0, realName.getText().toString(), username.getText().toString(), password.getText().toString(), date, job.getText().toString(), isMale);

                    query.columnNames = new String[]{"username"};
                    query.where = "username=?";
                    query.args = new String[]{username.getText().toString()};

                    Cursor cursor = null;

                    try {
                        cursor = DB.fetchCustomer(query);
                    }
                    catch (Exception ex) {
                        PrintWriter o = new PrintWriter(System.out);
                        o.println(ex.getMessage());
                    }

                    if (cursor == null || cursor.isAfterLast()) {
                        DB.insertCustomer(customer);

                        query.columnNames = new String[]{"custID"};
                        query.where = "username=?";
                        query.args = new String[]{username.getText().toString()};

                        cursor = DB.fetchCustomer(query);

                        Cart.custID = customer.custID = Integer.parseInt(cursor.getString(0));
                        Intent login = new Intent(SignUp.this, Login.class);
                        startActivity(login);
                    }
                }
            }
        });

        Button back = (Button) findViewById(R.id.button23);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}