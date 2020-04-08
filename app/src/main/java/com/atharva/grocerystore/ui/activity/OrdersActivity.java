package com.atharva.grocerystore.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.atharva.grocerystore.R;
import com.atharva.grocerystore.data.RuntimeData;
import com.atharva.grocerystore.data.model.Order;
import com.atharva.grocerystore.ui.utils.ProductView;

import java.util.List;

public class OrdersActivity extends AppCompatActivity {

    LinearLayout orderViewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        orderViewList = findViewById(R.id.orderViewList);
        showOrders();
    }

    private void showOrders() {
        orderViewList.removeAllViews();
        List<Order> orders = RuntimeData.userData.getOrders();
        orders.forEach(o -> {
            int index = orders.indexOf(o);

            CardView card = ProductView.getOrderScreenProductCard(
                    getApplicationContext(), getResources(), "Order " + index, o.getPrice());

            card.setOnClickListener(v -> {
                Intent intent = new Intent(getApplicationContext(), OrderDetailsActivity.class);
                intent.putExtra("order", index);
                startActivity(intent);
            });

            orderViewList.addView(card);
        });
    }

}
