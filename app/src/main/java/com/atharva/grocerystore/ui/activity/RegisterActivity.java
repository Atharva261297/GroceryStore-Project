package com.atharva.grocerystore.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.atharva.grocerystore.R;
import com.atharva.grocerystore.data.RuntimeData;
import com.atharva.grocerystore.data.model.Profile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText email;
    EditText password;
    EditText name;
    EditText phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.emailRegister);
        password = findViewById(R.id.passwordRegister);
        name = findViewById(R.id.nameRegister);
        phone = findViewById(R.id.phoneRegister);
    }

    public void onRegister(View view) {
        showProgressBar();
        RuntimeData.userData =
                new Profile(
                        name.getText().toString(),
                        email.getText().toString(),
                        phone.getText().toString(),
                        "", new ArrayList<>());

        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        RuntimeData.user = mAuth.getCurrentUser();
                        initDbRefAndData();
                    } else {
                        Toast.makeText(getApplicationContext(),"Error during signing", Toast.LENGTH_SHORT).show();
                        hideProgressBar();
                    }
                });
    }

    private void initDbRefAndData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        RuntimeData.userDb = database.getReference().child("users");
        RuntimeData.productDb = database.getReference().child("products");
        RuntimeData.userDb.child(getDbRefName()).push().getKey();
        RuntimeData.userDb = FirebaseDatabase.getInstance().getReference().child("users").child(getDbRefName());
        RuntimeData.userDb.setValue(RuntimeData.userData).addOnCompleteListener(task -> {
            hideProgressBar();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        });
    }

    private String getDbRefName() {
        return Objects.requireNonNull(RuntimeData.user.getEmail())
                .replaceAll("[.]", "")
                .replaceAll("[#]", "")
                .replaceAll("[$]", "")
                .replaceAll("[\\[]", "")
                .replaceAll("[]]", "");
    }

    private void hideProgressBar() {
        findViewById(R.id.registerLayout).setAlpha(1f);
        findViewById(R.id.register_progress_bar).setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void showProgressBar() {
        findViewById(R.id.registerLayout).setAlpha(0.2f);
        findViewById(R.id.register_progress_bar).setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}
