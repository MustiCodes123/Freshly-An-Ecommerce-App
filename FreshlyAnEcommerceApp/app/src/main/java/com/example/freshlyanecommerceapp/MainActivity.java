package com.example.freshlyanecommerceapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

public class MainActivity extends AppCompatActivity {

    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "freshly-database").build();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        Button loginVendorButton = findViewById(R.id.btnLoginVendor);
        Button loginCustomerButton = findViewById(R.id.btnLoginCustomer);

        loginVendorButton.setOnClickListener(view -> {
            Intent vendorIntent = new Intent(MainActivity.this, VendorLoginActivity.class);
            startActivity(vendorIntent);
        });

        loginCustomerButton.setOnClickListener(view -> {
            Intent customerIntent = new Intent(MainActivity.this, CustomerLoginActivity.class);
            startActivity(customerIntent);
        });
    }
}