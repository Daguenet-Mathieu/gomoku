package org.utils;

public class Blocker {

    public int [] val = new int[2];
    public int [] bl1 = new int[2];
    public int [] bl2 = new int[2];
    public int str;
    public int player;
    public int blockcolor;
    public int dir;
    

    public void val(int x, int y)
    {
        val[0] = x; val[1] = y;
    }

    public void bl1(int x, int y)
    {
        bl1[0] = x; bl1[1] = y;
    }

    public void bl2(int x, int y)
    {
        bl2[0] = x; bl2[1] = y;
    }

    public Blocker(int nb, int color, int dir)
    {
        this.str = nb;
        this.player=color;
        this.dir = dir;

        if (color == 1)
            blockcolor = 2;
        else
            blockcolor = 1;
    }

}

