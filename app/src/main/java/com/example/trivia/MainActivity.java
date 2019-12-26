package com.example.trivia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.trivia.data.AnswerListAsyncResponse;
import com.example.trivia.data.QuestionBank;
import com.example.trivia.data.Score;
import com.example.trivia.model.Question;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String HIGH_SCORE = "highScore";
    public static final String CURRENT_SCORE = "currentScore";
    private RequestQueue mQueue;

    private TextView questionTV;
    private TextView questionCounter;
    private TextView currentScoreTV;
    private TextView highScoreTV;
    private Button trueButton;
    private Button falseButton;

    private ImageButton backButton;
    private ImageButton nextButton;
    private int currentQuestionIndex = 0;

    private List<Question> questionList;


    private int currentScore=0;
    private int highScore = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        questionList = new QuestionBank().getQuestions(new AnswerListAsyncResponse() {
            @Override
            public void proccessFinished(ArrayList<Question> questionArrayList) {

                questionTV.setText(questionArrayList.get(currentQuestionIndex).getAnswer());

                questionCounter.setText(currentQuestionIndex + 1 + " of " + questionList.size());
                Log.d("test json", "processFinished: "+questionArrayList.get(2).getAnswer());


            }
        });

        falseButton = findViewById(R.id.false_button);
        trueButton = findViewById(R.id.true_button);
        questionTV = findViewById(R.id.question_tv);
        questionCounter = findViewById(R.id.question_counter);
        currentScoreTV = findViewById(R.id.current_score);
        highScoreTV = findViewById(R.id.high_score);

        backButton = findViewById(R.id.back_button);
        nextButton = findViewById(R.id.next_button);


        falseButton.setOnClickListener(this);
        trueButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        backButton.setOnClickListener(this);

        questionTV.setText("This is a question");

        //10SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        //highScore = Integer.parseInt(sharedPreferences.getInt(HIGH_SCORE, -1));
        //highScore = sharedPreferences.getInt(HIGH_SCORE, -1);
        loadData();
        updateScore();

    }




    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.true_button:
                //shakeAnimation();
                checkAnswer(true);
                //Toast.makeText(MainActivity.this, "True", Toast.LENGTH_LONG).show();
                break;
            case R.id.false_button:
                checkAnswer(false);
                //Toast.makeText(MainActivity.this, "False", Toast.LENGTH_LONG).show();
                break;
            case R.id.next_button:

                //currentQuestionIndex;
                //currentQuestionIndex = (currentQuestionIndex + 1) % questionList.size();
                //shakeAnimation();
                //updateQuestion();
                goNext();
                questionTV.setText(questionList.get(currentQuestionIndex).getAnswer());
                break;

            case R.id.back_button:
                // Toast.makeText(MainActivity.this, "False", Toast.LENGTH_LONG).show();
                //Toast.makeText(MainActivity.this, "Back", Toast.LENGTH_LONG).show();

                Log.d("Index", " " + currentQuestionIndex);
                if (currentQuestionIndex == 0) {

                    //Log.d("QuestionBank", ""+questionBank.length);
                    //currentQuestionIndex = questionList.size() - 1;
                    //updateQuestion();
                    goBack();
                } else {
                    //currentQuestionIndex;
                    currentQuestionIndex = (currentQuestionIndex - 1) % questionList.size();


                    updateQuestion();
                    //questionTV.setText(questionBank[currentQuestionIndex].getQuestionResId());
                }
                break;
        }
    }


    private void checkAnswer(boolean userChoseCorrect) {
        boolean answerIsTrue = questionList.get(currentQuestionIndex).isAnswerTrue();

        int ToastMessageID = 0;

        if (userChoseCorrect == answerIsTrue) {
            ToastMessageID = R.string.right_answer;
            greenAnimation();
            Score.increaseScore();


        } else {

            //questionTV.setText(questionList.get(currentQuestionIndex).getAnswer());
            ToastMessageID = R.string.wrong_answer;
            shakeAnimation();
            Score.decreaseScore();

            //updateQuestion();
        }
        Toast.makeText(MainActivity.this, ToastMessageID, Toast.LENGTH_LONG).show();
        updateScore();
    }

    private void updateScore(){

        //currentScore+= 10;
        currentScoreTV.setText("Current Score: "+Score.getScore());
        //highScoreTV.setText("High Score: "+ highScore);
        //Toast.makeText(MainActivity.this, "testest", Toast.LENGTH_LONG).show();

        setData();
        highScoreTV.setText("High Score: "+ highScore);

        /*
        if(currentScore > highScore){
            highScore = currentScore;
            highScoreTV.setText("High Score: "+ highScore);
        }
        */
    }

    public void setData() {

        if (Score.getScore() > highScore) {


            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Toast.makeText(MainActivity.this, "shared pref", Toast.LENGTH_LONG).show();


            highScore = Score.getScore();
            editor.putInt(HIGH_SCORE, Score.getScore());

            editor.apply();
        }

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(CURRENT_SCORE, currentScore);
        editor.apply();

    }

    public void loadData(){

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        highScore = sharedPreferences.getInt(HIGH_SCORE, -1);

        currentScore = sharedPreferences.getInt(CURRENT_SCORE, 0);

        Score.setScore(currentScore);

    }


    // Change question
    public void updateQuestion() {
        //shakeAnimation();
        questionCounter.setText(currentQuestionIndex+1 +" of "+ questionList.size());
        questionTV.setText(questionList.get(currentQuestionIndex).getAnswer());
    }

    private void goBack(){
        currentQuestionIndex = questionList.size() - 1;
        updateQuestion();
    }
    private void goNext(){

        currentQuestionIndex = (currentQuestionIndex + 1) % questionList.size();
        updateQuestion();
    }



    // Animation
    private void greenAnimation(){

        Animation animation = new AlphaAnimation(0.1f, 1.0f);
        animation.setDuration(1000);
        final CardView cardView = findViewById(R.id.card_view);
        //cardView.setCardBackgroundColor(Color.GREEN);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
                goNext();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        cardView.startAnimation(animation);

    }
    private void shakeAnimation() {

        Animation shake = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake_animation);

        final CardView cardView = findViewById(R.id.card_view);

        cardView.setAnimation(shake);

        shake.setAnimationListener(new Animation.AnimationListener() {


            @Override
            public void onAnimationStart(Animation animation) {

                cardView.setCardBackgroundColor(Color.RED);

            }


            @Override
            public void onAnimationEnd(Animation animation) {

                cardView.setCardBackgroundColor(Color.WHITE);

                //updateQuestion();
                //Log.d(TAG, "onAnimationEnd: ");
                goNext();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

        });

        cardView.startAnimation(shake);

}

}

