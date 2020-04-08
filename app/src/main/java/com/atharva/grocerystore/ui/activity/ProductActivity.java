package com.atharva.grocerystore.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.atharva.grocerystore.R;
import com.atharva.grocerystore.data.RuntimeData;
import com.atharva.grocerystore.data.model.Cart;
import com.atharva.grocerystore.data.model.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class ProductActivity extends AppCompatActivity {

    ImageView productPhoto;
    TextView productName;
    TextView productPrice;
    TextView productQuantity;
    TextView productQuantitySelected;

    private String name;
    private Product product;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        productPhoto = findViewById(R.id.productPhoto);
        productName = findViewById(R.id.productName);
        productPrice = findViewById(R.id.productPrice);
        productQuantity = findViewById(R.id.productQuantity);
        productQuantitySelected = findViewById(R.id.productQuantitySelected);

        name = getIntent().getStringExtra("name");

        product = RuntimeData.products.get(name);
        if (product != null) {
            Picasso.get().load(product.getPhoto()).into(productPhoto);
            productName.setText(product.getName());
            productPrice.setText(String.format("Price: %d", product.getPrice()));
            productQuantity.setText(String.format("Price: %s", product.getQuantity()));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cart:
                startActivity(new Intent(getApplicationContext(), CartActivity.class));
                return true;
            case R.id.account:
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                return true;
            case R.id.orders:
                startActivity(new Intent(getApplicationContext(), OrdersActivity.class));
                return true;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressLint("DefaultLocale")
    public void onReduce(View view) {
        int i = Integer.parseInt(productQuantitySelected.getText().toString());
        i -= 1;
        productQuantitySelected.setText(String.format("%d", i));
    }

    @SuppressLint("DefaultLocale")
    public void onIncrease(View view) {
        int i = Integer.parseInt(productQuantitySelected.getText().toString());
        i += 1;
        productQuantitySelected.setText(String.format("%d", i));
    }

    @SuppressLint("DefaultLocale")
    public void onAddToCart(View view) {
        int i = Integer.parseInt(productQuantitySelected.getText().toString());
        if (RuntimeData.cart == null) {
            RuntimeData.cart = new Cart();
            RuntimeData.cart.put(product, i);
        } else {
            if (RuntimeData.cart.containsKey(product)) {
                Integer count = RuntimeData.cart.remove(product);
                if (count != null) {
                    RuntimeData.cart.put(product, count + i);
                } else {
                    RuntimeData.cart.put(product, i);
                }
            } else {
                RuntimeData.cart.put(product, i);
            }
        }
        productQuantitySelected.setText(String.format("%d", 0));
        Toast.makeText(getApplicationContext(), "Added to cart", Toast.LENGTH_SHORT).show();
    }
}
