package com.atharva.grocerystore.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.atharva.grocerystore.R;
import com.atharva.grocerystore.data.RuntimeData;
import com.atharva.grocerystore.data.model.Order;
import com.atharva.grocerystore.data.model.Product;
import com.atharva.grocerystore.ui.utils.ProductView;

import java.util.Map;

public class CartActivity extends AppCompatActivity {

    LinearLayout productsViewList;
    TextView priceCart;
    int price = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        productsViewList = findViewById(R.id.productsViewListCart);
        priceCart = findViewById(R.id.priceCart);
        populateCart();
    }

    @SuppressLint("DefaultLocale")
    private void populateCart() {
        productsViewList.removeAllViews();
        if (RuntimeData.cart == null) {
            priceCart.setText(String.format("Total Price: %d", 0));
            return;
        }
        for (Map.Entry<Product, Integer> entry : RuntimeData.cart.entrySet()) {
            Product p = entry.getKey();
            Integer i = entry.getValue();
            price += p.getPrice() * i;

            CardView card = ProductView.getCartScreenProductCard(
                    getApplicationContext(), getResources(), p.getName(), p.getPhoto(), i);

            card.setOnClickListener(v -> {
                Intent intent = new Intent(getApplicationContext(), ProductActivity.class);
                intent.putExtra("name", p.getName());
                startActivity(intent);
            });

            productsViewList.addView(card);
        }

        priceCart.setText(String.format("Total Price: %d", price));
    }

    public void onProceed(View view) {
        createOrder();
        startActivity(new Intent(getApplicationContext(), AddressActivity.class));
    }

    private void createOrder() {
        RuntimeData.tempOrder = new Order(RuntimeData.cart, price, RuntimeData.userData.getAddress());
    }
}
