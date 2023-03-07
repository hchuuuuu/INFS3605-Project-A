package com.example.greentrailapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class InfoActivity extends AppCompatActivity {

    TextView intentTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        intentTest = findViewById(R.id.intentTest);
        Intent intent = getIntent();
        String str = intent.getStringExtra("markerName");
        intentTest.setText(str);

    }
}