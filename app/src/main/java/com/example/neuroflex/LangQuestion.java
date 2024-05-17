package com.example.neuroflex;

import java.util.List;

public class LangQuestion {
    private String questionText;
    private List<String> answers;
    private int correctAnswerIndex;

    public LangQuestion(String questionText, List<String> answers, int correctAnswerIndex) {
        this.questionText = questionText;
        this.answers = answers;
        this.correctAnswerIndex = correctAnswerIndex;
    }

    public String getQuestionText() {
        return questionText;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }
}
