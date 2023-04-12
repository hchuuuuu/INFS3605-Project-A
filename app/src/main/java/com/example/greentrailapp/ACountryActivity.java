package com.example.greentrailapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ACountryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acountry);
        getSupportActionBar().hide();
    }
}