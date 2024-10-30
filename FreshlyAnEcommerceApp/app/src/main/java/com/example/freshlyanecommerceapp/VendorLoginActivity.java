package com.example.freshlyanecommerceapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

public class VendorLoginActivity extends AppCompatActivity {
    private EditText etUsername, etPassword;
    private Button btnLogin;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_vendor);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "freshly-database").build();

        btnLogin.setOnClickListener(view -> {
            String username = etUsername.getText().toString();
            String password = etPassword.getText().toString();

            new Thread(() -> {
                Vendor vendor = db.vendorDao().loginVendor(username, password);
                runOnUiThread(() -> {
                    if (vendor != null) {

                        // Save session in Shared Preferences

                        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("userId", vendor.v_id);
                        editor.putString("userType", "vendor");
                        editor.apply();

                        // Successful login
                        Toast.makeText(this, "Vendor logged in", Toast.LENGTH_SHORT).show();

                        // Navigate to vendor home screen
                        startActivity(new Intent(this, VendorHomeActivity.class));

                    } else {
                        Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    }
                });
            }).start();
        });

    }

    public void openSignUpPage(View view) {
        Intent intent = new Intent(this, VendorSignUpActivity.class);
        startActivity(intent);
    }



}