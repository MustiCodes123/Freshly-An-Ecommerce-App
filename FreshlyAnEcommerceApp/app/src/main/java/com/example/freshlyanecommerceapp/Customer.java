package com.example.freshlyanecommerceapp;
import android.media.Image;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "customers")
public class Customer {
    @PrimaryKey(autoGenerate = true)
    public int c_id;
    public String name;
    public String email;
    public String password;
    public String image;
    public String gender;
}