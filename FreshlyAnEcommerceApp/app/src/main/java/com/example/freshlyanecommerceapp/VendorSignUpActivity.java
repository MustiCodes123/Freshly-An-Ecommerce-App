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

public class VendorSignUpActivity extends AppCompatActivity {
    private EditText etUsername, etPassword, etAddress, etPhone;
    private Button btnChooseProfilePicture, btnSignUp;
    private Uri profilePictureUri;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_sign_up);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etAddress = findViewById(R.id.etAddress);
        etPhone = findViewById(R.id.etPhone);
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
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        String address = etAddress.getText().toString();
        String phone = etPhone.getText().toString();

        if (username.isEmpty() || password.isEmpty() || address.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            Vendor existingVendor = db.vendorDao().getVendorByUsername(username);
            if (existingVendor != null) {
                runOnUiThread(() -> Toast.makeText(this, "Username already registered", Toast.LENGTH_SHORT).show());
            } else {
                Vendor vendor = new Vendor();
                vendor.username = username;
                vendor.password = password;
                vendor.address = address;
                vendor.phone = phone;
                vendor.image = profilePictureUri != null ? profilePictureUri.toString() : "";

                db.vendorDao().insertVendor(vendor);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Sign up successful", Toast.LENGTH_SHORT).show();
                    finish(); // Close the sign-up page and return to login
                });
            }
        }).start();
    }
}
