package org.utils;
import java.util.ArrayList;

import org.model.*;
import org.model.Candidat.coord;


public class Test {
    MinMax m;
    public ArrayList<Test.stone> stones;

    static public class stone 
    {
        int number;
        int strength;
        boolean type;
        String dir="nul";
        ArrayList<Candidat.coord> blanks;
        ArrayList<Candidat.coord> plst;

        public stone(int nm, int str, boolean tp){this.number= nm;this.strength=str;this.type=tp;
        this.plst = new ArrayList<Candidat.coord>();}
        public stone(int nm, int str, boolean tp, String d){this.number= nm;this.strength=str;this.type=tp; this.dir=d;
        this.plst = new ArrayList<Candidat.coord>();}
    }

    public Test()
    {
        this.m = new MinMax();
        this.stones = new ArrayList<Test.stone>();

    }

    public void decrease_stone(Test.stone st, int x, int y, int player)
    {

    }

    public void increase_stone(Test.stone st, int x, int y, int player)
    {
        if (st.number == 1)
        {
            Candidat.coord p = st.plst.get(0);
            System.out.printf("%d %d %d %d\n", p.x, p.y, x, y);
            st.number = 2;
            st.blanks.clear();
            if (x + 1 == p.x && y == p.y)
            {
                if (m.map[x-1][y] == 0)
                    st.blanks.add(new Candidat.coord(x - 1, y));
                if (m.map[x+2][y] == 0)
                    st.blanks.add(new Candidat.coord(x + 2, y));
                st.dir = "ver";
            }
            else if (x + 1 == p.x && y + 1 == p.y )
            {
                if (m.map[x-1][y-1] == 0)
                    st.blanks.add(new Candidat.coord(x - 1, y - 1));
                if (m.map[x+2][y+2] == 0)
                    st.blanks.add(new Candidat.coord(x + 2, y + 2));
                st.dir = "diag1";
            }
            else if (x == p.x && y + 1 == p.y)
            {
                if (m.map[x][y-1] == 0)
                    st.blanks.add(new Candidat.coord(x , y - 1));
                if (m.map[x][y+2] == 0)
                    st.blanks.add(new Candidat.coord(x , y + 2));
                st.dir= "hor";
            }
            else if (x - 1 == p.x && y + 1 == p.y)
            {
                if (m.map[x+1][y-1] == 0)
                    st.blanks.add(new Candidat.coord(x + 1, y - 1));
                if (m.map[x-2][y+2] == 0)
                    st.blanks.add(new Candidat.coord(x - 2, y + 2));
                st.dir= "diag2";
            }
            else if (x - 1 == p.x && y == p.y)
            {
                if (m.map[x+1][y] == 0)
                    st.blanks.add(new Candidat.coord(x + 1, y));
                if (m.map[x-2][y] == 0)
                    st.blanks.add(new Candidat.coord(x - 2, y));
                st.dir= "ver";
            }
            else if (x - 1 == p.x && y - 1 == p.y)
            {
                if (m.map[x+1][y+1] == 0)
                    st.blanks.add(new Candidat.coord(x + 1, y + 1));
                if (m.map[x-2][y-2] == 0)
                    st.blanks.add(new Candidat.coord(x - 2, y - 2));
                st.dir= "diag1";
            }
            else if (x == p.x && y - 1 == p.y)
            {
                if (m.map[x][y+1] == 0)
                    st.blanks.add(new Candidat.coord(x, y + 1));
                if (m.map[x][y-2] == 0)
                    st.blanks.add(new Candidat.coord(x, y - 2));
                st.dir= "hor";
            }
            else if (x + 1 == p.x && y - 1 == p.y)
            {
                if (m.map[x-1][y+1] == 0)
                    st.blanks.add(new Candidat.coord(x - 1, y + 1));
                if (m.map[x+2][y-2] == 0)
                    st.blanks.add(new Candidat.coord(x + 2, y - 2));
                st.dir= "diag2";
            }
            st.plst.add(new Candidat.coord(x, y));
            st.strength = st.blanks.size();
        }

    }

    public void modify_stone(Test.stone st, int x, int y, int player)
    {
        for (int j = 0 ; j < st.blanks.size() ; j++)
        {
            if (st.blanks.get(j).x == x && st.blanks.get(j).y == y)
            {
                if (st.type)
                    increase_stone(st, x, y, player);
                else
                    decrease_stone(st, x, y, player);
            }
        }
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


    private boolean alone(int x, int y)
    {
        if (x + 1 != 19 && m.map[x+1][y] != 0)
        {
            //System.out.printf("1");
            return false;
        }
        if (x - 1 != -1 && m.map[x-1][y] != 0)
        {
            //System.out.println("2");
            return false;
        }

        if (y + 1 != 19 && m.map[x][y+1] != 0)
        {
            //System.out.println("3");
            return false;
        }
        if (y - 1 != -1 && m.map[x][y-1] != 0)
        {
            //System.out.println("4");    
            return false;
        }
        if (x + 1 != 19 && y + 1 != 19 && m.map[x+1][y+1] != 0)
        {
            //System.out.println("5");
            return false;
        }
        if (x + 1 != 19 && y - 1 != -1 && m.map[x+1][y-1] != 0)
        {
            //System.out.println("6");
            return false;
        }
        if (x - 1 != -1 && y + 1 != 19 && m.map[x-1][y+1] != 0)
        {
            //System.out.println("7");
            return false;
        }
        if (x - 1 != -1 && y - 1 != -1 && m.map[x-1][y-1] != 0)
        {
            //System.out.println("8");
            return false;
        }
        return true;

    }

    public void analyse_move(int x, int y, int player)
    {
        if (alone(x, y))
        {
            System.out.println("new stones");
            Test.stone st = new Test.stone(1, 0, player==m.map[x][y]?true:false);
            st.blanks = alone_blanks(x, y);
            st.plst.add(new Candidat.coord(x, y));
            st.strength = 2;
            stones.add(st);
            return ;
        }
        else
        {
            System.out.println("false");
        }

        for (int i = 0 ; i < stones.size() ; i++)
        {
            modify_stone(stones.get(i), x, y, player);
        }
    }

    public void move(int x, int y, int turn, int player)
    {
        m.play(new Candidat.coord(x, y), turn);
        m.display_map();
        analyse_move(x, y, player);
        display();
    }

    public void display()
    {
        Test.stone st;
        for (int i = 0 ; i < this.stones.size() ; i++)
        {
            st = this.stones.get(i);
            System.out.printf("nm %d st %d type %d %s\n", st.number, st.strength, st.type?1:0, st.dir);            
        }
    }

    public void run()
    {
       move(10, 10, 1, 1);
       move(10, 11, 1, 1);
    //    move (11, 10, 1, 1);

    //    move(1, 1, 2, 1);
    //    move(0, 1, 2, 1);

    //    move(8, 8, 1, 1);
    //    move(7, 7, 1, 1);
    }

}