package com.example.greentrailapp.Models;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.greentrailapp.R;

public class ACountryDialog extends Dialog{
    public ACountryDialog(@NonNull Context context) {
        super(context);
    }
    private int instructionPoints = 0;
    Button closeBtn;
    TextView aCountryTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acountry_dialog_layout);

        closeBtn = findViewById(R.id.closeBtn);
        aCountryTV = findViewById(R.id.aCountryTV);

        setInstructionPoint(aCountryTV, "UNSW is located on the unceded territory of the Bedegal (Kensington campus), Gadigal (City and Paddington Campuses) and Ngunnawal peoples (UNSW Canberra) who are the Traditional Owners of the lands where each campus of UNSW is situated.");

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }

    private void setInstructionPoint(TextView instructionsTV, String instructionPoint){
        if(instructionPoints == 0){
            instructionsTV.setText(instructionPoint);
        }
        else{
            instructionsTV.setText(instructionsTV.getText()+ "\n\n" + instructionPoint);
        }
    }
}
