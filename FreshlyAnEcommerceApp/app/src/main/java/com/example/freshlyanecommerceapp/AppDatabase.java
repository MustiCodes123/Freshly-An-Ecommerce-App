package com.example.freshlyanecommerceapp;
import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Product.class, Customer.class, Category.class, Vendor.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ProductDao productDao();
    public abstract CustomerDao customerDao();
    public abstract VendorDao vendorDao();
}