package com.atharva.grocerystore.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;

import com.atharva.grocerystore.R;
import com.atharva.grocerystore.data.RuntimeData;
import com.atharva.grocerystore.data.model.Product;
import com.atharva.grocerystore.ui.utils.ProductView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    SearchView searchView;
    LinearLayout productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        productList = findViewById(R.id.productsViewList);
        searchView = findViewById(R.id.searchProducts);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                productList.removeAllViews();
                for (final Product p : RuntimeData.products.values()) {
                    if (p.getName().toLowerCase().contains(query)) {
                        productList.addView(getCardForProduct(p));
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                productList.removeAllViews();
                for (final Product p : RuntimeData.products.values()) {
                    if (p.getName().contains(newText)) {
                        productList.addView(getCardForProduct(p));
                    }
                }
                return false;
            }
        });
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

    @Override
    protected void onResume() {
        try {
            RuntimeData.productDb.addValueEventListener(productListener());
        } catch (Exception ignored) {}
        super.onResume();
    }

    @Override
    protected void onPause() {
        try {
            RuntimeData.productDb.removeEventListener(productListener());
        } catch (Exception ignored) {}
        super.onPause();
    }

    private void showProducts() {
        productList.removeAllViews();
        for (final Product p : RuntimeData.products.values()) {
            productList.addView(getCardForProduct(p));
        }
    }

    private CardView getCardForProduct(Product p) {
        CardView card = ProductView.getMainScreenProductCard(
                getApplicationContext(),
                getResources(),
                p.getName(),
                p.getPhoto()
        );

        card.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ProductActivity.class);
            intent.putExtra("name", p.getName());
            startActivity(intent);
        });
        return card;
    }

    private ValueEventListener productListener() {
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (RuntimeData.products == null) {
                    RuntimeData.products = new HashMap<>();
                }
                for (DataSnapshot productFromDb : dataSnapshot.getChildren()) {
                    if (RuntimeData.products.containsKey(productFromDb.getKey())) {
                        Objects.requireNonNull(RuntimeData.products.get(productFromDb.getKey()))
                                .updateProduct(Objects.requireNonNull(productFromDb.getValue(Product.class)));
                    } else {
                        RuntimeData.products.put(productFromDb.getKey(), Objects.requireNonNull(productFromDb.getValue(Product.class)));
                    }
                }
                showProducts();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
    }
}
