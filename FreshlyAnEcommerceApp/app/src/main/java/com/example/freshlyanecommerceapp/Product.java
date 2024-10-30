package com.example.freshlyanecommerceapp;
import androidx.room.PrimaryKey;
import java.io.Serializable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "products")
public class Product {
    @PrimaryKey(autoGenerate = true)
    public int p_id;
    public String title;
    public String description;
    public String image;
    public int category_id;
    public String category;
    public int vendor_id;
    public double price;
    public int quantity;

    public double getPrice() {
        return price;
    }
}
