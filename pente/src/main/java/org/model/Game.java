package org.model;
import org.utils.*;
import java.util.ArrayList;
import org.model.*;
import javafx.scene.Group;
import javafx.scene.shape.*;


public class Game {
    public MinMax m;
    public int nb_move = 0;
    public ArrayList<Group> candidate;
    public Float val;


    // public SquareState[][] map;
    public GameMap map[][];
    // public ArrayList<Circle> nodes = new ArrayList<Circle>();
    public boolean start = true;
    public int black_prisoners = 0;
    public int white_prisoners = 0;


    public Game(){
        map = new GameMap[19][19];
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                map[i][j] = new GameMap();
            }
        }
        nb_move = 0;
        m = new MinMax();
        m.len = 0;
        candidate = new ArrayList<Group>();
    }

    // public byte[][] getMapAsByteArray() {
    // byte[][] byteMap = new byte[19][19];
    // for (int i = 0; i < map.length; i++) {
    //     for (int j = 0; j < map[i].length; j++) {
    //         switch (map[i][j]) {
    //             case NONE:
    //                 byteMap[i][j] = 0;
    //                 break;
    //             case BLACK:
    //                 byteMap[i][j] = 1;
    //                 break;
    //             case WHITE:
    //                 byteMap[i][j] = 2;
    //                 break;
    //         }
    //     }
    // }
    // return byteMap;
    // }

    public void move(Point point, int color)
    {
        // System.out.println("coucou le move : " + color);
        if (color == 1)
            m.map[point.y][point.x] = 1;
        else
            m.map[point.y][point.x] = 2;

        // m.display_map();
        // System.out.println();
        // System.out.println();
        // System.out.println();

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
        val = m.minmax(3, turn, player);
        m.play(m.best, turn);
        return new Point(m.best.y, m.best.x);
    }

}