package com.example.community;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthProvider;

public class SplashScreen extends AppCompatActivity {
    private static FirebaseAuth mAuth;
    public static Boolean started=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mAuth=FirebaseAuth.getInstance();   //contains all the information connect with the user
        if(mAuth.getCurrentUser()!=null)
        {
           // Toast.makeText(this, "Hello "+mAuth.getCurrentUser().getEmail().toString(), Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);    //it clears all above intents
            startActivity(intent);
            finish();
            return;
        }else{
            Toast.makeText(this, "Revert to login page", Toast.LENGTH_SHORT).show();
            try {
                Thread.sleep(2000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Intent intent=new Intent(SplashScreen.this,LoginPage.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);    //it clears all above intents
            startActivity(intent);
            finish();
            return;
        }
    }
}