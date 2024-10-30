package com.example.freshlyanecommerceapp;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface ProductDao{
    @Insert
    void insertProduct(Product product);

    @Query("SELECT * FROM products WHERE category LIKE '%' || :query || '%' OR  title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%'")
    List<Product> searchProducts(String query);

    @Query("SELECT * FROM products")
    List<Product> getAllProducts();

    @Query("SELECT * FROM products WHERE category = :category")
    List<Product> getProductsByCategory(String category);

    @Query("SELECT * FROM products WHERE p_id = :productId")
    Product getProductById(int productId);



}