package org.model;

import java.util.ArrayList;
import java.util.Map;

public class MinMax
{
    public int [] [] map;
    public int [] [] mapc;
    public Eval ev;
    public Candidat candidat;
    public Candidat.coord best;
    public float [] values;
    //private int depth;

    public class coord
    {
        int x;
        int y;
    }

    public MinMax()
    {
        this.map = new int[19][19];
        this.mapc = new int[19][19];
        this.ev = new Eval();
        this.candidat = new Candidat();
        for (int i = 0 ; i < 19 ; i++)
        {
            for (int j = 0 ; j < 19 ; j++)
            {
                map[i][j] = 0;
                mapc[i][j] = 0;
            }
        }
    }

    public MinMax(int [][]inimap)
    {
        this.map = new int[19][19];
        this.mapc = new int[19][19];
        this.ev = new Eval();
        this.candidat = new Candidat();
        //this.depth = -1;

        for (int i = 0 ; i < 19 ; i++)
        {
            for (int j = 0 ; j < 19 ; j++)
            {
                map[i][j] = inimap[i][j];
                mapc[i][j] = inimap[i][j];
            }
        }
    }

    public void display_map()
    {
        for (int i = 0 ; i < 19 ; i ++)
        {
            for (int j = 0 ; j < 19 ; j++)
            {
                System.out.printf("%d", map[i][j]);
            }
            System.out.println("");
        }
        System.out.println("");
    }

    private void savemapc()
    {
        for (int i = 0 ; i < 19 ; i++)
        {
            for (int j = 0 ; j < 19 ; j++)
            {
                mapc[i][j] = map[i][j];
            }
        }
    }

    private boolean isplay(int c)
    {
        if (c == 1 || c == 2)
            return true;
        return false;
    }

    private boolean near(int i, int j)
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
        if (i-1 != 19 && j+1 != -1 && isplay(map[i-1][j+1]))
            return true;
        return false;
    }


    private void displayco()
    {
        System.out.printf("co   : %d %d %d %d %d \n", ev.co[0], ev.co[1], ev.co[2], ev.co[3], ev.co[4]);
        System.out.printf("unco : %d %d %d %d %d \n", ev.unco[0], ev.unco[1], ev.unco[2], ev.unco[3], ev.unco[4]);

    }

    private void inc_count(int count, int symb)
    {
        if (symb == 1)
        {
            if (count == 10)
                ev.free3 = 1;
            else
                ev.co[(Math.min(count, 5)) - 1] += 1;
        }
        else
        {
            if (count == 10)
                ev.unfree3 = 1;
            else
                ev.unco[Math.min(count, 5) - 1] += 1;
        }
    }

    public void hort(int player)
    {
        int count = 0;
        int symb;
        savemapc();
        int pow;
    
        for (int i = 0 ; i < 19 ; i++)
        {
            for (int j = 0 ; j < 19 ; j++)
            {
                symb = mapc[i][j];
                if (symb == 0)
                    continue;
                pow=0;
                for (int k = j ; k != 19 && mapc[i][k] == symb ; k++)
                {
                    count +=1;
                    mapc[i][k] = -1;
                }
                if (count == 5)
                    ev.add_stones(5, 2, player==symb);
                else if (count == 4)
                {
                    if (j - 1 != -1 && mapc[i][j -1] == 0)
                        pow+=1;
                    if (j + 4 != 19 && mapc[i][j+4] == 0)
                        pow+=1;
                    ev.add_stones(4, pow, player==symb);
                }
                else if (count == 3)
                {
                    if (j - 1 != -1 && mapc[i][j -1] == 0)
                        pow+=1;
                    if (j + 3 != 19 && mapc[i][j+3] == 0)
                        pow+=1;
                    ev.add_stones(3, pow, player==symb);;
                }
                inc_count(count, symb);
                count = 0;
            }
        }
    }

    public void vert(int player)
    {
        int count = 0;
        int symb;
        int pow;
        savemapc();
        for (int i = 0 ; i < 19 ; i++)
        {
            for (int j = 0 ; j < 19 ; j++)
            {
                symb = mapc[i][j];
                if (symb == 0)
                    continue;
                pow = 0;

                for (int k = i ; k != 19 && mapc[k][j] == symb ; k++)
                {
                    count +=1;
                    mapc[k][j] = -1;
                }
                if (count >= 5)
                    ev.add_stones(5, 2, symb == player);
                else if (count == 4)
                {
                    if (i - 1 != -1 && mapc[i - 1][j] == 0)
                        pow+=1;
                    if ( i + 4 != 19 && mapc[i + 4][j] == 0)
                        pow +=1;
                    ev.add_stones(4, pow, symb==player);
                }
                else if (count == 3)
                {
                    if (i - 1 != -1 && mapc[i - 1][j] == 0)
                        pow+=1;
                    if (i + 3 != 19 && mapc[i + 3][j] == 0)
                        pow +=1;
                    ev.add_stones(3, pow, symb==player);
                }
                inc_count(count, symb);
                count = 0;
            }
        }
    }

    public void diag1(int player)
    {
        int count = 0;
        int symb;
        savemapc();
        int pow;

        for (int i = 0 ; i < 19 ; i++)
        {
            for (int j = 0 ; j < 19 ; j++)
            {
                symb = mapc[i][j];
                if (symb == 0)
                    continue;
                pow = 0;
                for (int k = 0 ; i + k != 19 && j + k != 19 && mapc[i + k][j + k] == symb ; k++)
                {
                    count += 1;
                    mapc[i + k][j + k] = -1;
                }
                if (count >= 5)
                    ev.add_stones(5, 2, player==symb);
                else if (count == 4)
                {
                    if (i-1 != -1 && j-1 != -1 && mapc[i-1][j-1] == 0)
                        pow +=1; 
                    if (i + 4 != 19 && j + 4 != 19 && mapc[i+4][j+4] == 0)
                        pow+=1;
                    ev.add_stones(4, pow, player==symb);
                }
                else if (count == 3)
                {
                    if (i-1 != -1 && j-1 != -1 && mapc[i-1][j-1] == 0)
                        pow+=1;
                    if(i + 3 != 19 && j + 3 != 19 && mapc[i+3][j+3] == 0)
                        pow+=1;
                    ev.add_stones(3, pow, player==symb);
                }
                inc_count(count, symb);
                count = 0;
            }
        }
    }

    public void diag2(int player)
    {
        int count = 0;
        int symb;
        int pow;
        savemapc();
        for (int i = 0 ; i < 19 ; i++)
        {
            for (int j = 0 ; j < 19 ; j++)
            {
                symb = mapc[i][j];
                if (symb == 0)
                    continue;
                pow = 0; 
                for (int k = 0 ; i + k != 19 && j - k != -1 && mapc[i + k][j - k] == symb ; k++)
                {
                    count += 1;
                    mapc[i + k][j - k] = -1;
                }
                if (count >= 5)
                    ev.add_stones(5, 2, player==symb);

                if (count == 4)
                {
                    if (i-1 != -1 && j+1 != 19 && mapc[i-1][j+1] == 0)
                        pow+=1; 
                    if (i + 4 != 19 && j - 4 != -1 && mapc[i+4][j-4] == 0)
                        pow+=1;
                    ev.add_stones(4, pow, player==symb);     
                }
                else if (count == 3)
                {
                    if (i-1 != -1 && j+1 != 19 && mapc[i-1][j+1] == 0)
                        pow+=1;
                    if(i + 3 != 19 && j - 3 != -1 && mapc[i+3][j-3] == 0)
                        pow+=1;
                    ev.add_stones(3, pow, player==symb);
                }
                inc_count(count, symb);
                count = 0;
            }
        }
    }

    // public float all_eval(int player)
    // { 
    //     int count = 0;
    //     int symb = 0;

    //     for (int i = 0 ; i < 19 ; i++)
    //     {
    //         for (int j = 0 ; j < 19 ; j++)
    //         {
    //             count = 0;
    //             symb = map[i][j];
    //             if (symb == 0)
    //                 continue;
    //             for (int k = 0 ; i + k != 19 && j - k != -1 && mapc[i + k][j - k] == symb ; k++)
    //             {
    //                 count +=1;
    //             }
    //             if (count >= 5)
    //                 return player == symb ?10000:-9000;
    //         }
    //     }

    //     for (int i = 0 ; i < 19 ; i++)
    //     {
    //         for (int j = 0 ; j < 19 ; j++)
    //         {
    //             count = 0;
    //             symb = map[i][j];
    //             if (symb == 0)
    //                 continue;
    //             for (int k = 0 ; i + k != 19 && j + k != 19 && mapc[i + k][j + k] == symb ; k++)
    //             {
    //                 count +=1;
    //             }
    //             if (count >= 5)
    //                 return player == symb ?10000:-9000;
    //         }
    //     }

    //     for (int i = 0 ; i < 19 ; i++)
    //     {
    //         for (int j = 0 ; j < 19 ; j++)
    //         {
    //             count = 0;
    //             symb = map[i][j];
    //             if (symb == 0)
    //                 continue;
    //             for (int k = j ; k != 19 && map[i][k] == symb ; k++)
    //             {
    //                 count +=1;
    //             }
    //             if (count >= 5)
    //                 return player == symb ?10000:-9000;
    //         }
    //     }
    //     for (int i = 0 ; i < 19 ; i++)
    //     {
    //         for (int j = 0 ; j < 19 ; j++)
    //         {
    //             count = 0;
    //             symb = map[i][j];
    //             if (symb == 0)
    //                 continue;
    //             for (int k = i ; k != 19 && mapc[k][j] == symb ; k++)
    //             {
    //                 count +=1;
    //             }
    //             if (count >= 5)
    //                 return player == symb ?10000:-9000;
    //         }
    //     }

    //     return 0;
    // }

    public float eval(int player)
    {
        hort(player);
        vert(player);
        diag1(player);
        diag2(player);


        return ev.evaluate();
    }

    public void candidat()
    {
        for (int i = 0 ; i < 19 ; i++)
        {
            for (int j = 0 ; j < 19 ; j++)
            {
                if (this.map[i][j] == 0 && near(i, j))
                    this.map[i][j] = 3;
            }
        }
    }

    public void play(Candidat.coord c, int player)
    {
        map[c.x][c.y] = player;
    }

    private int change(int player)
    {
        if (player == 1)
            return 2;
        else
            return 1;
    }

    private float max(float [] val)
    {
        float res = val[0];
        best = candidat.lst.get(0);

        for (int i = 0 ; i < val.length ; i++)
        {
            if (val[i] > res)
            {
                res = val[i];
                best = candidat.lst.get(i);
            }
        }
        return res;
    }

    private float min(float [] val)
    {
        float res = val[0];
        for (int i = 0 ; i < val.length ; i++)
        {
            if (val[i] < res)
            {
                res = val[i];
                best = candidat.lst.get(i);
            }
        }
        return res;
    }

    private void display_values(float [] val, ArrayList<Candidat.coord> lst)
    {
        Candidat.coord c;
        for(int i = 0 ; i < val.length ; i++)
        {
            c = lst.get(i);
            System.out.printf("pos %d %d : %f\n", c.y, c.x, val[i]);
        }
    }

    public float minmax(int depth, int turn, int player)
    {   
        //this.depth = depth;

        if (depth == 0)
        {
            return eval(player);
        }

        candidat.load(map, depth);

        values = new float[candidat.lst.size()];

        for (int i = 0 ; i < candidat.lst.size() ; i++)
        {
            MinMax m = new MinMax(map);
            m.play(candidat.lst.get(i), turn);
            values[i] = m.minmax(depth - 1, change(turn), player);
        }

        if (depth == 3)
        {
            eval(player);
            ev.display();
            display_values(values, candidat.lst);
        }
        if (turn == player)
            return max(values);
        else
            return min(values);
    }

    // public static void main(String[] args)
    // {

    //     MinMax m = new MinMax();

    //     m.map[10][10] = 2;
    //     m.map[9][11] = 1;
    //     m.map[10][11] = 2;
    //     m.map[10][12] = 2;
    //     m.map[8][12] = 1;
        
    //     //m.play(new Candidat.coord(3, 4), 2);
    //     m.display_map();

    //     m.minmax(3, 1);

    //     System.out.printf("The best move is %d %d\n", m.best.x, m.best.y);
    //     m.play(m.best, 1);

    //     m.display_map();

    //     m.play(new Candidat.coord(9, 10), 2);

    //     m.display_map();
    //     m.minmax(3, 1);
    //     System.out.printf("The best move is %d %d\n", m.best.x, m.best.y);
    //     m.play(m.best, 1);
    //     m.display_map();
    // }

}

