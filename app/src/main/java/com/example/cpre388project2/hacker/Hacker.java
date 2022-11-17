package com.example.cpre388project2.hacker;

public class Hacker {
    private float[] coords;

    /**
     * Create a new hacker
     *
     * @param x Starting x value
     * @param y Starting y value
     */
    public Hacker(float x, float y) {
        coords = new float[]{x, y};
    }

    /**
     * Closes any resources and deletes hacker
     */
    public void kill() {
    }

    /**
     * Moves the hacker to the set value
     *
     * @param x x coordinate of screenspace
     * @param y y coordinate of screenspace
     */
    public void move(float x, float y) {
    }

    /**
     * Get hacker's coordinates on screen
     *
     * @return getCoords[0] = x, getCoords[1] = y
     */
    public float[] getCoords() {
        return coords;
    }
}
