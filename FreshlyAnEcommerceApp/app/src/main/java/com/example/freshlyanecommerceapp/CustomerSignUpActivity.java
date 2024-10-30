package com.example.freshlyanecommerceapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import android.net.Uri;

public class CustomerSignUpActivity extends AppCompatActivity {
    private EditText etName, etEmail, etPassword, etGender;
    private Button btnChooseProfilePicture, btnSignUp;
    private Uri profilePictureUri;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_sign_up);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etGender = findViewById(R.id.etGender);
        btnChooseProfilePicture = findViewById(R.id.btnChooseProfilePicture);
        btnSignUp = findViewById(R.id.btnSignUp);

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "freshly-database").build();

        btnChooseProfilePicture.setOnClickListener(view -> chooseProfilePicture());
        btnSignUp.setOnClickListener(view -> signUp());
    }

    private void chooseProfilePicture() {
        // Launch gallery to choose a profile picture
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        profilePictureLauncher.launch(intent);
    }

    private final ActivityResultLauncher<Intent> profilePictureLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    profilePictureUri = result.getData().getData();
                    Toast.makeText(this, "Profile picture selected", Toast.LENGTH_SHORT).show();
                }
            }
    );

    private void signUp() {
        String name = etName.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String gender = etGender.getText().toString();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || gender.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            Customer existingCustomer = db.customerDao().getCustomerByEmail(email);
            if (existingCustomer != null) {
                runOnUiThread(() -> Toast.makeText(this, "Email already registered", Toast.LENGTH_SHORT).show());
            } else {
                Customer customer = new Customer();
                customer.name = name;
                customer.email = email;
                customer.password = password;
                customer.gender = gender;
                customer.image = profilePictureUri != null ? profilePictureUri.toString() : "";

                db.customerDao().insertCustomer(customer);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Sign up successful", Toast.LENGTH_SHORT).show();
                    finish(); // Close the sign-up page and return to login
                });
            }
        }).start();
    }
}
