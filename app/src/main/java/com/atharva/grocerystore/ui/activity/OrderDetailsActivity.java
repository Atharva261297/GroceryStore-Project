package com.atharva.grocerystore.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.atharva.grocerystore.R;
import com.atharva.grocerystore.data.RuntimeData;
import com.atharva.grocerystore.data.model.Order;
import com.atharva.grocerystore.ui.utils.ProductView;

import java.util.Objects;

public class OrderDetailsActivity extends AppCompatActivity {

    LinearLayout productsViewListOrderDetails;
    TextView priceOrderDetails;
    TextView paymentModeOrderDetails;
    TextView addressOrderDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        productsViewListOrderDetails = findViewById(R.id.productsViewListOrderDetails);
        priceOrderDetails = findViewById(R.id.priceOrderDetails);
        paymentModeOrderDetails = findViewById(R.id.paymentModeOrderDetails);
        addressOrderDetails = findViewById(R.id.addressOrderDetails);
        showProducts();
    }

    @SuppressLint("SetTextI18n")
    private void showProducts() {
        productsViewListOrderDetails.removeAllViews();
        Order order = RuntimeData.userData.getOrders().get(getIntent().getIntExtra("order", 0));
        order.getProducts().forEach( (p,i) -> {
            CardView card = ProductView.getCartScreenProductCard(
                    getApplicationContext(),
                    getResources(),
                    p,
                    Objects.requireNonNull(RuntimeData.products.get(p)).getPhoto(),
                    i
            );

            card.setOnClickListener(v -> {
                Intent intent = new Intent(getApplicationContext(), ProductActivity.class);
                intent.putExtra("name", p);
                startActivity(intent);
            });

            productsViewListOrderDetails.addView(card);
        });

        priceOrderDetails.setText("Total Price: " + order.getPrice());
        paymentModeOrderDetails
                .setText("Payment Mode: " +
                        (order.getModeOfPayment() == null || order.getModeOfPayment().isEmpty() ?
                                "Cash/Card on Delivery": order.getModeOfPayment())
                );
        addressOrderDetails.setText("Address: " + order.getAddress());
    }
}
