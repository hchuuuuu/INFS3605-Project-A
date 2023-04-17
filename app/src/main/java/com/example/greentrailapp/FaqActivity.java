package com.example.greentrailapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class FaqActivity extends AppCompatActivity {

    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        getSupportActionBar().hide();

        back = findViewById(R.id.btnBack);
        back.setOnClickListener(view ->{
            startActivity(new Intent(FaqActivity.this, ProfileActivity.class));
        });

    }
}