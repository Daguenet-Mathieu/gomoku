package org.model;
import org.utils.*;


public class Game {
    static public enum SquareState {
        NONE, 
        BLACK,
        WHITE
    }
    public MinMax m;
    public int nb_move;

    public SquareState[][] map;
    public boolean start = true;

    public Game() {
        map = new SquareState[19][19];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                map[i][j] = SquareState.NONE;
            }
        }
        nb_move = 0;

        m = new MinMax();
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

    public Point best_move(int turn, int player)
    {
        m.display_map();
        m.minmax(3, turn, player);
        m.play(m.best, turn);
        return new Point(m.best.y, m.best.x);
    }

}