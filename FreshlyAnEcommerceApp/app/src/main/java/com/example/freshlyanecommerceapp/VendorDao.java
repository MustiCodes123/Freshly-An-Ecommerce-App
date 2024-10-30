package com.example.freshlyanecommerceapp;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface VendorDao{

    @Insert
    void insertVendor(Vendor vendor);

    @Query("SELECT * FROM vendors WHERE username = :username AND password = :password")
    Vendor loginVendor(String username , String password);

    @Query("SELECT * FROM vendors WHERE username = :username LIMIT 1")
    Vendor getVendorByUsername(String username);
}