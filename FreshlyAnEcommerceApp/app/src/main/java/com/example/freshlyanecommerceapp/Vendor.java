package com.example.freshlyanecommerceapp;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "vendors")
public class Vendor {
    @PrimaryKey(autoGenerate = true)
    public int v_id;
    public String username;
    public String password;
    public String image;
    public String phone;
    public String address;
}
