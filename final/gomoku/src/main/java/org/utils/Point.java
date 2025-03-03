package org.utils;

public class Point {
    public int x;
    public int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    public Point()
    {
        this.x = 0;
        this.y = 0;
    }
}
