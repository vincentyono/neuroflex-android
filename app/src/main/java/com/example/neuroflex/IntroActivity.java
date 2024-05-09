package com.example.neuroflex;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        Button buttonLoginGoogle = findViewById(R.id.buttonLoginGoogle);
        Button buttonLogin = findViewById(R.id.buttonLogin);
        Button buttonReg = findViewById(R.id.buttonReg);

        // Google Login
        buttonLoginGoogle.setOnClickListener(view -> loginWithGoogle());

        // Navigate to Login Activity
        buttonLogin.setOnClickListener(view -> {
            Intent intent = new Intent(IntroActivity.this, Login.class);
            startActivity(intent);
            finish();
        });

        // Navigate to Register Activity
        buttonReg.setOnClickListener(view -> {
            Intent intent = new Intent(IntroActivity.this, Register.class);
            startActivity(intent);
            finish();
        });
    }

    private void loginWithGoogle() {
        // Implement Google Firebase Authentication logic here
    }
}