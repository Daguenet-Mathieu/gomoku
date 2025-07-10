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
    public String rules;
    public ArrayList<Double> timelst;
    static public int max_depth;

    public SquareState[][] map;
    //public int [][] mmap;
    public boolean start = true;

    public Game(String rules, int board_size){
        map = new SquareState[19][19];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                map[i][j] = SquareState.NONE;
            }
        }
        nb_move = 0;
        max_depth = 9;
        //max_depth = 3;
        m = minmax_tree(rules); // gomoku.rules
        m.len = 0;
        candidate = new ArrayList<Group>();
        timelst = new ArrayList<Double>();


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
            this.rules = "Gomoku";
            max_depth=10;
            System.out.println("ruleset " + rules);
            return new Moku();
        }
        else if (str == "Pente")
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

    public void move(Point point, int turn)
    {
        //int res;
        MinMax.map[point.y][point.x] = turn;

        scbord.analyse_move(point.y, point.x, turn);
        if (this.rules == "Pente")
        {
            //res = Pente.count_capture(point.y, point.x, turn);
            // if (res!= 0)
            //     System.out.printf("Some captures added point %d %d turn %d: val %d", point.y, point.x, turn, res);
            Pente.prisoners[turn %2] += Pente.count_capture(point.y, point.x, turn);
            m.complete_check_win(point.y, point.x, turn);
            MinMax.map[point.y][point.x] = turn;
        }
        nb_move++;
    }

    public void victory_str(Point p, int dir, int turn)
    {
        for (int i = 1 ; MinMax.map[p.y+i*MinMax.ddir[dir][0]][p.x+i*MinMax.ddir[dir][1]] == turn; i++)
        {
            if (MinMax.map[p.y+i*MinMax.ddir[dir][0]][p.x+i*MinMax.ddir[dir][1]] == 0)
            {
                if (turn == 1)
                    scbord.str1[dir][p.y+i*MinMax.ddir[dir][0]][p.x+i*MinMax.ddir[dir][1]] = 4;
                else
                    scbord.str2[dir][p.y+i*MinMax.ddir[dir][0]][p.x+i*MinMax.ddir[dir][1]] = 4;
            }
        }

        for (int i = 1 ; MinMax.map[p.y-i*MinMax.ddir[dir][0]][p.x-i*MinMax.ddir[dir][1]] == turn; i++)
        {
            if (MinMax.map[p.y-i*MinMax.ddir[dir][0]][p.x-i*MinMax.ddir[dir][1]] == 0)
            {
                if (turn == 1)
                    scbord.str1[dir][p.y-i*MinMax.ddir[dir][0]][p.x-i*MinMax.ddir[dir][1]] = 4;
                else
                    scbord.str2[dir][p.y-i*MinMax.ddir[dir][0]][p.x-i*MinMax.ddir[dir][1]] = 4;
            }
        }
    }



    public void remove(Point point)
    {
        int val = MinMax.map[point.y][point.x];

        //System.out.printf("Point %d %d val %d removed !!!!!??????!!!!!\n", point.y, point.x, val);

        MinMax.map[point.y][point.x] = 0;
        scbord.analyse_unmove(point.y, point.x, val);
        //MinMax.display_Map();
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
        //Candidat.coord c;
        //m.display_map();
        System.out.printf("best move turn %d player %d\n", turn, player);
        //System.exit(0);
        time = System.currentTimeMillis();
        if (nb_move == 0)
        {
            m.best = new Candidat.coord(9, 9);
        }
        // else if (Pente.prisoners[player %2] == 8)
        // {
        //     System.out.println("I have 8 captures !");
        // }
        else
        {
            m.load_cur_score(scbord, turn);
            //MinMax.display_Map();
            //scbord.display();
            MinMax.scsimul.display();
            System.out.printf("\n\tminmax launch turn %d player %d\n", turn, player);
    
            if (this.rules.equals("Pente") || this.rules.equals("Gomoku"))
            {
                // m.display_map();
                // val = m.minmax(max_depth, turn, player);
                // System.out.printf("minmax best move %d %d\n", m.best.y, m.best.x);
                // c = m.best;
                // m.display_map();
                // m.load_cur_score(scbord);
                val = m.minmaxab(max_depth, turn, player, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY);

                System.out.printf("minmaxab best move %d %d\n", m.best.y, m.best.x);
                // if (c.x == m.best.x && c.y == m.best.y)
                // {
                //     System.out.println("minmax and minmax ab : SAME MOVE");
                // }
                // else
                //     System.out.println("minmax and minmax ab : DIFFERENT MOVE");
                // m.display_map();
            }
            else
                val = m.minmax(max_depth, turn, player);
        }
        //MinMax.candidat.clear();
        time = System.currentTimeMillis() - time;
        timelst.add((double)time / 1000);


        //m.play(m.best, turn);
        System.out.printf("IA move %d (Turn %d) at %d %d played in %f seconds (%d pos, %d depth) mean : %f\n", nb_move + 1,(nb_move + 1) / 2 + 1, m.best.y, m.best.x,(double)time / 1000, MinMax.pos_counter, max_depth + 1,return_mean_time());
        if ((double)time/1000 > 0.6 && return_mean_time() > 0.46)
        {
            max_depth = 9;
            System.out.printf("max depth decreesed to %d\n", max_depth + 1);
        }


        return new Point(m.best.y, m.best.x);
    }

}