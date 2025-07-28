package org.modelai;

import org.utils.*;
import java.util.ArrayList;

public class Game {

    public MinMax m;
    public Miniscore scbord =  new Miniscore();
    public int nb_move;
    public Float val;
    public long time;
    public String rules;
    public ArrayList<Double> timelst;
    static public int max_depth = 10;
    static public int max_can = 7;
    static public int min_can = 5;
    public Game(String rules, int board_size)
    {
        nb_move = 0;
        m = minmax_tree(rules);
        m.len = 0;
        timelst = new ArrayList<Double>();
    }

    private MinMax minmax_tree(String str)
    {
        str = Character.toUpperCase(str.charAt(0)) + str.substring(1);
        //tree_config(3);
        if ("Gomoku".equals(str))
        {
            this.rules = "Gomoku";
            max_depth=10;
            System.out.println("ruleset " + rules);
            return new Moku();
        }
        else if ("Pente".equals(str))
        {
            this.rules = "Pente";
            System.out.println("ruleset " + rules);
            return new Pente();
        }
        else
        {
            this.rules = "None";
            System.out.println("ruleset " + rules);
            return new MinMax();
        }

    }

    public void tree_config(int lvl)
    {
        if (lvl == 1)
        {
            max_depth = 9;
            max_can = 8;
            min_can = 7;
        }
        else if (lvl == 2)
        {
            max_depth = 10;
            max_can = 7;
            min_can = 5;
        }
        else if (lvl == 3)
        {
            max_depth = 5;
            max_can = 8;
            min_can = 8;
        }
    }  

    public void move(Point point, int turn)
    {
        System.err.println("point : " + point + " turn " + turn);
        MinMax.map[point.y][point.x] = turn;

        scbord.analyse_move(point.y, point.x, turn);
        if (this.rules == "Pente")
        {
            Pente.prisoners[turn %2] += Pente.count_capture(point.y, point.x, turn, false);
            m.complete_check_win(point.y, point.x, turn);
            MinMax.map[point.y][point.x] = turn;
        }
        nb_move++;
    }

    public void remove(Point point, ArrayList<Point> capt, boolean undo)
    {
        int val = MinMax.map[point.y][point.x];

        MinMax.map[point.y][point.x] = 0;
        scbord.analyse_unmove(point.y, point.x, val);

        if (undo)
        {
            if (capt.size() != 0)
            {
                int op = val == 1 ? 2 : 1;
                Point p;
                for (int i = 0 ; i < capt.size() ; i++)
                {
                    p = capt.get(i);
                    MinMax.map[p.y][p.x] = op;
                    Pente.prisoners[op - 1]--;
                    scbord.analyse_move(p.y, p.x, op);
                }
            }
            nb_move --;
        }
    }

    public void reset_minmax()
    {
        for (int i = 0 ; i < 19 ; i++)
            for (int j = 0 ; j < 19 ; j++)
                MinMax.map[i][j] = 0;
        scbord.reset_str();
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

    public Point best_move(int turn, int player)
    {
        // if (player == 1)
        //     Game.max_depth = 9;
        // else
        //     Game.max_depth = 9;

        System.err.printf("Call best_move turn %d player %d et nb move %d\n", turn, player, nb_move);

        time = System.currentTimeMillis();
        if (nb_move == 0)
            m.best = new Candidat.coord(9, 9);
        else
        {
            m.load_cur_score(scbord, turn);
            MinMax.display_Map();
            scbord.display();
            System.out.printf("prisoners[0] : %d, prisoners[1] : %d\n", Pente.prisoners[0], Pente.prisoners[1]);

            if (this.rules.equals("Pente") || this.rules.equals("Gomoku"))
                val = m.minmaxab(max_depth, turn, player, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY);
            else
                val = m.minmax(max_depth, turn, player);
        }

        time = System.currentTimeMillis() - time;
        timelst.add((double)time / 1000);

        best_move_stamp();
    
        return new Point(m.best.y, m.best.x);

    }

    private void best_move_stamp()
    {
        System.out.printf("IA move %d (Turn %d) at %d %d played in %f seconds (%d pos, %d depth) mean : %f\n", nb_move + 1,(nb_move + 1) / 2 + 1, m.best.y, m.best.x,(double)time / 1000, MinMax.pos_counter, max_depth + 1,return_mean_time());
        if ((double)time/1000 > 0.6 && return_mean_time() > 0.46)
        {
            Game.max_depth = 9;
            Game.max_can = 8;
            Game.min_can = 6;
            System.out.printf("max depth decreesed to %d\n", max_depth + 1);
        }
    }
}