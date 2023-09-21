package com.example.community;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class Registration extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener firebaseAuthState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
       // firebaseAuthState=mAuth.addAuthStateListener();
    }
}