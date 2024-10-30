package com.example.freshlyanecommerceapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.freshlyanecommerceapp.Product;
import com.example.freshlyanecommerceapp.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> productList;
    private Context context;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.title.setText(product.title);
        holder.price.setText("$" + product.price);

        holder.addToCartButton.setOnClickListener(view -> {
            addToCart(product);
            Toast.makeText(context, product.title + " added to cart", Toast.LENGTH_SHORT).show();
        });
    }



    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView title, price;
        Button addToCartButton;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            price = itemView.findViewById(R.id.price);
            addToCartButton = itemView.findViewById(R.id.addToCartButton);
        }
    }
    public void updateData(List<Product> newProductList) {
        productList.clear();
        productList.addAll(newProductList);
        notifyDataSetChanged();
    }
    private void addToCart(Product product) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("CartPrefs", Context.MODE_PRIVATE);
        Gson gson = new Gson();

        // Retrieve existing cart items and total cost
        String json = sharedPreferences.getString("cartItems", null);
        Type type = new TypeToken<ArrayList<Product>>() {}.getType();
        List<Product> cartItems = gson.fromJson(json, type);
        cartItems = cartItems != null ? cartItems : new ArrayList<>();

        double totalCost = sharedPreferences.getFloat("totalCost", 0.0f);

        // Add the product to the cart and update the total cost
        cartItems.add(product);
        totalCost += product.getPrice(); // Assuming getPrice() returns the product price

        // Save updated cart and total cost back to SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("cartItems", gson.toJson(cartItems));
        editor.putFloat("totalCost", (float) totalCost);
        editor.apply();
    }
}
