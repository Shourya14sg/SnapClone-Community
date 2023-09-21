package com.example.community;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginPage extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthState;   //this is called when user logs in and logs out //indicates login or not
    private int choiceSwitch;
    LinearLayout registrationBanner;
    CardView logincard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        choiceSwitch=0; //by default
       mAuth=FirebaseAuth.getInstance();   //all thing store here
        firebaseAuthState=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //getcurrentUser calls when user logs in
                FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null){ //for more safety measure
                  //  Toast.makeText(LoginPage.this, "Login recognised", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(LoginPage.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);    //it clears all above intents
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };
        //Registration Banner 1- loginoption
                TextView loginOption=(TextView) findViewById(R.id.login_choice);
                loginOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choiceSwitch=1;
                registrationBanner=findViewById(R.id.Registration_choice);
                logincard=findViewById(R.id.loginCard);
                registrationBanner.setVisibility(View.GONE);
                logincard.setVisibility(View.VISIBLE);
                //Login will be called automatically
            }
        });
        // mAuth.addAuthStateListener(firebaseAuthState);
        //signupOption
        //Button SignUp_Option=findViewById(R.id.Sign_Up_Choice);
         onBackPressed();
    }

    public  void onBackPressed(){
        optionUpdate();
    }

    public void SignUP(View view) {
        choiceSwitch=2;
        Intent intent=new Intent(this,Registration.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);    //it clears all above intents
        startActivity(intent);
        onBackPressed();
        //finish();
    }

    public void Login(View view) {
        EditText emailFeild,passwordFeild;
        emailFeild=findViewById(R.id.EmailEditText);
        passwordFeild=findViewById(R.id.PasswordEditText);
        final String email=emailFeild.getText().toString();
        final String password=passwordFeild.getText().toString();
        TextView errorText=findViewById(R.id.errorMessage);
        if(email.equals("")||email==null||!(email.contains("@"))||password.equals("")||password==null){
            errorText.setVisibility(View.VISIBLE);
            errorText.setText("Please enter valid Email/password");
        }
        else{
                if(errorText.getVisibility()==View.VISIBLE)
                    errorText.setVisibility(View.GONE);

               // Toast.makeText(this, "Login", Toast.LENGTH_SHORT).show();
                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(LoginPage.this, new OnCompleteListener<AuthResult>() {
                    //after this AuthStateLisner will call automatically
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(LoginPage.this, "Sign in successfull", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                //in case of fail
                                // If sign in fails, display a message to the user.
                                Log.w("Login Attempt", "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginPage.this, "Authentication failed.",Toast.LENGTH_SHORT).show();
                                //updateAlertBox();
                            }
                    }
                });
            }
    }
    public void optionUpdate(){
     switch (choiceSwitch){
         case 1://login selected
             registrationBanner=findViewById(R.id.Registration_choice);
             logincard=findViewById(R.id.loginCard);
             registrationBanner.setVisibility(View.VISIBLE);
             logincard.setVisibility(View.GONE);
             break;
         default:
             registrationBanner=findViewById(R.id.Registration_choice);
             registrationBanner.setVisibility(View.VISIBLE);
             break;
     }
    choiceSwitch=0;//as we return to homepage na buddy
    }


    @Override
    protected void onResume() {
        super.onResume();
        mAuth.addAuthStateListener(firebaseAuthState);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthState);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthState);
    }

}