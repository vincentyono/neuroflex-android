package com.example.neuroflex;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.example.neuroflex.mathpuzzle.Difficulty;
import com.example.neuroflex.mathpuzzle.MathQuestion;

import org.junit.Test;

public class MathQuestionTest {
    @Test
    public void singleDigitArithmeticOnEasyQuestion() {
        MathQuestion question = new MathQuestion(Difficulty.EASY);
        // question should be '<single_digit> <arithmetic_operator> <single_digit>'
        // so length of the question should be 5
        assertTrue(question.getQuestion().length() == 5);
    }

    @Test
    public void doubleDigitToSingleDigitOnMediumQuestion() {
        MathQuestion question = new MathQuestion(Difficulty.MEDIUM);
        // question should be '<double_digit> <arithmetic_operator> <single_digit>'
        // so length of the question should be 6
        assertTrue(question.getQuestion().length() == 6);
        // first digit should have length of 2
        assertTrue(question.getQuestion().split(" ")[0].length() == 2);
    }

    @Test
    public void doubleDigitToDoubleDigitOnHardQuestion() {
        MathQuestion question = new MathQuestion(Difficulty.HARD);
        // question should be '<double_digit> <arithmetic_operator> <single_digit>'
        // so length of the question should be 7
        assertTrue(question.getQuestion().length() == 7);
    }

    @Test
    public void getRandomOperatorShouldOnlyBeAdditionSubstractionAndMultiplication() {
        MathQuestion question = new MathQuestion(Difficulty.HARD);
        boolean addition = question.getQuestion().charAt(3) == '+';
        boolean substraction = question.getQuestion().charAt(3) == '-';
        boolean multiplication = question.getQuestion().charAt(3) == '*';
        // question should only have either addition, substraction or multiplication operator
        assertTrue(addition || substraction || multiplication);
    }

    @Test
    public void verifyShouldWorkProperly() {
        MathQuestion question = new MathQuestion(Difficulty.HARD);
        int answer = question.getAnswer();
        question.setC(answer);
        // verifyAnswer method should always be true if C is set to getAnswer
        assertTrue(question.verifyAnswer());
    }
}
