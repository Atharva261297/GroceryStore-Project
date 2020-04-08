package com.atharva.grocerystore.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.atharva.grocerystore.R;
import com.atharva.grocerystore.data.RuntimeData;
import com.atharva.grocerystore.data.model.Profile;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private static final int SIGN_IN_CODE = 100;
    private FirebaseAuth mAuth;
    EditText email;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.emailLogin);
        password = findViewById(R.id.passwordLogin);
    }

    @Override
    public void onStart() {
        super.onStart();
        RuntimeData.user = mAuth.getCurrentUser();
        if (RuntimeData.user != null) {
            showProgressBar();
            initDbRef();
            initUserData();
        }
    }

    private void initDbRef() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        RuntimeData.userDb = database.getReference().child("users");
        RuntimeData.productDb = database.getReference().child("products");
    }

    private void initUserData() {
        RuntimeData.userDb.child(getDbRefName())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        RuntimeData.userData = dataSnapshot.getValue(Profile.class);
                        hideProgressBar();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), "Error fetching user details from server", Toast.LENGTH_SHORT).show();
                        hideProgressBar();
                    }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SIGN_IN_CODE) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                RuntimeData.user = FirebaseAuth.getInstance().getCurrentUser();
                initDbRef();
                initUserData();
            } else {
                Toast.makeText(getApplicationContext(),"Error during signing", Toast.LENGTH_SHORT).show();
                hideProgressBar();
            }
        }
    }

    public void onLogin(View view) {
        showProgressBar();
        mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            RuntimeData.user = mAuth.getCurrentUser();
                            initDbRef();
                            initUserData();
                        } else {
                            Toast.makeText(getApplicationContext(),"Error during signing", Toast.LENGTH_SHORT).show();
                            hideProgressBar();
                        }
                    }
                });
    }

    public void onRegister(View view) {
        startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
    }

    private void hideProgressBar() {
        findViewById(R.id.loginLayout).setAlpha(1f);
        findViewById(R.id.login_progress_bar).setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void showProgressBar() {
        findViewById(R.id.loginLayout).setAlpha(0.2f);
        findViewById(R.id.login_progress_bar).setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}
