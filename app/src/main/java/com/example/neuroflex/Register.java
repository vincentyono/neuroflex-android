package com.example.neuroflex;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {

    // UI elements
    TextInputEditText editTextName, editTextEmail, editTextPassword;
    Button buttonReg;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    ProgressBar progressBar;
    TextView textView;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    public void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        // Initialize UI elements
        editTextName = findViewById(R.id.name);
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        buttonReg = findViewById(R.id.btn_register);
        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.loginNow);

        // Navigate to Login Activity when "loginNow" text is clicked
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        // Register button click listener
        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user input
                String name, email, password;
                name = String.valueOf(editTextName.getText());
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());

                // Validate input fields
                if (TextUtils.isEmpty(name)){
                    Toast.makeText(Register.this, "Enter name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(email)){
                    Toast.makeText(Register.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    Toast.makeText(Register.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(Register.this, "Enter a valid email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isValidPassword(password)) {
                    Toast.makeText(Register.this, "Password must be at least 6 characters long, contain at least one capital letter, and one number.", Toast.LENGTH_LONG).show();
                    return;
                }

                // Show progress bar when the db is being queried
                progressBar.setVisibility(View.VISIBLE);

                // Create user with email and password in db
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Register.this, task -> {
                            // Hide progress bar
                            progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                Toast.makeText(Register.this, "Account created.", Toast.LENGTH_SHORT).show();
                                // If sign in succeeds, display a message to the user and create user data
                                DbQuery.createUserData(email, name, new MyCompleteListener(){

                                    @Override
                                    public void onSuccess() {
                                        // Firebase Analytics
                                        Bundle regBundle = new Bundle();
                                        regBundle.putString(FirebaseAnalytics.Event.SIGN_UP, name);
                                        mFirebaseAnalytics.logEvent("sign_up", regBundle);
                                        Log.d("FirebaseAnalytics", "log sign_up");

                                        // Navigate to Login Activity on success
                                        Intent intent = new Intent(getApplicationContext(), Login.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                    @Override
                                    public void onFailure() {
                                        // Show error message on failure
                                        Toast.makeText(Register.this, "Something went wrong. Try again later!", Toast.LENGTH_SHORT).show();
                                    }
                                });

//                                    FirebaseUser user = mAuth.getCurrentUser();
//                                    if (user != null) {
//
//                                        Toast.makeText(Register.this, "Account created.", Toast.LENGTH_SHORT).show();
//                                        Intent intent = new Intent(getApplicationContext(), Login.class);
//                                        startActivity(intent);
//                                        finish();
//
//                                        // Save the user's name in db.
////                                        Map<String, Object> userMap = new HashMap<>();
////                                        userMap.put("name", name);
////                                        db.collection("users").document(user.getUid()).set(userMap)
////                                                .addOnSuccessListener(aVoid -> {
////                                                    Log.d("Register", "User profile updated.");
////                                                })
////                                                .addOnFailureListener(e -> {
////                                                    Log.w("Register", "Error updating user profile.", e);
////                                                });
//                                    }
                            } else {
                                // If sign in fails, display a message to the user.
                                if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                    Toast.makeText(Register.this, "This email is already registered.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(Register.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });



            }
        });
    }

    // Function to validate password
    private boolean isValidPassword(String password) {
        // Minimum 6 characters, at least one uppercase letter and one number
        Pattern pattern = Pattern.compile("^(?=.*[A-Z])(?=.*\\d).{6,}$");
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}