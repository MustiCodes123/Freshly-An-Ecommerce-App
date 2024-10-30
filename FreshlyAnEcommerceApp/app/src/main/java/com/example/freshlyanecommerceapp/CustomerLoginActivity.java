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


public class CustomerLoginActivity extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private Button btnLogin;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_customer);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "freshly-database").build();

        btnLogin.setOnClickListener(view -> {
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();

            new Thread(() -> {
                Customer customer = db.customerDao().loginCustomer(email, password);
                runOnUiThread(() -> {
                    if (customer != null) {
                        // Successful login, save session
                        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("userId", customer.c_id);
                        editor.putString("userType", "customer");
                        editor.apply();

                        Toast.makeText(this, "Customer logged in", Toast.LENGTH_SHORT).show();

                        // Navigate to customer home screen
                        startActivity(new Intent(this, CustomerHomeActivity.class));
                    } else {
                        Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    }
                });
            }).start();
        });
    }

    public void openSignUpPage(View view) {
        Intent intent = new Intent(this, CustomerSignUpActivity.class);
        startActivity(intent);
    }
}