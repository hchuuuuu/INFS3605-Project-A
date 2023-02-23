package com.example.greentrailapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    EditText loginEmail;
    EditText loginPass;
    Button btnLogin;
    TextView registerLink;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Hides the top bar
        getSupportActionBar().hide();

        //Binding views
        loginEmail = findViewById(R.id.loginEmail);
        loginPass = findViewById(R.id.loginPass);
        btnLogin = findViewById(R.id.btnLogin);
        registerLink = findViewById(R.id.registerLink);

        //Creating instance of FirebaseAuth
        mAuth = FirebaseAuth.getInstance();


        //When login button is clicked, execute the loginUser method
        btnLogin.setOnClickListener(view ->{
            loginUser();
        });
        //When register is clicked, open the register activity
        registerLink.setOnClickListener(view ->{
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });

    }

    private void loginUser() {
        //Extract user input from textInput fields
        String email = loginEmail.getText().toString();
        String password = loginPass.getText().toString();

        //if loop for throwing errors if email/ password are empty, or create user if both fields are valid
        if (TextUtils.isEmpty(email)){
            loginEmail.setError("Email cannot be empty!");
            loginEmail.requestFocus();
        }else if (TextUtils.isEmpty(password)){
            loginPass.setError("Password cannot be empty!");
            loginPass.requestFocus();
        }else{
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Logged in!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }else{
                        Toast.makeText(LoginActivity.this, " Error logging in! ", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}