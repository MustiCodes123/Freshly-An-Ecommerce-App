package com.example.freshlyanecommerceapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.navigation.NavigationView;
import java.util.ArrayList;
import java.util.List;

public class CustomerHomeActivity extends AppCompatActivity {
    private EditText searchBar;
    private RecyclerView recyclerViewProducts;
    private ProductAdapter productAdapter;
    private AppDatabase db;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageButton profileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);

        // Initialize Views
        searchBar = findViewById(R.id.searchBar);
        recyclerViewProducts = findViewById(R.id.recyclerViewProducts);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        profileButton = findViewById(R.id.profileButton);

        // Initialize Room Database
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "freshly-database").build();

        // Initialize ProductAdapter with an empty list and set it on the RecyclerView
        productAdapter = new ProductAdapter(this, new ArrayList<>());
        recyclerViewProducts.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewProducts.setAdapter(productAdapter);

        // Load all products initially
        loadProducts(null);

        // Set up Search Bar functionality
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchProducts(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Set up Navigation Drawer toggle and listener
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Navigation Drawer item selection
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.menu_vegetables) {
                loadProducts("Vegetables");
            } else if (id == R.id.menu_fruits) {
                loadProducts("Fruits");
            } else if (id == R.id.menu_dry_fruits) {
                loadProducts("Dry Fruits");
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true; // Return true to keep the selected item highlighted
        });

        // Profile Button to update customer info
        profileButton.setOnClickListener(v -> {
            Intent intent = new Intent(CustomerHomeActivity.this, UpdateProfileActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.cartButton).setOnClickListener(v -> {
            Intent intent = new Intent(CustomerHomeActivity.this, CartActivity.class);
            startActivity(intent);
        });
    }

    private void loadProducts(@Nullable String category) {
        new Thread(() -> {
            try {
                List<Product> productList = (category == null) ?
                        db.productDao().getAllProducts() :
                        db.productDao().getProductsByCategory(category);
                runOnUiThread(() -> productAdapter.updateData(productList));
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "Error loading products", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void searchProducts(String query) {
        new Thread(() -> {
            try {
                List<Product> productList = db.productDao().searchProducts(query);
                runOnUiThread(() -> productAdapter.updateData(productList));
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "Error searching products", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}
