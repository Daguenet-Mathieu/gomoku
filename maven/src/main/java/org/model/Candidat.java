package org.model;

import java.util.ArrayList;

public class Candidat
{
    ArrayList<Candidat.coord> lst=  new ArrayList<Candidat.coord>(); 
    public static class coord
    {
        public int x;
        public int y;
        public coord(int x, int y){this.x=x;this.y=y;}
    }
    public Candidat(){}

    private boolean isplay(int c)
    {
        if (c == 1 || c == 2)
            return true;
        return false;
    }

    private boolean near(int [] [] map, int i, int j)
    {
        if (j+1 != 19 && isplay(map[i][j + 1]))
            return true;
        if (j-1 != -1 && isplay(map[i][j - 1]))
            return true;
        if (i+1 != 19 && isplay(map[i + 1][j]))
            return true;
        if (i-1 != -1 && isplay(map[i - 1][j]))
            return true;
        if (i+1 != 19 && j-1 != -1 && isplay(map[i+1][j-1]))
            return true;
        if (i-1 != -1 && j-1 != -1 && isplay(map[i-1][j-1]))
            return true;
        if (i+1 != 19 && j+1 != 19 && isplay(map[i+1][j+1]))
            return true;
        if (i-1 != -1 && j+1 != 19 && isplay(map[i-1][j+1]))
            return true;
        return false;
    }

    public void load (int [][] map, int depth)
    {
        // System.out.println("==================");
        // display_map(map);
        // System.out.println("===================");
        this.lst.clear();

        for (int i = 0 ; i < 19 ; i++)
        {
            for (int j = 0 ; j < 19 ; j++)
            {
                if (map[i][j] == 0 && near(map, i, j))
                    this.lst.add(new Candidat.coord(i, j));
            }
        }
            // if (map[9][10] == 2 && depth == 3)
            // {
            //     display_map(map);
            //     display_candidat(map);
            //     System.exit(0);
            // }
    }
    private void display_map(int [][] map)
    {
        for (int i = 0 ; i < 19 ; i ++)
        {
            for (int j = 0 ; j < 19 ; j++)
            {
                System.out.printf("%d", map[i][j]);
            }
            System.out.println("");
        }
    }

    public void display_candidat(int [][] map)
    {
        int [] [] mapc = new int [19][19];
        System.arraycopy(map, 0, mapc, 0, 19);
    
        for (int i = 0 ; i < this.lst.size() ; i++)
        {
            mapc[lst.get(i).x][lst.get(i).y] = 3;
        }
        display_map(mapc);
    }
}