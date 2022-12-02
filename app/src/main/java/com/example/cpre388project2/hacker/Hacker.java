package com.example.cpre388project2.hacker;

public class Hacker {
    private int level;

    public Hacker() {
        this(1);
    }

    public Hacker(int level) {
        this.level = level;
    }

    /**
     * Closes any resources and deletes hacker
     */
    public void kill() {
    }

    public int getAttackAmount() {
        return level * 10;
    }

}
