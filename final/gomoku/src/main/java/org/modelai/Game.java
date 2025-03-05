package org.modelai;

import org.utils.*;
import java.util.ArrayList;
import javafx.scene.Group;



public class Game {
    static public enum SquareState {
        NONE, 
        BLACK,
        WHITE
    }
    public MinMax m;
    public Miniscore scbord =  new Miniscore();
    public int nb_move;
    public ArrayList<Group> candidate;
    public Float val;
    public long time;
    public ArrayList<Double> timelst;
    static public int max_depth;

    public SquareState[][] map;
    //public int [][] mmap;
    public boolean start = true;

    public Game(String rules){
        map = new SquareState[19][19];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                map[i][j] = SquareState.NONE;
            }
        }
        nb_move = 0;
        m = minmax_tree(rules); // gomoku.rules
        m.len = 0;
        candidate = new ArrayList<Group>();
        timelst = new ArrayList<Double>();
        max_depth = 5;
        //max_depth = 3;

        //mmap = new int [19][19];
    }

    public byte[][] getMapAsByteArray() {
    byte[][] byteMap = new byte[19][19];
    for (int i = 0; i < map.length; i++) {
        for (int j = 0; j < map[i].length; j++) {
            switch (map[i][j]) {
                case NONE:
                    byteMap[i][j] = 0;
                    break;
                case BLACK:
                    byteMap[i][j] = 1;
                    break;
                case WHITE:
                    byteMap[i][j] = 2;
                    break;
            }
        }
    }
    return byteMap;
    }

    private MinMax minmax_tree(String str)
    {
        if (str == "Gomoku")
        {
            System.out.println("ruleset 1");
            return new Moku();
        }
        else if (str == "Pente")
        {
            System.out.println("ruleset 2");
            return new Pente();
        }
        else
        {

            System.out.println("ruleset 3");
            return new MinMax();
        }
    }

    public void move(Point point, int turn)
    {
        MinMax.map[point.y][point.x] = turn;
        scbord.analyse_move(point.y, point.x, turn);
        nb_move++;
    }

    public void remove(Point point)
    {
        int val = MinMax.map[point.y][point.x];
        MinMax.map[point.y][point.x] = 0;
        scbord.analyse_unmove(point.y, point.x, val);
    }


    public boolean first_move()
    {
        if (start)
        {
            start = false;
            return true;
        }
        return false;
    }

    private double return_mean_time()
    {
        double res = 0;

        for (int i = 0; i < timelst.size() ; i++)
        {
            res += timelst.get(i);
        }
        return res / timelst.size();
    }

    // public Point do_ia_move(int turn, int player)
    // {

    //         //m.display_map();
    //         time = System.currentTimeMillis();
    //         m.load_cur_score(scbord);
    //         val = m.minmax(max_depth, turn, player);
    //         //MinMax.candidat.clear();
    //         time = System.currentTimeMillis() - time;
    //         timelst.add((double)time / 1000);

    //         if ((double)time/1000 > 0.6)
    //         {
    //             max_depth = max_depth == 1 ? 1 : max_depth - 1;
    //             System.out.printf("max depth decreesed to %d\n", max_depth);
    //         }

    //         m.play(m.best, turn);
    //         System.out.printf("IA move at %d %d played in %f seconds (%d pos, %d depth) mean : %f\n", m.best.y, m.best.x,(double)time / 1000, MinMax.pos_counter, max_depth + 1,return_mean_time());
    //         return new Point(m.best.y, m.best.x);
    // }

    public Point best_move(int turn, int player)
    {
        //m.display_map();
        System.out.printf("best move %d %d\n", turn, player);
        //System.exit(0);
        time = System.currentTimeMillis();
        if (nb_move == 0)
        {
            m.best = new Candidat.coord(9, 9);
        }
        else
        {
            m.load_cur_score(scbord);
            System.out.printf("\n\tminmax launch turn %d player %d\n", turn, player);
            val = m.minmax(max_depth, turn, player);

        }
        //MinMax.candidat.clear();
        time = System.currentTimeMillis() - time;
        timelst.add((double)time / 1000);

        if ((double)time/1000 > 0.6)
        {
            max_depth = max_depth == 1 ? 1 : max_depth - 1;
            System.out.printf("max depth decreesed to %d\n", max_depth);
        }

        m.play(m.best, turn);
        System.out.printf("IA move at %d %d played in %f seconds (%d pos, %d depth) mean : %f\n", m.best.y, m.best.x,(double)time / 1000, MinMax.pos_counter, max_depth + 1,return_mean_time());

        return new Point(m.best.y, m.best.x);
    }

}