package org.utils;

public class Point {
    public int x;
    public int y;
    public int color;
    public float val;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
        this.color = 0;
    }

    public Point(int x, int y, int c)
    {
        this.x = x;
        this.y = y;
        this.color = c;
    }

    @Override
    public String toString() {
        return "( x: " + x + ", y: " + y + ")" + ", val: " + val;
    }

    public String colormove() {
        String C = this.color == 1 ? "B" : "W";
        if (color == 0)
            return "(" + x + "," + y + ")";
        return C + "(" + x + "," + y + ")";
    }

    public void set_val(float v){
        val = v;
    }

    public float get_val(){
        return val;
    }

    public Point()
    {
        this.x = 0;
        this.y = 0;
    }
}
