package com.example.neuroflex.mathpuzzle;

import java.util.Random;
public class MathQuestion {
    private int _a;
    private int _b;
    private char _operator;
    private int _c;

    public MathQuestion(Difficulty difficulty) {
        if(difficulty == Difficulty.EASY) {
            int a = this.getRandomNumber(1, 9);
            int b = this.getRandomNumber(1, 9);
            this._a = Math.max(a, b);
            this._b = Math.min(a, b);
            this._operator = this.getRandomOperator();
        }
        else if(difficulty == Difficulty.MEDIUM) {
            this._a = this.getRandomNumber(10, 89);
            this._b = this.getRandomNumber(1, 9);
            this._operator = this.getRandomOperator();
        }
        else {
            int a = this.getRandomNumber(1, 99);
            int b = this.getRandomNumber(1, 99);
            this._a = Math.max(a, b);
            this._b = Math.min(a, b);
            this._operator = this.getRandomOperator();
        }
    }

    public String getQuestion() {
        return String.format("%d %c %d", this._a, this._operator, this._b);
    }

    public int getAnswer() {
        return this._c;
    }

    public void setC(int c) {
        this._c = c;
    }

    public int getC() {
        return this._c;
    }

    public Boolean verifyAnswer() {
        if(this._operator == '+') return this._c == this._a + this._b;
        else if(this._operator == '-') return this._c == this._a - this._b;
        else  return this._c == this._a * this._b;
    }

    /*
    * Returns a random number between 1 and upperBound (inclusive)
    * */
    private int getRandomNumber(int lowerBound, int upperBound) {
        Random random = new Random();
        return random.nextInt(upperBound - lowerBound) + lowerBound;
    }

    /*
    * Returns a random arithmetic operator [+, -, *]
    * */
    private char getRandomOperator() {
        Random random = new Random();
        int temp = random.nextInt(3);

        switch(temp) {
            case 0:
                return '+';
            case 1:
                return '-';
            default:
                return '*';
        }
    }
}
