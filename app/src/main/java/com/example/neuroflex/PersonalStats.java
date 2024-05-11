package com.example.neuroflex;

public class PersonalStats {
    private int speed;
    private int accuracy;
    private int time;

    public PersonalStats(int speed, int accuracy, int time) {
        this.speed = speed;
        this.accuracy = accuracy;
        this.time = time;
    }

    // Getters and setters for the properties

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
