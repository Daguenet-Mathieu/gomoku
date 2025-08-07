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
    static public boolean large_cut = false;
    public int gameMap[][];

    public Game(String rules, int board_size)
    {
        gameMap = new int[board_size][board_size];
        nb_move = 0;
        m = minmax_tree(rules);
        m.len = 0;
        timelst = new ArrayList<Double>();
    }

    public void printGameMap(){
        for (int i = 0; i < gameMap.length ;i++){
            for (int j = 0; j < gameMap.length; j++){
                System.out.print(gameMap[i][j]);
            }
            System.out.println("");
        }
    }

    private MinMax minmax_tree(String str)
    {
        str = Character.toUpperCase(str.charAt(0)) + str.substring(1);
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
            System.err.println("HARD");
            max_depth = 10;
            max_can = 7;
            min_can = 5;
            large_cut = false;
        }
        else if (lvl == 2)
        {
            System.err.println("MEDIUM");
            max_depth = 9;
            max_can = 7;
            min_can = 6;
            large_cut = false;
        }
        else if (lvl == 3)
        {
            System.err.println("EASY");
            max_depth = 4;
            max_can = 8;
            min_can = 8;
            large_cut = false;
        }
        else if (lvl == 4)
        {
            System.err.println("SUPER HARD");
            max_depth = 9;
            max_can = 8;
            min_can = 7;
            large_cut = false;
        }
    }  

    public void move(Point point, int turn)
    {
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
            if (timelst.size() != 0)
                timelst.remove(timelst.size() - 1);
        }
    }

    public void reset_minmax()
    {
        for (int i = 0 ; i < 19 ; i++)
            for (int j = 0 ; j < 19 ; j++)
                MinMax.map[i][j] = 0;
        MinMax.pos_counter =0;
        MinMax.nbmove = 0;
        MinMax.after_capwinsim = true;
        MinMax.forced_capture.clear();
        MinMax.capwin.clear();
        Game.large_cut = false;
        scbord.reset_str();
    }

    public void manage_time()
    {
        if (nb_move >= 4 && return_mean_time() > 0.42)
            large_cut = true;
        if (nb_move >= 4 && return_mean_time() < 0.32)
            large_cut = false;
    }

    private double return_mean_time()
    {
        double res = 0;

        for (int i = 0; i < timelst.size() ; i++)
            res += timelst.get(i);
        return res / timelst.size();
    }

    public Point best_move(int turn, int player, boolean display)
    {
        if (display)
            System.out.printf("Call best_move turn %d player %d et nb move %d\n", turn, player, nb_move);

        time = System.currentTimeMillis();
        if (nb_move == 0)
            m.best = new Candidat.coord(9, 9);
        else
        {
            m.load_cur_score(scbord, turn);

            if (this.rules.equals("Pente") || this.rules.equals("Gomoku"))
                val = m.minmaxab(max_depth, turn, player, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY);
            else
                val = m.minmax(max_depth, turn, player);
        }

        if (display)
            display_all_board_info();

        time = System.currentTimeMillis() - time;
        timelst.add((double)time / 1000);

        if (display)
            best_move_stamp();

        manage_time();
    
        return new Point(m.best.y, m.best.x);
    }

    //display function
    private void display_all_board_info()
    {
            System.out.println("XXX");
            MinMax.display_Map();
            System.out.println("XXX");
            scbord.display(false);
            System.out.printf("prisoners[0] : %d, prisoners[1] : %d\n", Pente.prisoners[0], Pente.prisoners[1]);
    }

    //display function
    private void best_move_stamp()
    {
        System.out.printf("IA move %d (Turn %d) at %d %d played in %f seconds (%d pos, %d depth) mean : %f\n", nb_move + 1,(nb_move + 1) / 2 + 1, m.best.y, m.best.x,(double)time / 1000, MinMax.pos_counter, max_depth, return_mean_time());
    }
}