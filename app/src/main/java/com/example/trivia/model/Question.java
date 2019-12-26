package com.example.trivia.model;

public class Question {

    private String answer;
    private boolean answerTrue;

    public Question(String answer, boolean answerTrue) {
        this.answer = answer;
        this.answerTrue = answerTrue;
    }

    public Question() {

    }

    // Setters
    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setAnswerTrue(boolean answerTrue) {
        this.answerTrue = answerTrue;
    }

    // Getters
    public String getAnswer() {
        return answer;
    }

    public boolean isAnswerTrue() {
        return answerTrue;
    }
}
