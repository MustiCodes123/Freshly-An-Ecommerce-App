package com.example.freshlyanecommerceapp;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    private RecyclerView recyclerViewCart;
    private CartAdapter cartAdapter;
    private TextView tvTotalCost;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        tvTotalCost = findViewById(R.id.tvTotalCost);
        recyclerViewCart = findViewById(R.id.recyclerViewCart);
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));

        List<Product> cartItems = getCartItems();
        cartAdapter = new CartAdapter(cartItems);
        recyclerViewCart.setAdapter(cartAdapter);


        // Set up the Back button
        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(view -> {
            // Go back to CustomerHomeActivity
            Intent intent = new Intent(CartActivity.this, CustomerHomeActivity.class);
            startActivity(intent);
            finish();
        });

        Button btnClearCart = findViewById(R.id.btnClearCart);
        btnClearCart.setOnClickListener(view -> {
            // Clear cart items and update the RecyclerView
            clearCartItems();
            cartAdapter.updateData(new ArrayList<>()); // Pass an empty list to clear the RecyclerView
            Toast.makeText(this, "Cart is now empty.", Toast.LENGTH_SHORT).show();
        });
        // Set up the Checkout button
        Button btnCheckout = findViewById(R.id.btnCheckout);
        btnCheckout.setOnClickListener(view -> {
            // Clear cart items and update the RecyclerView
            clearCartItems();
            cartAdapter.updateData(new ArrayList<>()); // Pass an empty list to clear the RecyclerView
            Toast.makeText(this, "Checkout successful. Cart is now empty.", Toast.LENGTH_SHORT).show();
        });
    }

    private List<Product> getCartItems() {
        SharedPreferences sharedPreferences = getSharedPreferences("CartPrefs", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("cartItems", null);
        Type type = new TypeToken<ArrayList<Product>>() {}.getType();
        return gson.fromJson(json, type);
    }

    private void clearCartItems() {
        SharedPreferences sharedPreferences = getSharedPreferences("CartPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("cartItems");  // Remove the cart items entry from SharedPreferences
        editor.apply();
    }

    private void updateTotalCost() {
        SharedPreferences sharedPreferences = getSharedPreferences("CartPrefs", MODE_PRIVATE);
       float totalCost = sharedPreferences.getFloat("totalCost", 0.0f);
        tvTotalCost.setText("Total: $" + String.format("%.2f", totalCost));
    }
}
