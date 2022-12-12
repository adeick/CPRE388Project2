package com.example.cpre388project2.hacker;

/**
 * Object that represents a current hacker.
 */
public class Hacker {
    private int level;

    /**
     * Constructs default hacker.
     */
    public Hacker() {
        this(1);
    }

    /**
     * Constructs hacker of specified level.
     *
     * @param level Level of hacker.
     */
    public Hacker(int level) {
        this.level = level;
    }

    /**
     * Gets amount of damage hacker should do.
     *
     * @return Amount of damage.
     */
    public int getAttackAmount() {
        return level * 10;
    }

}
