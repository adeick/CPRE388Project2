package com.example.cpre388project2.firewall;

public class Firewall {
    private float[] coords;

    /**
     * Create a new firewall
     *
     * @param x Starting x value
     * @param y Starting y value
     */
    public Firewall(float x, float y) {
        coords = new float[]{x, y};
    }

    /**
     * Get firewall's coordinates on screen
     *
     * @return getCoords[0] = x, getCoords[1] = y
     */
    public float[] getCoords() {
        return coords;
    }
}
