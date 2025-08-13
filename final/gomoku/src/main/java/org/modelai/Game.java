package org.modelai;

import org.utils.*;
import java.util.ArrayList;

public class Game {

    public MinMax m;
    public int gameMap[][];
    public Miniscore scbord =  new Miniscore();
    public int nb_move;
    public Float val;
    public long time;
    public String rules;
    public ArrayList<Double> timelstb;
    public ArrayList<Double> timelstw;
    static public int max_depth = 10;
    static public int max_can = 7;
    static public int min_can = 5;
    static public int fast_search = 0;


    public Game(String rules, int board_size)
    {
        gameMap = new int[board_size][board_size];
        nb_move = 0;
        m = minmax_tree(rules);
        m.len = 0;
        timelstb = new ArrayList<Double>();
        timelstw = new ArrayList<Double>();
    }

    private MinMax minmax_tree(String str)
    {
        str = Character.toUpperCase(str.charAt(0)) + str.substring(1);
        if ("Gomoku".equals(str))
        {
            this.rules = "Gomoku";
            max_depth=10;
            return new Moku();
        }
        else if ("Pente".equals(str))
        {
            this.rules = "Pente";
            return new Pente();
        }
        else
        {
            this.rules = "None";
            return new MinMax();
        }
    }

    public void tree_config(int lvl)
    {
        if (lvl == 1)
        {
            max_depth = 10;
            max_can = 9;
            min_can = 5;
            fast_search = 0;
        }
        else if (lvl == 2)
        {
            max_depth = 9;
            max_can = 7;
            min_can = 6;
            fast_search = 0;
        }
        else if (lvl == 3)
        {
            max_depth = 5;
            max_can = 8;
            min_can = 8;
            fast_search = 0;
        }
        else if (lvl == 4)
        {
            max_depth = 9;
            max_can = 8;
            min_can = 7;
            fast_search = 0;
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

            if (val == 1 && timelstb.size() != 0)
                    timelstb.remove(timelstb.size() - 1);
            else if (val == 2 && timelstw.size() != 0)
                    timelstw.remove(timelstw.size() - 1);
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
        Game.fast_search = 0;
        scbord.reset_str();
    }

    public void manage_time(int player)
    {
        if (nb_move >= 8 && return_mean_time(player) > 0.40)
        {
            Game.min_can = Math.max(Game.min_can - 1, 4);
            if (Game.max_can == 9)
                Game.max_can = 8;
            fast_search++;
        }
        if (fast_search != 0 && nb_move >= 4 && return_mean_time(player) < 0.40)
        {
            Game.min_can = Math.min(Game.min_can + 1, Game.max_can);
            fast_search--;
        }
    }

    private double return_mean_time(int player)
    {
        double res = 0;

        if (player == 1)
        {
            for (int i = 0; i < timelstb.size() ; i++)
                res += timelstb.get(i);
            return res / timelstb.size();
        }
        else if (player == 2)
        {
            for (int i = 0; i < timelstw.size() ; i++)
                res += timelstw.get(i);
            return res / timelstw.size();
        }
        return 0;
    }

    private void initialize_map()
    {
        for (int i = 0 ; i < 19 ; i++)
            for (int j = 0 ; j < 19 ; j++)
                MinMax.map[i][j] = gameMap[i][j];
    }

    public Point best_move(int turn, int player, boolean display)
    {
        if (display)
            System.out.printf("Call best_move turn %d player %d et nb move %d\n", turn, player, nb_move);
        
        if (nb_move == 0)
                reset_minmax();

        initialize_map();
     
        if (display)
            display_all_board_info();

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

        time = System.currentTimeMillis() - time;
        if (player == 1)
            timelstb.add((double)time / 1000);
        else
            timelstw.add((double)time / 1000);

        if (display)
            best_move_stamp(player);

        manage_time(player);
    
        return new Point(m.best.y, m.best.x);
    }

    //display function
    private void display_all_board_info()
    {
            MinMax.display_Map();
            scbord.display(false);
            System.out.printf("prisoners[0] : %d, prisoners[1] : %d\n", Pente.prisoners[0], Pente.prisoners[1]);
            System.out.println();
    }

    //display function
    private void best_move_stamp(int  player)
    {
        System.out.printf("IA move %d (Turn %d) at %d %d played in %f seconds (%d pos, %d depth, %d speed) mean : %f\n", nb_move + 1,(nb_move + 1) / 2 + 1, m.best.y, m.best.x,(double)time / 1000, MinMax.pos_counter, max_depth, fast_search, return_mean_time(player));
    }
}