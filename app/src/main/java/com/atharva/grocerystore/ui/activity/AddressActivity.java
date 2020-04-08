package com.atharva.grocerystore.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.atharva.grocerystore.R;
import com.atharva.grocerystore.data.RuntimeData;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class AddressActivity extends AppCompatActivity {

    EditText addressLine1;
    EditText addressLine2;
    EditText city;
    EditText state;
    EditText pincode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        addressLine1 = findViewById(R.id.addressLine1);
        addressLine2 = findViewById(R.id.addressLine2);
        city = findViewById(R.id.city);
        state = findViewById(R.id.state);
        pincode = findViewById(R.id.pincode);

        String address = RuntimeData.userData.getAddress();
        if (address != null && !address.isEmpty()) {
            String[] split = address.split(",");
            int i=split.length-1;
            pincode.setText(split[i]);
            i -= 1;
            state.setText(split[i]);
            i -= 1;
            city.setText(split[i]);
            i -= 1;
            addressLine2.setText(split[i]);
            StringBuilder bf = new StringBuilder();
            for (int j=0; j<i ; j++) {
                bf.append(split[j]);
            }
            addressLine1.setText(bf.toString());
        }
    }

    public void onCancel(View view) {
        RuntimeData.tempOrder = null;
        startActivity(new Intent(getApplicationContext(), CartActivity.class));
        finish();
    }

    public void onNext(View view) {
        String address = String.format("%s, %s, %s, %s, %s",
                addressLine1.getText().toString(),
                addressLine2.getText().toString(),
                city.getText().toString(),
                state.getText().toString(),
                pincode.getText().toString());

        RuntimeData.userData.setAddress(address);
        RuntimeData.tempOrder.setAddress(address);

        RuntimeData.userDb = FirebaseDatabase.getInstance().getReference().child("users").child(getDbRefName());
        RuntimeData.userDb.setValue(RuntimeData.userData).addOnCompleteListener(
                        task -> startActivity(new Intent(getApplicationContext(), BuyActivity.class)));


    }

    private String getDbRefName() {
        return Objects.requireNonNull(RuntimeData.user.getEmail())
                .replaceAll("[.]", "")
                .replaceAll("[#]", "")
                .replaceAll("[$]", "")
                .replaceAll("[\\[]", "")
                .replaceAll("[]]", "");
    }
}
