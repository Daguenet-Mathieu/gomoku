package org.utils;

import org.modelai.MinMax;
public class Blocker {

    public int [] bl1 = new int[2];
    public int [] bl2 = new int[2];
    public int color;
    public int blockcolor;
    public int dir;
    public int sig;
    public int [][] cases = new int[4][2];
    public int rank;
    static int [][] ddir = {{1, 0}, {0, 1}, {1, 1}, {-1, 1}};

    public void bl1(int x, int y)
    {
        bl1[0] = x; bl1[1] = y;
    }

    public void bl2(int x, int y)
    {
        bl2[0] = x; bl2[1] = y;
    }

    public Blocker(int bcolor, int dir, int sig)
    {
        this.blockcolor = bcolor;
        this.dir = dir;
        this.sig = sig;
        this.rank = 0;

        if (bcolor == 1)
            this.color = 2;
        else
            this.color = 1;
    }

    private boolean in_goban(int x, int y)
    {
        if (x >=0 && x < 19 && y >=0 && y < 19)
            return true;
        return false;
    }

    public void update_block_info()
    {
        rank = 0;
        
        for (int i = 1 ; i < 5 ; i++)
        {
            if (!in_goban(bl1[0]+ddir[dir][0]*sig*i, bl1[1]+ddir[dir][1]*sig*i))
                break;
            if (MinMax.map[bl1[0]+ddir[dir][0]*sig*i][bl1[1]+ddir[dir][1]*sig*i] == 0)
            {
                cases[rank][0] = bl1[0]+ddir[dir][0]*sig*i;
                cases[rank][1] = bl1[1]+ddir[dir][1]*sig*i;
                rank++;
            }
        }
    }
}

