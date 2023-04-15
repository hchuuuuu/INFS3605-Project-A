package com.example.greentrailapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class TutorialActivity extends AppCompatActivity {

    Button exitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        getSupportActionBar().hide();

        exitBtn = findViewById(R.id.exitBtn);
        exitBtn.setOnClickListener(view ->{
            startActivity(new Intent(TutorialActivity.this, ProfileActivity.class));
        });
    }
}