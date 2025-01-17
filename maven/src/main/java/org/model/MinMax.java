package org.model;

public class MinMax
{
    public int [] [] map;
    public int [] [] mapc;
    public Eval ev;
    public Candidat candidat;
    public Candidat.coord best;

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
            ev.co[(Math.min(count, 5)) - 1] += 1;
        else
            ev.unco[Math.min(count, 5) - 1] += 1;
    }

    public void hort()
    {
        int count = 0;
        int symb;
        savemapc();
        for (int i = 0 ; i < 19 ; i++)
        {
            for (int j = 0 ; j < 19 ; j++)
            {
                symb = mapc[i][j];
                if (symb == 0)
                    continue;
                for (int k = j ; k != 19 && mapc[i][k] == symb ; k++)
                {
                    count +=1;
                    mapc[i][k] = 0;
                }
                inc_count(count, symb);
                count = 0;
            }
        }
    }

    public void vert()
    {
        int count = 0;
        int symb;
        savemapc();
        for (int i = 0 ; i < 19 ; i++)
        {
            for (int j = 0 ; j < 19 ; j++)
            {
                symb = mapc[i][j];
                if (symb == 0)
                    continue;

                for (int k = i ; k != 19 && mapc[k][j] == symb ; k++)
                {
                    count +=1;
                    mapc[k][j] = 0;
                }
                inc_count(count, symb);
                count = 0;
            }
        }
    }

    public void diag1()
    {
        int count = 0;
        int symb;
        savemapc();
        for (int i = 0 ; i < 19 ; i++)
        {
            for (int j = 0 ; j < 19 ; j++)
            {
                symb = mapc[i][j];
                if (symb == 0)
                    continue;

                for (int k = 0 ; i + k != 19 && j + k != 19 && mapc[i + k][j + k] == symb ; k++)
                {
                    count += 1;
                    mapc[i + k][j + k] = 0;
                }
                inc_count(count, symb);
                count = 0;
            }
        }
    }

    public void diag2()
    {
        int count = 0;
        int symb;
        savemapc();
        for (int i = 0 ; i < 19 ; i++)
        {
            for (int j = 0 ; j < 19 ; j++)
            {
                symb = mapc[i][j];
                if (symb == 0)
                    continue;

                for (int k = 0 ; i + k != 19 && j - k != -1 && mapc[i + k][j - k] == symb ; k++)
                {
                    count += 1;
                    mapc[i + k][j - k] = 0;
                }
                inc_count(count, symb);
                count = 0;
            }
        }
    }

    public float eval(int player)
    {
        hort();
        vert();
        diag1();
        diag2();
        //displayco();
        if (player == 1)
            return  (ev.co[1] + ev.co[2] * 2 + ev.co[3] * 5 + ev.co[4] * 1000) - (ev.unco[1] + ev.unco[2] * 2 + ev.unco[3] * 5 + ev.unco[4] * 1000);
        else
            return  - (ev.co[1] + ev.co[2] * 2 + ev.co[3] * 5 + ev.co[4] * 1000) + (ev.unco[1] + ev.unco[2] * 2 + ev.unco[3] * 5 + ev.unco[4] * 1000);
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

    public float minmax(int depth, int turn, int player)
    {   
        if (depth == 0)
        {
            return eval(player);
        }

        candidat.load(map, depth);

        float [] values = new float[candidat.lst.size()];

        for (int i = 0 ; i < candidat.lst.size() ; i++)
        {
            MinMax m = new MinMax(map);
            m.play(candidat.lst.get(i), turn);
            values[i] = m.minmax(depth - 1, change(turn), player);
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

