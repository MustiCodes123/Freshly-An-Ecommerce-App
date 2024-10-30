package com.example.freshlyanecommerceapp;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import java.util.Arrays;
import java.util.List;
import android.widget.ArrayAdapter;


public class VendorHomeActivity extends AppCompatActivity {
    private EditText etTitle, etDescription, etPrice;
    private Spinner spinnerCategory;
    private Button btnUploadProduct;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_home);


        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        etPrice = findViewById(R.id.etPrice);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        btnUploadProduct = findViewById(R.id.btnUploadProduct);


        // List of categories
        List<String> categories = Arrays.asList("Dry Fruits", "Vegetables", "Fruits");

        // Set up ArrayAdapter with the categories list
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                categories
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "freshly-database").build();

        btnUploadProduct.setOnClickListener(view -> {
            String title = etTitle.getText().toString();
            String description = etDescription.getText().toString();
            double price = Double.parseDouble(etPrice.getText().toString());
            String category = spinnerCategory.getSelectedItem().toString();

            new Thread(() -> {
                Product product = new Product();
                product.title = title;
                product.description = description;
                product.price = price;
                product.category = category;

                db.productDao().insertProduct(product);
                runOnUiThread(() -> Toast.makeText(this, "Product uploaded", Toast.LENGTH_SHORT).show());
            }).start();
        });
    }
}