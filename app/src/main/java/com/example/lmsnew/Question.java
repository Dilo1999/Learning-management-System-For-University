package com.example.lmsnew;

public class Question {
    private String question;
    private int questionNumber;

    public Question() {
        // Required empty constructor for Firestore serialization
    }

    public Question(String question, int questionNumber) {
        this.question = question;
        this.questionNumber = questionNumber;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }
}
