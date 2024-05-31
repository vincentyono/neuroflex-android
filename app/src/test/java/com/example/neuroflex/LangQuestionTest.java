package com.example.neuroflex;

import static org.junit.Assert.assertTrue;

import com.example.neuroflex.Models.LangQuestion;

import org.junit.Test;

import java.util.ArrayList;

public class LangQuestionTest {
    @Test
    public void getQuestionTextShouldReturnQuestion() {
        ArrayList<String> ans = new ArrayList<>();
        ans.add("A");
        ans.add("B");
        ans.add("C");
        ans.add("D");
        LangQuestion langQuestion = new LangQuestion(
                "What is the first character in english Alphabet?",
                ans,
                0
        );
        assertTrue(langQuestion.getQuestionText().length() > 0);
    }

    @Test
    public void getAnswerShouldNotBeEmpty() {
        ArrayList<String> ans = new ArrayList<>();
        ans.add("A");
        ans.add("B");
        ans.add("C");
        ans.add("D");
        LangQuestion langQuestion = new LangQuestion(
                "What is the first character in english Alphabet?",
                ans,
                0
        );
        assertTrue(langQuestion.getAnswers().size() > 0);
    }

    @Test
    public void getCorrectAnswerIndexShouldAlwaysBePositive() {
        ArrayList<String> ans = new ArrayList<>();
        ans.add("A");
        ans.add("B");
        ans.add("C");
        ans.add("D");
        LangQuestion langQuestion = new LangQuestion(
                "What is the first character in english Alphabet?",
                ans,
                0
        );
        assertTrue(langQuestion.getCorrectAnswerIndex() > -1);
    }
}
