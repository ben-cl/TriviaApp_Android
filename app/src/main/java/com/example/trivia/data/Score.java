package com.example.trivia.data;

public class Score {

    private static  int score;



    public static void setScore(int s){score = s;}
    public static void increaseScore(){
        score++;
    }
    public static void decreaseScore(){
        if(score>0) score--;

    }

    public static int getScore(){
        return score;
    }
}
