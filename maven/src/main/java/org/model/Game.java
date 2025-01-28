package org.model;

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
    public int nb_move;
    public ArrayList<Group> candidate;
    public Float val;
    public long time;
    public ArrayList<Double> timelst;

    public SquareState[][] map;
    //public int [][] mmap;
    public boolean start = true;

    public Game(){
        map = new SquareState[19][19];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                map[i][j] = SquareState.NONE;
            }
        }
        nb_move = 0;
        m = new MinMax();
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

    public void move(Point point, SquareState color)
    {
        if (color == SquareState.BLACK)
            m.map[point.y][point.x] = 1;
        else
            m.map[point.y][point.x] = 2;
        map[point.y][point.x] = color;

        //m.display_map();
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

    public Point best_move(int turn, int player)
    {
        //m.display_map();
        time = System.currentTimeMillis();
        val = m.minmax(3, turn, player);
        time = System.currentTimeMillis() - time;
        timelst.add((double)time / 1000);

        m.play(m.best, turn);
        System.out.printf("IA move at %d %d played in %f seconds mean : %f\n", m.best.y, m.best.x, (double)time / 1000, return_mean_time());
        return new Point(m.best.y, m.best.x);
    }

}