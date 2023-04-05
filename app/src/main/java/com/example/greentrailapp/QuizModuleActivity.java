package com.example.greentrailapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.greentrailapp.Models.InstructionsDialog;
import com.example.greentrailapp.Models.QuestionsList;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


public class QuizModuleActivity extends AppCompatActivity {
    //Declare variables and instantiate views

    private final List<QuestionsList> questionsLists = new ArrayList<>();

    private TextView quizTimer;
    private RelativeLayout option1Layout, option2Layout, option3Layout, option4Layout;
    private TextView option1TV, option2TV, option3TV, option4TV;
    private ImageView option1Icon, option2Icon, option3Icon, option4Icon;
    private TextView questionTV;
    private TextView totalQuestionsTV;
    private TextView currentQuestion;
    private Button nextQuestionBtn;
    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://green-trail-app-default-rtdb.firebaseio.com/");
    BottomNavigationView nav;

    private CountDownTimer downTimer;

    private int currentQuestionPosition = 0;
    private int selectedOption = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_module);
        getSupportActionBar().hide();

        nav = findViewById(R.id.nav);
        //nav.setSelectedItemId(R.id.quiz);

        //Binding views
        quizTimer = findViewById(R.id.quizTimer);
        option1Layout= findViewById(R.id.option1Layout);
        option2Layout= findViewById(R.id.option2Layout);
        option3Layout= findViewById(R.id.option3Layout);
        option4Layout= findViewById(R.id.option4Layout);

        option1TV = findViewById(R.id.option1TV);
        option2TV = findViewById(R.id.option2TV);
        option3TV = findViewById(R.id.option3TV);
        option4TV = findViewById(R.id.option4TV);

        option1Icon = findViewById(R.id.option1Icon);
        option2Icon = findViewById(R.id.option2Icon);
        option3Icon = findViewById(R.id.option3Icon);
        option4Icon = findViewById(R.id.option4Icon);

        questionTV = findViewById(R.id.questionTV);
        totalQuestionsTV = findViewById(R.id.totalQuestionsTV);
        currentQuestion = findViewById(R.id.currentQuestionTV);

        nextQuestionBtn = findViewById(R.id.nextQuestionBtn);

        //Creates an instance of the instructions dialog which must be read and closed prior to engaging in the quiz
        InstructionsDialog instructionsDialog = new InstructionsDialog(QuizModuleActivity.this);
        instructionsDialog.setCancelable(false);
        instructionsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        instructionsDialog.show();

        //Populate the views in the activity based on Firebase Realtime Database backend, where quiz data is stored
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final int getQuizTime = Integer.parseInt(snapshot.child("time").getValue(String.class));

                for (DataSnapshot questions : snapshot.child("questions").getChildren()){
                    String getQuestion = questions.child("question").getValue(String.class);
                    String getOption1 = questions.child("option1").getValue(String.class);
                    String getOption2 = questions.child("option2").getValue(String.class);
                    String getOption3 = questions.child("option3").getValue(String.class);
                    String getOption4 = questions.child("option4").getValue(String.class);
                    int getAnswer = Integer.parseInt(questions.child("answer").getValue(String.class));

                    QuestionsList questionsList = new QuestionsList(getQuestion, getOption1, getOption2, getOption3, getOption4, getAnswer);

                    questionsLists.add(questionsList);
                }

                totalQuestionsTV.setText("/" + String.valueOf(questionsLists.size()));
                //Begin the timer
                startQuizTimer(getQuizTime);
                //Load the first question
                selectQuestion(currentQuestionPosition);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(QuizModuleActivity.this, "Failed to fetch questions", Toast.LENGTH_SHORT).show();
            }
        });
        //onClick listeners for each answer option
        option1Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedOption = 1;
                selectOption(option1Layout, option1Icon);
            }
        });
        option2Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedOption = 2;
                selectOption(option2Layout, option2Icon);

            }
        });
        option3Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedOption = 3;
                selectOption(option3Layout, option3Icon);
            }
        });
        option4Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedOption = 4;
                selectOption(option4Layout, option4Icon);
            }
        });
        //
        nextQuestionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedOption != 0){
                    questionsLists.get(currentQuestionPosition).setUserSelectedAnswer(selectedOption);

                    selectedOption = 0;
                    currentQuestionPosition++;

                    if(currentQuestionPosition < questionsLists.size()){
                        selectQuestion(currentQuestionPosition);
                    }
                    else{
                        downTimer.cancel();
                        finishQuiz();
                    }
                }
                else{
                    Toast.makeText(QuizModuleActivity.this, "Please select an answer", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //closes the quiz and sends results to the quizresult activity
    private void finishQuiz(){
        Intent intent = new Intent(QuizModuleActivity.this, QuizResult.class);

        addProgress();

        Bundle bundle = new Bundle();
        bundle.putSerializable("questions", (Serializable)questionsLists);

        intent.putExtras(bundle);

        startActivity(intent);

        finish();
    }

    private void addProgress(){
        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("progress").setValue(ServerValue.increment(5));
    }

    //Begins the quiz timer, counting down by 1 second at a time and displaying in view
    private void startQuizTimer(int maxTimeInSeconds){
        downTimer = new CountDownTimer(maxTimeInSeconds*1000, 1000) {
            @Override
            public void onTick(long l) {
                long getHour = TimeUnit.MILLISECONDS.toHours(l);
                long getMinute = TimeUnit.MILLISECONDS.toMinutes(l);
                long getSecond = TimeUnit.MILLISECONDS.toSeconds(l);

                String generateTime = String.format(Locale.getDefault(), "%02d:%02d:%02d", getHour,
                        getMinute - TimeUnit.HOURS.toMinutes(getHour),
                        getSecond - TimeUnit.MINUTES.toSeconds(getMinute));

                quizTimer.setText(generateTime);

            }

            @Override
            public void onFinish() {
                finishQuiz();
            }
        };
        downTimer.start();
    }
    //Reset activity
    private void selectQuestion(int questionListPosition){
        resetOptions();
        questionTV.setText(questionsLists.get(questionListPosition).getQuestion());
        option1TV.setText(questionsLists.get(questionListPosition).getOption1());
        option2TV.setText(questionsLists.get(questionListPosition).getOption2());
        option3TV.setText(questionsLists.get(questionListPosition).getOption3());
        option4TV.setText(questionsLists.get(questionListPosition).getOption4());


        currentQuestion.setText("Question"+(questionListPosition+1));
    }

    private void resetOptions(){
        option1Layout.setBackgroundResource(R.drawable.round_back_white50_10);
        option2Layout.setBackgroundResource(R.drawable.round_back_white50_10);
        option3Layout.setBackgroundResource(R.drawable.round_back_white50_10);
        option4Layout.setBackgroundResource(R.drawable.round_back_white50_10);

        option1Icon.setImageResource(R.drawable.round_back_white50_100);
        option2Icon.setImageResource(R.drawable.round_back_white50_100);
        option3Icon.setImageResource(R.drawable.round_back_white50_100);
        option4Icon.setImageResource(R.drawable.round_back_white50_100);
    }

    private void selectOption(RelativeLayout selectedOptionLayout, ImageView selectedOptionIcon){


        resetOptions();

        selectedOptionIcon.setImageResource(R.drawable.tick);
        selectedOptionLayout.setBackgroundResource(R.drawable.round_back_selected_option);
    }
}