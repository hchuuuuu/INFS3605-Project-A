package com.example.greentrailapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.greentrailapp.Models.Person;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    //Declare views
    EditText regName;
    EditText regEmail;
    EditText regPass;
    Button btnRegister;
    TextView loginLink;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();
        //Bind views
        regName = findViewById(R.id.regName);
        regEmail = findViewById(R.id.regEmail);
        regPass = findViewById(R.id.regPass);
        btnRegister = findViewById(R.id.btnRegister);
        loginLink = findViewById(R.id.loginLink);
        //Create instance of FirebaseAuth
        mAuth = FirebaseAuth.getInstance();
        //when the register button is clicked, execute the createUser() method
        btnRegister.setOnClickListener(view ->{
            createUser();
        });
        //when the login link is clicked, open loginActivity
        loginLink.setOnClickListener(view ->{
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        });

    }

    private void createUser() {
        //Extract user input from views
        String email = regEmail.getText().toString();
        String password = regPass.getText().toString();
        String name = regName.getText().toString();
        //catch null user input errors and display messages
        if (TextUtils.isEmpty(email)){
            regEmail.setError("Email cannot be empty!");
            regEmail.requestFocus();
        }else if (TextUtils.isEmpty(password)) {
            regPass.setError("Password cannot be empty!");
            regPass.requestFocus();
        }else if (TextUtils.isEmpty(name)) {
            regName.setError("Name cannot be empty!");
            regName.requestFocus();
        }else{
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    //if all field are complete, create a user with 0 progress, display a toast, and navigate to the login Activity
                    if (task.isSuccessful()){
                        Person person = new Person(name, 0);
                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(person).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(RegisterActivity.this, "User registered successfully!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(RegisterActivity.this, " Registration failed. " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}