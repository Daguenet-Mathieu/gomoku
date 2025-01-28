package org.model;

import java.util.ArrayList;
import java.util.Map;

public class MinMax
{
    static public int [] [] map;
    static public int [] [] mapc;
    public Eval ev;
    public Candidat candidat;
    public Candidat.coord best;
    public float [] values;
    public int len;
    //private int depth;

    public class coord
    {
        int x;
        int y;
    }

    public MinMax()
    {
        map = new int[19][19];
        mapc = new int[19][19];
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

    public MinMax(int [][]inimap, int len)
    {
        map = new int[19][19];
        mapc = new int[19][19];
        this.ev = new Eval();
        this.candidat = new Candidat();
        this.len = len;

        for (int i = 0 ; i < 19 ; i++)
        {
            for (int j = 0 ; j < 19 ; j++)
            {
                map[i][j] = inimap[i][j];
                mapc[i][j] = inimap[i][j];
            }
        }
    }

    public MinMax (int len)
    {
        // map = new int[19][19];
        // mapc = new int[19][19];
        this.ev = new Eval();
        this.candidat = new Candidat();
        this.len = len;

    }



    public void display_map()
    {
        for (int i = 0 ; i < 19 ; i ++)
        {
            for (int j = 0 ; j < 19 ; j++)
            {
                System.out.printf("%2d", map[i][j]);
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

        final String dir = "hor";
    
        for (int i = 0 ; i < 19 ; i++)
        {
            for (int j = 0 ; j < 19 ; j++)
            {
                symb = mapc[i][j];
                if (!isplay(symb))
                    continue;
                pow=0;
                for (int k = j ; k != 19 && mapc[i][k] == symb ; k++)
                {
                    count +=1;
                    mapc[i][k] = -1;
                }
                if (count == 5)
                    ev.add_stones(5, 2, player==symb, dir);
                else if (count == 4)
                {
                    if (j - 1 != -1 && mapc[i][j -1] == 0)
                        pow+=1;
                    if (j + 4 != 19 && mapc[i][j+4] == 0)
                        pow+=1;
                    ev.add_stones(4, pow, player==symb, dir);
                }
                else if (count == 3)
                {
                    if (j - 1 != -1 && mapc[i][j -1] == 0)
                        pow+=1;
                    if (j + 3 != 19 && mapc[i][j+3] == 0)
                        pow+=1;
                    ev.add_stones(3, pow, player==symb, dir);
                }
                else if (count == 2)
                {
                    if (j - 1 != -1 && mapc[i][j -1] == 0)
                        pow+=1;
                    if (j + 2 != 19 && mapc[i][j+2] == 0)
                        pow+=1;
                    ev.add_stones(2, pow, player==symb, dir);
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
        final String dir = "ver";
        for (int i = 0 ; i < 19 ; i++)
        {
            for (int j = 0 ; j < 19 ; j++)
            {
                symb = mapc[i][j];
                if (!isplay(symb))
                    continue;
                pow = 0;

                for (int k = i ; k != 19 && mapc[k][j] == symb ; k++)
                {
                    count +=1;
                    mapc[k][j] = -1;
                }
                if (count == 5)
                    ev.add_stones(5, 2, symb == player, dir);
                else if (count == 4)
                {
                    if (i - 1 != -1 && mapc[i - 1][j] == 0)
                        pow+=1;
                    if ( i + 4 != 19 && mapc[i + 4][j] == 0)
                        pow +=1;
                    ev.add_stones(4, pow, symb==player, dir);
                }
                else if (count == 3)
                {
                    if (i - 1 != -1 && mapc[i - 1][j] == 0)
                        pow+=1;
                    if (i + 3 != 19 && mapc[i + 3][j] == 0)
                        pow +=1;
                    ev.add_stones(3, pow, symb==player, dir);
                }
                else if (count == 2)
                {
                    if (i - 1 != -1  && mapc[i-1][j] == 0)
                        pow+=1;
                    if (i + 2 != 19 && mapc[i+2][j] == 0)
                        pow+=1;
                    ev.add_stones(2, pow, player==symb, dir);
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

        final String dir = "diagp";

        for (int i = 0 ; i < 19 ; i++)
        {
            for (int j = 0 ; j < 19 ; j++)
            {
                symb = mapc[i][j];
                if (!isplay(symb))
                    continue;
                pow = 0;
                for (int k = 0 ; i + k != 19 && j + k != 19 && mapc[i + k][j + k] == symb ; k++)
                {
                    count += 1;
                    mapc[i + k][j + k] = -1;
                }
                if (count == 5)
                    ev.add_stones(5, 2, player==symb, dir);
                else if (count == 4)
                {
                    if (i-1 != -1 && j-1 != -1 && mapc[i-1][j-1] == 0)
                        pow +=1; 
                    if (i + 4 != 19 && j + 4 != 19 && mapc[i+4][j+4] == 0)
                        pow+=1;
                    ev.add_stones(4, pow, player==symb, dir);
                }
                else if (count == 3)
                {
                    if (i-1 != -1 && j-1 != -1 && mapc[i-1][j-1] == 0)
                        pow+=1;
                    if(i + 3 != 19 && j + 3 != 19 && mapc[i+3][j+3] == 0)
                        pow+=1;
                    ev.add_stones(3, pow, player==symb, dir);
                }
                else if (count == 2)
                {
                    if (i-1 != -1 && j-1 != -1 && mapc[i-1][j-1] == 0)
                        pow+=1;
                    if(i + 2 != 19 && j + 2 != 19 && mapc[i+2][j+2] == 0)
                        pow+=1;
                    ev.add_stones(2, pow, player==symb, dir);
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
        final String dir = "diagn";
        for (int i = 0 ; i < 19 ; i++)
        {
            for (int j = 0 ; j < 19 ; j++)
            {
                symb = mapc[i][j];
                if (!isplay(symb))
                    continue;
                pow = 0; 
                for (int k = 0 ; i + k != 19 && j - k != -1 && mapc[i + k][j - k] == symb ; k++)
                {
                    count += 1;
                    mapc[i + k][j - k] = -1;
                }
                if (count == 5)
                    ev.add_stones(5, 2, player==symb, dir);

                if (count == 4)
                {
                    if (i-1 != -1 && j+1 != 19 && mapc[i-1][j+1] == 0)
                        pow+=1; 
                    if (i + 4 != 19 && j - 4 != -1 && mapc[i+4][j-4] == 0)
                        pow+=1;
                    ev.add_stones(4, pow, player==symb, dir);     
                }
                else if (count == 3)
                {
                    if (i-1 != -1 && j+1 != 19 && mapc[i-1][j+1] == 0)
                        pow+=1;
                    if(i + 3 != 19 && j - 3 != -1 && mapc[i+3][j-3] == 0)
                        pow+=1;
                    ev.add_stones(3, pow, player==symb, dir);
                }
                else if (count == 2)
                {
                    if (i-1 != -1 && j+1 != 19 && mapc[i-1][j+1] == 0)
                        pow+=1;
                    if(i + 2 != 19 && j - 2 != -1 && mapc[i+2][j-2] == 0)
                        pow+=1;
                    ev.add_stones(2, pow, player==symb, dir);
                }
                inc_count(count, symb);
                count = 0;
            }
        }
    }


    public float eval(int player, int len)
    {
        float ret;
        hort(player);
        vert(player);
        diag1(player);
        diag2(player);


        ret= ev.evaluate(len);
        //ev.clear_stones();
        return ret;
    }

    public void candidat()
    {
        for (int i = 0 ; i < 19 ; i++)
        {
            for (int j = 0 ; j < 19 ; j++)
            {
                if (map[i][j] == 0 && near(i, j))
                    map[i][j] = 3;
            }
        }
    }

    private boolean in_goban(int x, int y)
    {
        if (x >=0 && x < 19 && y >=0 && y < 19)
            return true;
        return false;
    }

    private boolean check_dir(int x, int y, int dx, int dy)
    {
        int player;
        int count;

        player = map[x][y];
        count = 0;

        for (int i=0 , j = 0; in_goban(x+i, y+j) && map[x + i][y + j] == player ; i+=dx, j+=dy)
            count +=1;

        for (int i = -dx, j = -dy ; in_goban(x + i, y + j) && map[x + i][y + j] == player ; i-=dx, j-=dy)
            count+=1;

        if (count == 5)
            return true;
        return false;
    }

    private boolean check_win_4_dir(int x, int y)
    {
        if (check_dir(x,y, 0, 1))
            return true;
        if (check_dir(x, y, 1, 0))
            return true;
        if (check_dir(x, y, 1, 1))
            return true;
        if (check_dir(x, y, 1, -1))
            return true;
        return false;
    }


    public boolean play(Candidat.coord c, int player)
    {

        map[c.x][c.y] = player;

        return check_win_4_dir(c.x, c.y);
        
    }

    public void unplay(Candidat.coord c)
    {
        map[c.x][c.y] = 0;
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

    private float value_victory(int player, int turn, int len)
    {
        if (player == turn)
        {
            if (len == 0)
                return 12000;
            return 10000;
        }
        return -9200;
    }

    public float minmax(int depth, int turn, int player)
    {   
        //this.depth = depth;

        if (depth == 0)
        {
            //display_map();
            return eval(player, len);
        }

        candidat.load(map, depth);

        values = new float[candidat.lst.size()];

        for (int i = 0 ; i < candidat.lst.size() ; i++)
        {
            MinMax m = new MinMax(len + 1);
            if (m.play(candidat.lst.get(i), turn))
                values[i] = value_victory(player, turn, len);
            else
                values[i] = m.minmax(depth - 1, change(turn), player);
            m.unplay(candidat.lst.get(i));
        }

        if (depth == 3)
        {
            eval(player, 0);
            ev.display();
            ev.clear_stones();
            //candidat.display_candidat(map);
            //display_values(values, candidat.lst);
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

