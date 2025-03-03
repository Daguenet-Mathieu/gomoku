package org.utils;
import org.modelai.*;
import java.util.Scanner;

public class NewTest
{
    public Miniscore mscore;
    public MinMax m;
    Scanner scanner = new Scanner(System.in);
    public int [][] dir = {{0, 1}, {1, 0}, {1, 1}, {1, -1}};

    public NewTest()
    {
        this.m = new MinMax();
        this.mscore = new Miniscore();
    }

    public void move(int x, int y, int turn)
    {
        System.out.printf("\nmove %d %d\n", x, y);
        m.play(new Candidat.coord(x, y), turn);
        m.display_map();
        mscore.analyse_move(x, y, turn);
        mscore.display();
        //mscore.display_free();
    }

    public void unmove(int x, int y, int turn)
    {
        System.out.printf("\n unmove %d %d\n", x, y);

        MinMax.map[x][y] = 0;
        mscore.analyse_unmove(x, y, turn);

        m.display_map();
        //mscore.display_free();
        mscore.display();
    }

    private void m_play(int x, int y, int val)
    {
        MinMax.map[x][y] = val; 
    }

    private boolean check_double_free(int x, int y, int val)
    {
        int nb_0;
        int dep;
        int nb_val;
        int cur_x;
        int cur_y;
        int nb_free = 0;
    
        final int sep = val == 1 ? 2 : 1;

        for (int i = 0 ; i < 4 ; i++)
        {
            nb_0 = 0;
            dep = 1;
            nb_val = 0;

            cur_x = x + dep * dir[i][0];
            cur_y = y + dep * dir[i][1];
            while(nb_0 != 2 && MinMax.map[cur_x][cur_y] != sep)
            {
                if (MinMax.map[cur_x][cur_y] == 0)
                    nb_0++;
                if (MinMax.map[cur_x][cur_y] == val)
                    nb_val++;
                dep++;
                cur_x = x + dep * dir[i][0];
                cur_y = y + dep * dir[i][1];
            }

            if (MinMax.map[cur_x][cur_y] == sep)
            {
                if (MinMax.map[cur_x - dir[i][0]][cur_y - dir[i][1]] !=0)
                    continue;
                if (cur_x-dir[i][0] == x && cur_y - dir[i][1] == y)
                    continue;
            }

            if (MinMax.map[cur_x][cur_y] == 0 && MinMax.map[cur_x - dir[i][0]][cur_y - dir[i][1]] == 0)
                nb_0 = 0;
            else
                nb_0 -=1;

            dep = 1;
            cur_x = x - (dep * dir[i][0]);
            cur_y = y - (dep * dir[i][1]);

            while(nb_0 != 2 && MinMax.map[cur_x][cur_y] != sep)
            {
                if (MinMax.map[cur_x][cur_y] == 0)
                    nb_0++;
                if (MinMax.map[cur_x][cur_y] == val)
                    nb_val++;
                dep++;
                cur_x = x - (dep * dir[i][0]);
                cur_y = y - (dep * dir[i][1]);
            }
            if (MinMax.map[cur_x][cur_y] == sep)
            {
                if (MinMax.map[cur_x + dir[i][0]][cur_y + dir[i][1]] !=0)
                    continue;
                if (cur_x + dir[i][0] == x && cur_y + dir[i][1] == y)
                    continue;
            }
            if (nb_val == 2)
            {
                System.out.printf("direction %d free detected\n", i);
                nb_free++;
            }
            if (nb_free == 2)
                return true;
        }

        return false;

    }

    private void check_valid(int x, int y, int val)
    {
        if (MinMax.map[x][y] != 0)
        {
            System.out.printf("Impossible move %d %d, case occuped by %d\n", x, y, MinMax.map[x][y]);
            return;
        }
        if (check_double_free(x, y, val))
            System.out.printf("Double free detetect at %d %d\n", x, y);
        else
        {
            System.out.printf("Move %d %d possible\n", x, y);
            m_play(x, y, val);
            m.display_map();
        }
    }


    public void run()
    {
        //move (10, 10, 1);
        // move (9, 9, 1);
        // move(11, 11, 1);
        // move(8, 8, 2);
        // unmove(8, 8, 2);
        //move (10, 9, 2);

        //test
        // move(7, 9, 2);
        // move(8, 8, 2);
        // move(8, 10, 1);
        // move(9, 9, 1);
        // move(10, 8, 1);
        // move(11, 8, 2);
        // move(7, 10, 1);
        // unmove(7, 10, 1);
        // move(7, 11, 1);
        // move(9, 9, 1);
        // move(10, 8, 1);
        // move(8, 10, 1);
        // move(6, 12, 2);
        // move(7, 11, 1);

        // unmove(7, 11, 1);
        // move(7, 12, 1);



        // move(9, 9, 1);
        // move(9, 10, 2);

        // move(8, 9, 2);
        // move(8, 10, 1);
        // move(7, 10,2);
        // move(6, 11, 2);
        // move(10, 7, 1);
        // move(10, 8, 1);
        // move(9, 8, 1);
        // unmove(9, 8, 1);
        // move(9, 11, 1);



        // m_play(9, 8, 1);
        // m_play(9, 12, 1);
        // //m_play(9, 13, 2);
        // m_play(8, 10, 1);
        // m_play(10, 10, 1);

        m_play(9, 9, 1);
        m_play(9, 10, 1);

        m_play(7, 6, 1);
        m_play(6, 5, 1);
        //m_play(9, 7, 2);

        m.display_map();
        check_valid(9, 8, 1);





        //move(8, 10, 1);
        //move(10, 10, 1);

        //unmove(9, 10, 1);
        //move(9, 10, 1);

        // move(9, 8, 2);
        // unmove(9, 8, 2);
        // move(9, 8, 2);
        


        // move(8, 5, 1);

        // move(10, 6, 1);

        // move(10, 8, 2);
        // move(7, 6, 2);

        //unmove(7, 9, 2);



        //move(6, 9, 2);

        //move(10, 11, 1);
        //move(10, 12, 1);
        //move(10, 13, 1);

        //move(10, 14, 2);
        //unmove(10, 14, 2);
        //move(10, 14, 1);
        //unmove(10,14, 1);
        //move(10, 11, 1);
        //move(10, 14, 1);
        // unmove(10, 11, 1);

        // unmove(10, 12, 1);
        // move(10, 12, 1);
        // move (10, 13, 1);
        // unmove(10, 13, 1);
        // move(10, 13, 1);
        // move(10, 14, 1);
        // unmove(10, 14, 1);

        // unmove (9, 10, 1);
        // move(9, 11, 1);
    }
}