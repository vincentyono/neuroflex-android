package com.example.neuroflex;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        // Initializing UI from layout components
        Button buttonLogin = findViewById(R.id.buttonLogin);
        Button buttonReg = findViewById(R.id.buttonReg);

        // Navigate to Login Activity
        buttonLogin.setOnClickListener(view -> {
            // Create an Intent to start the Login activity
            Intent intent = new Intent(IntroActivity.this, Login.class);
            // Start the Login activity
            startActivity(intent);
            // Finish the current activity to remove it from the back stack
            finish();
        });

        // Navigate to Register Activity
        buttonReg.setOnClickListener(view -> {
            // Create an Intent to start the Register activity
            Intent intent = new Intent(IntroActivity.this, Register.class);
            // Start the Register activity
            startActivity(intent);
            // Finish the current activity to remove it from the back stack
            finish();
        });
    }

}