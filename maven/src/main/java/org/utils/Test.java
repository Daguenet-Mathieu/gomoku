package org.utils;
import java.util.ArrayList;

import org.model.*;
import org.model.Candidat.coord;


public class Test {
    MinMax m;

    Score sc;
    int [][] str;
    int [][] str1;
    int [][] str2;
    int cur_player;
    int cur_turn;
    int dx;
    int dy;



    public Test()
    {
        this.m = new MinMax();
        this.sc = new Score();
        this.str1 = new int[19][19];
        this.str2 = new int[19][19];
    }

    private ArrayList<Candidat.coord> alone_blanks(int x, int y)
    {
        ArrayList<Candidat.coord> res = new ArrayList<Candidat.coord>();

        if (x + 1 != 19)
            res.add(new Candidat.coord(x + 1, y));
        if (x - 1 != -1)
            res.add(new Candidat.coord(x - 1, y));
        if (y + 1 != 19)
            res.add(new Candidat.coord(x, y + 1));
        if (y - 1 != -1)
            res.add(new Candidat.coord(x, y - 1));
        if (x + 1 != 19 && y + 1 != 19)
            res.add(new Candidat.coord(x + 1, y + 1));
        if (x + 1 != 19 && y-1 != -1)
            res.add(new Candidat.coord(x + 1, y-1));
        if (x - 1 != -1 && y +1 != 19)
            res.add(new Candidat.coord(x - 1, y + 1));
        if (x - 1 != -1 && y - 1 != -1)
            res.add(new Candidat.coord(x - 1, y - 1));

        return res;
    }

    public void display_str()
    {

        for (int i  = 0 ; i < 19 ; i++)
        {
            for (int j = 0 ; j < 19 ; j++)
            {
                System.out.printf("%2d", str1[i][j]);
            }
            System.out.println();
        }
    }

    private boolean in_goban(int x, int y)
    {
        if (x >=0 && x < 19 && y >=0 && y < 19)
            return true;
        return false;
    }

    public void strenght(int x,int y, int st)
    {
        if (x >= 0 && x <= 19 && y >= 0 && y < 19)
        {
                if (m.map[x][y] == 0)
                {
                    if (cur_turn == 1)
                    {
                        sc.one -= str[x][y];
                        str1[x][y] = st;
                        sc.one += st;
                    }
                    else
                    {
                        sc.two -= str[x][y];
                        str[x][y] = st;
                        sc.two += st;
                    }
                }
        }
    }

    public void connect(int x, int y)
    {
        this.str = m.map[x][y] == 1 ? str1 : str2;

        if (str[x][y] == 0)
        {
            //System.out.printf("heeeeeeere %d %d %d\n",x, y, str[x][y]);
            if (in_goban(x-dx, y-dy) && m.map[x-dx][y-dy] == cur_turn)
            {
                strenght(x - 2 * dx, y - 2 * dy, 3);
                strenght(x + 2 * dx, y + 2 * dy, 3);
            }
            else
            {
                strenght(x - dx, y - dy, 2);
                strenght(x + 2 * dx, y + 2 * dy, 2);
            }
        }

        if (Math.abs(str[x][y]) == 2)
        {

            if (in_goban(x-dx, y-dy) && m.map[x-dx][y-dx] == cur_turn)
            {
                strenght(x - 2 * dx, y - 2 * dy, 4);
                strenght(x + 3 * dx, y + 3 * dy, 4);
            }
            else
            {
                strenght(x - dx, y - dy, 3);
                strenght(x + 3 * dx, y + 3 * dy, 3);
            }

        }

        if (Math.abs(str[x][y]) == 3)
        {

            if (in_goban(x-dx, y-dy) && m.map[x-dx][y-dx] == cur_turn)
            {
                sc.victory(cur_turn);
            }
            else
            {
                strenght(x - dx, y - dy, 4);
                strenght(x + 4 * dx, y + 4 * dy, 4);
            }

        }

        if (Math.abs(str[x][y]) == 4)
        {
            sc.victory(cur_turn);
        }

        fill(x,y);

    }

    public void fill(int x, int y)
    {
        sc.one -= (str1[x][y]);
        sc.two -= (str2[x][y]);        
        str1[x][y]=0;
        str2[x][y]=0;
    }

    private boolean is_player(int c)
    {
        if (c == 1 || c == 2)
            return true;
        return false;
    }

    public void analyse_move(int x, int y)
    {
        if (x + 1 != 19 && is_player(m.map[x+1][y]))
        {
            //System.out.println("oh no");
            dx=1;dy=0;
            if (m.map[x+1][y] == cur_turn)
                connect(x, y);
            else
                fill(x, y);

        }
        if (x - 1 != -1 && is_player(m.map[x-1][y]))
        {
            System.out.println("this dir");
            //display();
            dx=-1; dy=0;
            if (m.map[x-1][y] == cur_turn)
                connect(x, y);
            else
                fill(x, y);
        }

        if (y + 1 != 19 && is_player(m.map[x][y+1]))
        {
            dx=0;dy=1;
            if (m.map[x][y+1] == cur_turn)
                connect(x, y);
            else
                fill(x, y);
        }
        if (y - 1 != -1 && is_player(m.map[x][y-1]))
        {
            dx=0;dy=-1;
            if (m.map[x][y-1] == cur_turn)
                connect(x, y);
            else
                fill(x, y);
        }
        if (x + 1 != 19 && y + 1 != 19 && is_player(m.map[x+1][y+1]))
        {
            dx=1;dy=1;
            if (m.map[x+1][y+1] == cur_turn)
                connect(x, y);
            else
                fill(x, y);
        }
        if (x + 1 != 19 && y - 1 != -1 && is_player(m.map[x+1][y-1]))
        {
            dx=1;dy=-1;
            if (m.map[x+1][y-1] == cur_turn)
                connect(x, y);
            else
                fill(x, y);
        }
        if (x - 1 != -1 && y + 1 != 19 && is_player(m.map[x-1][y+1]))
        {
            dx=-1;dy=1;
            if (m.map[x-1][y+1] == cur_turn)
                connect(x, y);
            else
                fill(x, y);
        }
        if (x - 1 != -1 && y - 1 != -1 && is_player(m.map[x-1][y-1]))
        {
            dx=-1;dy=-1;
            if (m.map[x-1][y-1] == cur_turn)
                connect(x, y);
            else
                fill(x, y);
        }
    }

    public void move(int x, int y, int turn, int player)
    {
        System.out.printf("\nmove %d %d\n", x, y);
        cur_player = player;
        cur_turn = turn;
        m.play(new Candidat.coord(x, y), turn);
        m.display_map();
        analyse_move(x, y);
        display();
    }

    public void display() //to adapt
    {

        System.out.printf("score %d %d\n", sc.one, sc.two);
        display_str();        

    }

    public void run()
    {
       move(10, 10, 1, 1);

       move(11, 10, 1, 1);
       move(12, 10, 1, 1);
       move(12, 12, 1, 1);
       move(12, 11, 1, 1);
    //    move(11 ,11, 1, 1);
    //    move(13,10, 2, 1);
    //    move(11, 10, 1,1);
    //    move(11, 11, 1, 1);
    //    move(11, 12, 1, 1);
    //    move(3, 4, 2, 1);s
    //    move(10, 11, 1, 1);
    //    move(4, 3, 2, 1);
    //    move(10,9, 1, 1);
    //    move(6, 6, 2, 1);
    //    move(10, 12, 1, 1);
    //    move (11, 10, 1, 1);

    //    move(1, 1, 2, 1);
    //    move(0, 1, 2, 1);

    //    move(8, 8, 1, 1);
    //    move(7, 7, 1, 1);
    }

}