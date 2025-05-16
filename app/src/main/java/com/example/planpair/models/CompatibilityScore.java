package com.example.planpair.models;

public class CompatibilityScore {
    private int score;

    public CompatibilityScore() {
        // Required for Firebase
    }

    public CompatibilityScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
