package com.example.freshlyanecommerceapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

public class UpdateProfileActivity extends AppCompatActivity {
    private EditText etName, etEmail, etPassword, etGender;
    private ImageView ivProfilePicture;
    private Button btnUpdate , btnDelete;
    private AppDatabase db;
    private Customer customer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        // Initialize Views
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etGender = findViewById(R.id.etGender);
        ivProfilePicture = findViewById(R.id.ivProfilePicture);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);


        // Initialize Room Database
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "freshly-database").build();

        // Retrieve user ID from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId", -1);

        if (userId != -1) {
            loadUserData(userId);
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Set up Update button functionality
        btnUpdate.setOnClickListener(view -> updateUserProfile());
        btnDelete.setOnClickListener(view -> deleteUserProfile());

    }


    private void deleteUserProfile() {
        if (customer == null) {
            Toast.makeText(this, "Error: Customer not loaded", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            // Delete the customer from the database
            db.customerDao().deleteCustomer(customer);

            // Clear the user session from SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("userId");  // Remove the saved user ID
            editor.apply();

            runOnUiThread(() -> {
                Toast.makeText(this, "Profile deleted successfully", Toast.LENGTH_SHORT).show();

                // Redirect to login page
                Intent intent = new Intent(UpdateProfileActivity.this, CustomerLoginActivity.class);
                startActivity(intent);
                finish();  // Close the UpdateProfileActivity
            });
        }).start();
    }



    private void loadUserData(int userId) {
        new Thread(() -> {
            customer = db.customerDao().getCustomerById(userId);

            if (customer != null) {
                runOnUiThread(() -> {
                    etName.setText(customer.name);
                    etEmail.setText(customer.email);
                    etPassword.setText(customer.password);
                    etGender.setText(customer.gender);

                    // Check if the image URI is valid and set it on the ImageView
                    if (customer.image != null && !customer.image.isEmpty()) {
                        try {
                            Uri imageUri = Uri.parse(customer.image);
                            ivProfilePicture.setImageURI(imageUri);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(this, "Error loading profile picture", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).start();
    }


    private void updateUserProfile() {
        // Retrieve updated data
        String name = etName.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String gender = etGender.getText().toString();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || gender.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update customer details
        customer.name = name;
        customer.email = email;
        customer.password = password;
        customer.gender = gender;

        new Thread(() -> {
            db.customerDao().updateCustomer(customer);
            runOnUiThread(() -> {
                Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                finish(); // Close the activity after updating
            });
        }).start();
    }
}
