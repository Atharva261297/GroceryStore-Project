package com.atharva.grocerystore.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.atharva.grocerystore.R;
import com.atharva.grocerystore.data.RuntimeData;
import com.atharva.grocerystore.data.model.Order;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BuyActivity extends AppCompatActivity {

    TextView priceBuy;
    TextView itemCountBuy;
    TextView deliveryDate;
    TextView addressBuy;

    @SuppressLint({"SetTextI18n", "SimpleDateFormat"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);
        priceBuy = findViewById(R.id.priceBuy);
        itemCountBuy = findViewById(R.id.itemCountBuy);
        deliveryDate = findViewById(R.id.deliveryDate);
        addressBuy = findViewById(R.id.addressBuy);

        priceBuy.setText("Total Price: " + RuntimeData.tempOrder.getPrice());
        itemCountBuy.setText("No of items: " + RuntimeData.tempOrder.getProducts().keySet().size());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, 3);
        deliveryDate.setText("Est Delivery: " + new SimpleDateFormat("dd-MM-yyyy").format(calendar.getTime()));
        addressBuy.setText("Address: " + RuntimeData.userData.getAddress());
    }

    public void onCancel(View view) {
        RuntimeData.tempOrder = null;
        startActivity(new Intent(getApplicationContext(), CartActivity.class));
        finish();
    }

    public void onConfirm(View view) {
        if (RuntimeData.userData.getOrders() == null) {
            List<Order> orders = new ArrayList<>();
            orders.add(RuntimeData.tempOrder);
            RuntimeData.userData.setOrders(orders);
        } else {
            RuntimeData.userData.getOrders().add(RuntimeData.tempOrder);
        }
        RuntimeData.tempOrder = null;
        RuntimeData.cart.clear();
        RuntimeData.userDb.setValue(RuntimeData.userData).addOnCompleteListener(task -> {
            Toast.makeText(getApplicationContext(), "Order has been successfully placed", Toast.LENGTH_LONG).show();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        });
    }
}
