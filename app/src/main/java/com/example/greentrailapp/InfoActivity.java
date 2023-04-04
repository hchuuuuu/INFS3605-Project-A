package com.example.greentrailapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.greentrailapp.Models.Desc;
import com.example.greentrailapp.Models.Marker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

public class InfoActivity extends AppCompatActivity {

    TextView mNameTV, iNameTV, sNameTV, gDistTV, tradUTV, descTV;
    ImageView markerIV, backIV;
    private DatabaseReference fb;
    private Button completeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        getSupportActionBar().hide();

        backIV = findViewById(R.id.back_infoTopic);
        backIV.setOnClickListener(view -> {
            onBackPressed();
        });

        mNameTV = findViewById(R.id.tvMname);
        iNameTV = findViewById(R.id.tvIname);
        sNameTV = findViewById(R.id.tvSciname);
        gDistTV = findViewById(R.id.tvGdistr);
        tradUTV = findViewById(R.id.tvTraduse);
        markerIV = findViewById(R.id.infoIV);
        descTV = findViewById(R.id.tvDesc);
        completeBtn = findViewById(R.id.completeBtn);

        Marker selectedMarker = getIntent().getParcelableExtra("Marker");

        String visible = "F";
        visible = getIntent().getStringExtra("Visible");
        if (visible.equals("T")){
            System.out.println(visible);
            completeBtn.setVisibility(View.VISIBLE);
        }

        completeBtn.setOnClickListener(view ->{
            addProgress();
            startActivity(new Intent(InfoActivity.this, MainActivity.class));
        });

        mNameTV.setText(selectedMarker.getmName());
        iNameTV.setText("Indigenous name: " + selectedMarker.getiName());
        sNameTV.setText("Scientific name: " + selectedMarker.getmSciName());
        gDistTV.setText("Geographic Distribution: " + selectedMarker.getGeoDistr());
        tradUTV.setText("Traditional Uses: " + selectedMarker.getTradUses());
        Glide.with(this).load(selectedMarker.getImg_url()).into(markerIV);

        String query = selectedMarker.getmSciName();
        startThread(query);

    }

    public void startThread(String query){
        InfoActivityRunnable runnable = new InfoActivityRunnable(query);
        new Thread(runnable).start();
    }

    public void stopThread(View view){

    }

    class InfoActivityRunnable implements Runnable{
        String queryItem;

        InfoActivityRunnable(String queryItem){
            this.queryItem = queryItem;
        }
        @Override
        public void run(){
            Desc plantDesc = new Desc(queryItem);
            if (plantDesc != null){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        descTV.setText("Description: " + plantDesc.getExtract());
                    }
                });
            }
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(InfoActivity.this, MainActivity.class));
    }

    private void addProgress(){
        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("progress").setValue(ServerValue.increment(4.16666666667));
    }
}