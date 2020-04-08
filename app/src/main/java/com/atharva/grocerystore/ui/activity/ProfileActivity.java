package com.atharva.grocerystore.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.atharva.grocerystore.R;
import com.atharva.grocerystore.data.RuntimeData;

public class ProfileActivity extends AppCompatActivity {

    TextView email;
    TextView address;
    TextView name;
    TextView phone;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        email = findViewById(R.id.emailProfile);
        address = findViewById(R.id.addressProfile);
        name = findViewById(R.id.nameProfile);
        phone = findViewById(R.id.phoneProfile);

        email.setText("Name: " + RuntimeData.userData.getEmail());
        name.setText("Email: " + RuntimeData.userData.getName());
        phone.setText("Phone Number: " + RuntimeData.userData.getPhone());
        address.setText("Address: " + RuntimeData.userData.getAddress());
    }

    public void onChangePassword(View view) {

    }

    public void onMyOrders(View view) {
        startActivity(new Intent(getApplicationContext(), OrdersActivity.class));
    }
}
