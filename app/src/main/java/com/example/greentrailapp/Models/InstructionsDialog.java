package com.example.greentrailapp.Models;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.greentrailapp.R;

public class InstructionsDialog extends Dialog {
    public InstructionsDialog(@NonNull Context context) {
        super(context);
    }
    private int instructionPoints = 0;
    Button continueBtn;
    TextView instructionsTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.instructions_dialog_layout);

        continueBtn = findViewById(R.id.continueBtn);
        instructionsTV = findViewById(R.id.instructionsTV);

        setInstructionPoint(instructionsTV, "You will have 30 seconds per question. Every correct answer = 1 point.");

        continueBtn.setOnClickListener(new View.OnClickListener() {
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
