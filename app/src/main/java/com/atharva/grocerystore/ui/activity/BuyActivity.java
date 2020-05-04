package com.atharva.grocerystore.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
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
    RadioButton cashDelivery;
    RadioButton googlePay;

    private static final int TEZ_REQUEST_CODE = 123;

    private static final String GOOGLE_TEZ_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";

    @SuppressLint({"SetTextI18n", "SimpleDateFormat"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);
        priceBuy = findViewById(R.id.priceBuy);
        itemCountBuy = findViewById(R.id.itemCountBuy);
        deliveryDate = findViewById(R.id.deliveryDate);
        addressBuy = findViewById(R.id.addressBuy);
        cashDelivery = findViewById(R.id.cashRadioBtn);
        googlePay = findViewById(R.id.gPayRadioBtn);

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
        if (googlePay.isChecked()) {
            RuntimeData.tempOrder.setModeOfPayment("Google Pay (Tez)");
            startGooglePayIntent();
        } else {
            RuntimeData.tempOrder.setModeOfPayment("Cash/Card on Delivery");
            completeOrder();
        }
    }

    private void startGooglePayIntent() {
        Uri uri =
                new Uri.Builder()
                        .scheme("upi")
                        .authority("pay")
                        .appendQueryParameter("pa", "test@axisbank")
                        .appendQueryParameter("pn", "Test Merchant")
                        .appendQueryParameter("mc", "1234")
                        .appendQueryParameter("tr", "123456789")
                        .appendQueryParameter("tn", "test transaction note")
                        .appendQueryParameter("am", RuntimeData.tempOrder.getPrice() + "")
                        .appendQueryParameter("cu", "INR")
                        .appendQueryParameter("url", "https://test.merchant.website")
                        .build();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        intent.setPackage(GOOGLE_TEZ_PACKAGE_NAME);
        startActivityForResult(intent, TEZ_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TEZ_REQUEST_CODE) {
            // Process based on the data in response.
            String status = data.getStringExtra("Status");
            if (status != null && !status.equals("FAILURE")) {
                completeOrder();
                Toast.makeText(getApplicationContext(), "Order Placed", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Something went wrong\nPlease try again later", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void completeOrder() {
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
