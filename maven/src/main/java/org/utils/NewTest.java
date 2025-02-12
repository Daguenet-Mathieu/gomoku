package org.utils;
import org.model.*;

public class NewTest
{
    public Miniscore mscore;
    public MinMax m;

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
    }

    public void unmove(int x, int y, int turn)
    {
        System.out.printf("\n unmove %d %d\n", x, y);
        MinMax.map[x][y] = 0;
        mscore.analyse_unmove(x, y, turn);
        m.display_map();
        mscore.display();
    }

    public void run()
    {
        move (10, 10, 1);
        move (9, 9, 1);
        move(11, 11, 1);
        move(8, 8, 2);
        unmove(8, 8, 2);
        //unmove (9, 10, 1);
        //move(9, 11, 1);
    }
}