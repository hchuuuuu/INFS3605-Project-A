package com.example.greentrailapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.greentrailapp.Models.QuestionsList;

import java.util.ArrayList;
import java.util.List;


public class QuizResult extends AppCompatActivity {
    private List<QuestionsList> questionsLists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);
        getSupportActionBar().hide();
        //Declare and bind views

        final TextView score = findViewById(R.id.scoreTV);
        final TextView totalScoreTV = findViewById(R.id.totalScoreTV);
        final TextView correctTV = findViewById(R.id.correctTV);
        final TextView incorrectTV = findViewById(R.id.incorrectTV);

        final Button shareBtn = findViewById(R.id.shareBtn);
        final Button retakeBtn = findViewById(R.id.retakeBtn);
        final Button homeBtn = findViewById(R.id.homeBtn);

        //Get the questionLists list
        questionsLists = (List<QuestionsList>) getIntent().getSerializableExtra("questions");
        //Set the result screen based on user score in the quiz & the quiz length
        totalScoreTV.setText("/" + questionsLists.size());
        score.setText(getCorrectAnswers()+ "");
        correctTV.setText(getCorrectAnswers() + "");
        incorrectTV.setText(String.valueOf(questionsLists.size() - getCorrectAnswers()));
        //Implicit intent to share quiz results via suitable apps (user decides)
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_SEND);
                String message = "I scored " + score.getText() + " on the Bush Trail quiz!";
                intent.putExtra(Intent.EXTRA_TEXT, message);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "I completed the Bush Trail quiz!");
                Intent chooser = Intent.createChooser(intent, "Share via");

                try{
                    startActivity(chooser);
                }catch (Exception e){

                }
                finish();


            }
        });
        //Restarts the quiz, navigates user to quizActivity
        retakeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(QuizResult.this, QuizModuleActivity.class));
            }
        });
        //Navigates back to MainActivity
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(QuizResult.this, MainActivity.class));
            }
        });
    }
    //Compares user input to correct answer (stored in Realtime Database) and returns an int user score.
    private int getCorrectAnswers(){
        int correctAnswers = 0;
        for(int i = 0; i<questionsLists.size(); i++){
            int getUserSelectedOption = questionsLists.get(i).getUserSelectedAnswer();
            int getQuestionAnswer = questionsLists.get(i).getAnswer();

            if(getUserSelectedOption == getQuestionAnswer){
                correctAnswers++;
            }
        }
        return correctAnswers;
    }
}