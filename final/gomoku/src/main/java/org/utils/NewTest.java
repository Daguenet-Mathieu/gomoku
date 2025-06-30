package org.utils;
import org.modelai.*;
import java.util.Scanner;

public class NewTest
{
    public Miniscore mscore;
    public Pente m;
    DoubleFree db;
    Scanner scanner = new Scanner(System.in);
    public int [][] dir = {{0, 1}, {1, 0}, {1, 1}, {1, -1}};

    public NewTest()
    {
        this.m = new Pente();
        this.mscore = new Miniscore();
        this.db = new DoubleFree();
    }

    public void move(int x, int y, int turn)
    {
        System.out.printf("\nmove %d %d\n", x, y);
        m.play(new Candidat.coord(x, y), turn);
        m.display_map();
        MinMax.scsimul.display();
        MinMax.scsimul.check_capt();
        //mscore.display_free();
    }

    public void unmove(int x, int y, int turn)
    {
        System.out.printf("\n unmove %d %d\n", x, y);
        m.unplay(new Candidat.coord(x, y), turn);

        m.display_map();
        //mscore.display_free();
        //System.out.println("after");       
        MinMax.scsimul.display();
        MinMax.scsimul.check_capt();
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

    public void check_valid(int x, int y, int val)
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

    public void splay(int x, int y, int player)
    {
        Candidat.coord c = new Candidat.coord(x, y);
        this.m.play(c, player);
        m.display_map();
        MinMax.scsimul.display();
    }

    public void sunplay(int x, int y, int player)
    {
        Candidat.coord c = new Candidat.coord(x, y);
        this.m.unplay(c, player);
        m.display_map();
        MinMax.scsimul.display();
    }


    public void run()
    {


        // move(8, 8, 1);
        // move(8, 9, 1);
        // move(8, 10, 2);
        // move(8, 7, 2);
        // System.out.printf("prisoners %d %d\n", Pente.prisoners[0], Pente.prisoners[1]);
        // unmove(8, 7, 2);
        // System.out.printf("prisoners %d %d\n", Pente.prisoners[0], Pente.prisoners[1]);

        move(9, 8, 2);
        move(7, 8, 1);
        move(6, 8, 2);
        move(8, 8, 1);
        move(8, 9, 1);
        move(8, 10, 1);
        move(8, 11, 1);
        move(8, 12, 1);

        // move(8, 8, 1);
        // move(9, 7, 1);
        // move(10, 6, 2);
        // move(7, 8, 1);
        // move(6, 8, 1);
        // move(5, 8, 1);
        // System.out.println("win move 4 8");
        // MinMax.map[4][8] = 1;
        // MinMax.scsimul.analyse_move(4, 8, 1);
        // m.display_map();
        // MinMax.scsimul.display();
        // //move(4, 8, 1);
        // unmove(8, 8, 1);
        //move(7, 9, 2);

        // move(8, 8, 1);
        // move(9, 8, 1);
        // move(10, 8, 1);
        // move(11, 8, 2);
        // unmove(11, 8, 2);


        // move(15, 3, 1);

        // move(14, 4, 2);
        // move(16, 4, 1);
        // move(14, 2, 2);
        // move(14, 3, 1);
        // move (17, 5, 2);
        // move(16, 4, 1);
        // move(15, 3, 2);


        // move(8, 8, 2);
        // move(4, 12, 1);
        // move(7, 9, 1);
        // move(6, 10, 1);
        // move(5, 11, 1);


        // move (5, 8, 2);
        // move(6, 9, 1);
        // move(7, 4, 1);
        // move(7, 8, 2);
        // move(7, 10, 1);
        // move(7, 12, 2);
        // move(8, 5, 2);
        // move(8, 7, 2);
        // move(8, 8, 1);
        // move(8, 11, 1);
        // move(9, 5, 2);
        // move(9, 6, 2);
        // move(9, 9, 2);
        // move(9, 10, 1);
        // move(10, 5, 2);
        // move(10, 7, 2);
        // move(10, 8, 1);
        // move(10, 9, 1);
        // move(11, 4, 1);
        // move(11, 5, 1);
        // move(11, 8, 2);
        // move(11, 10, 2);
        // move(12, 9, 1);
        // if (db.check_double_free(9, 7, 2, MinMax.map) == true)
        //     System.out.printf("all fine");
        // else
        //     System.out.printf("double free detected");




        //moin1
        //unmove 8-6 (1)
        //unmove 8-7 (2)
        //unmove 9-8 (2)
        //move 7-6 (1)
        // move(0, 0, 1);
        // move(1, 1, 1);
        // move(2, 2, 1);
        // move(4, 4, 2);

        // move(8, 8, 2);
        // move (8, 13, 2);
        // move(8, 9, 1);
        // move(8, 10, 1);
        // move(8, 11, 1);
        // move(8, 12, 1);

        // unmove(8, 10, 1);

        // unmove(8, 8, 2);



        // move(7, 12, 1);
        // move(6, 12, 1);
        // move(5, 12, 2);

        // move(9, 12, 1);
        // move(10, 12, 2);


        // move(7, 13, 2);
        // move(6, 13, 1);
        // move(9, 13, 1);

        // move(8, 13, 2);

        // move(7, 10, 2);
        // move(6, 11, 2);
        // move(5, 12, 2);
        // move(3, 14, 1);
        // move(7, 9, 2);
        // move(8, 8, 1);
        // move(9, 7, 1);
        // move(10, 6, 1);
        // move(11, 5, 1);
        // move(12, 4, 2);
        // unmove(10, 6, 1);

        // unmove(11, 5, 1);

        // apart
        // move(6, 6, 2);
        // move(7,7, 1);
        // move(8, 8, 1);
        // move(8, 9, 2);
        // move(7, 9, 1);
        // move(9, 7, 1);
        // move(10, 6, 1);
        // move(9, 8, 2);
        // move(10, 7, 2);
        // move(10, 8, 2);
        // move(10, 10, 2);
        // move(11, 8, 2);
        // move(11, 9, 2);
        // move(12, 8, 1);

        // //then
        // System.out.println("THEN");
        // unmove(11, 9, 2);
        // move(9, 9, 1);
        // move(10, 9, 1);
        // unmove(7, 9, 1);
        // unmove(10, 7, 2);
        // unmove(10, 9, 1);
        // unmove(9, 9, 1);
        // move(11 ,9, 2);
        // appart

        // move(8, 9, 2);
        // move(8, 10, 2);
        // move(8, 12, 2);
        // move(8, 13, 2);
        //move(8, 12, 2);
        //move(8, 10, 1);

        //history
        // move(7, 8, 1);
        // move (8, 6, 1);
        // move(8, 7, 2);
        // move(8, 8, 2);
        // move(8, 9, 2);
        // move(8, 10, 2);
        // move(9, 7, 1);
        // move(9, 8, 2);
        // move(9, 9, 1);
        // move(10, 7, 1);
        // move(10, 8, 2);
        // move(10, 9, 1);
        // move(11, 8, 1);
        //history
        //move(8, 7, 1);
        // move(9, 10, 2);
        // move(10, 9, 2);
        // move(11, 7, 1);
        // move(11, 8, 1);
        // move(11, 9, 1);

        // unmove(11, 7, 1);




        //history

        // move (8, 6, 1);
        // move(8, 11, 1);
        // //move(8, 11, 1);
        // move(11, 7, 2);
        // move(11, 8, 1);
        // move(11, 9, 1);

        // unmove(8, 11, 1);
        // move(10, 9, 2);
        // move(9, 10, 2);
        // unmove(11, 7, 2);
        // move(11, 10, 2);

        //zero
        // move(8, 8, 2);
        // move(8, 9, 2);
        // move(8, 10, 2);
        // move(8, 11, 1);
        // move(9, 8, 1);
        // move(9, 9, 1);
        // move(9, 10, 1);
        // move(10, 7, 2);
        // move(10, 8, 1);
        // move(11, 8, 1);
        // move(12, 8, 2);

        // unmove(8, 11, 1);
        // unmove(8, 9, 2);
        
        //one
        // move(4, 8, 1); //falc
        // move (5, 8, 1); //falc

        // move(7, 8, 1);
        // move(8, 8, 1);
        // move(9, 8, 1);
        // move(10, 8, 1);

        // move(12, 8, 1);
        // move(13, 8, 1);
        // move(14, 8, 1);

        // unmove(10, 8, 1);
        //two
        // move(8, 8, 1);
        // move(9, 8, 1);
        // move(10, 8, 1);

        // move (6, 8, 1);
        // move(5, 8, 1);
        //endtwo

        //move(12, 8, 1);
        //move(13, 8, 1);
        //move(13, 8, 1);

        //unmove(9, 8, 1);

        // move(6, 7, 1);
        // move(7, 9, 1);
        // move(8, 8, 1);
        // move(9, 7, 1);
        // move(9, 8, 2);
        // move(9, 9, 1);
        // move(10, 6, 2);
        // move(10, 7, 1);
        // move(10, 8, 2);
        // move(8, 7, 2);

        // move (7, 7, 2);
        // move(7, 9, 1);
        // move(9, 7, 1);
        // move(9, 8, 2);
        // move(10,6, 1 );
        // move(10, 8, 2);
        // move(10, 10, 2);
        // move(11, 5, 2);
        // unmove(10, 10, 2);
        // move(9, 9, 1);
        // move(8, 8, 1);

        // move(7, 7, 2);
        // move(7, 9, 1);
        // move(8, 8, 1);
        // move(9, 7, 1);
        // move(9, 8, 2);
        // move(9, 9, 1);
        // move(10, 6, 2);
        // move(10, 7, 2);
        // move(10, 8, 2);
        // move(10, 10, 1);

        // unmove(10, 7, 2);
        // move(7, 7, 2);
        // move(8, 8, 1);
        // move(9, 9, 1);
        // move(10, 8, 2);
        // move(9, 11, 1);
        // move(10, 11, 2);
        // move(11, 11, 2);
        // move(10, 10, 2);
        
        // move(8, 8, 2);
        // move (9, 7, 2);
        // move (10, 7, 1);
        // move(10, 6, 1);
        // unmove(10, 6, 1);
        // move (8, 8, 1);
        // move(8, 9, 1);
        // move(8, 10, 2);
        // unmove(8, 10, 2);



        //move(8,11, 2);

        // Validation

        // move (9, 8, 1);
        // move(9, 7, 2);
        // move(9, 9, 1 );
        // move(9, 10, 1);
        // move(9, 11, 2);
        // move(10, 8, 1);
        // move(10, 10, 1);
        // move(11, 7, 1);
        // move(11, 10, 1);
        // move(11, 11, 1);
        // move(12, 6, 2);
        // move(12, 10, 2);
        // move(12, 12, 2);
        // move(8, 10, 1);
        // move(7, 10, 2);
        // move(7, 11, 2);
        // move (7, 7, 2);

        // db.check_valid(10, 9, 1, MinMax.map);
        // db.check_valid(11, 8, 1, MinMax.map);

        // weird
        // move(6, 5, 1);
        // move(8, 8, 2);
        // move(9, 9, 2);
        // move(7, 7, 1);
        // move(9, 8, 1);
        // move(9, 7, 1);
        // move(7, 9, 2);

        // unmove(7, 7, 1);
        // move(9, 6, 1);
        // weird

        // move(10, 9, 1);
        // move(11, 9, 1);
        // move(12, 9, 1);
        // move(9, 9, 2);
        // unmove(10, 9, 1);
        // move(7, 7, 1);
        // move(6, 6, 2);
        // unmove(7, 7, 1);
        // unmove(8, 8, 1);
        //move(11, 9, 1);

        // move(12, 10, 1);
        // move(7, 9, 2);
        // move(11, 9, 1);
        // move(9, 11, 2);
        // move(9, 7, 1);
        // move(12, 8, 2);
        //unmove(11, 9, 1);
        //unmove(10, 10, 1);
        // splay(5, 9, 2);
        // splay(6, 8, 1);
        // splay(7, 8, 2);
        // splay(8, 9, 1);
        // splay(9, 10, 1);
        // splay(10, 11, 2);
        // sunplay(10, 11, 2);
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


        // m_play(9, 9, 1);
        // m_play(9, 10, 1);

        // m_play(7, 6, 1);
        // m_play(6, 5, 1);
        // //m_play(9, 7, 2);

        // m.display_map();
        // check_valid(9, 8, 1);

        


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