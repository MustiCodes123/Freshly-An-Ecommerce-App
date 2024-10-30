package com.example.freshlyanecommerceapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface CustomerDao {

    @Insert
    void insertCustomer(Customer customer);

    @Query("SELECT * FROM customers WHERE email = :email AND password = :password")
    Customer loginCustomer(String email, String password);

    @Query("SELECT * FROM customers WHERE email = :email LIMIT 1")
    Customer getCustomerByEmail(String email);

    @Query("SELECT * FROM customers WHERE c_id = :userId LIMIT 1")
    Customer getCustomerById(int userId);

    @Update
    void updateCustomer(Customer customer);

    @Delete
    void deleteCustomer(Customer customer);
}
