package org.utils;
//import org.interfacegui.Map;
import org.modelai.*;

public class DoubleFree
{
    static private int [][] dir = {{0, 1}, {1, 0}, {1, 1}, {1, -1}};
    //hor vertical diagpos diagneg
    private MinMax m;
    private static int goban_size = 19;

    private void m_play(int x, int y, int val, int[][] map )
    {
        map[x][y] = val; 
    }

    private boolean in_goban(int x, int y)
    {
        if (x >=0 && x < goban_size && y >=0 && y < goban_size)
            return true;
        return false;
    }

    private boolean check_win(int x, int y, int val, int i, int [][]map )
    {
        int cur_x;
        int cur_y;
        int dep = 1;
        int res = 0;
    
        cur_x = x + dep * dir[i][0];
        cur_y = y + dep * dir[i][1];
        while(in_goban(cur_x, cur_y) && map[cur_x][cur_y] == val)
            {
                res++;
                dep++;
                cur_x = x + dep * dir[i][0];
                cur_y = y + dep * dir[i][1];
            }

        dep = -1;
        cur_x = x + dep * dir[i][0];
        cur_y = y + dep * dir[i][1];
         while(in_goban(cur_x, cur_y) && map[cur_x][cur_y] == val)
            {
                res++;
                dep--;
                cur_x = x + dep * dir[i][0];
                cur_y = y + dep * dir[i][1];
            }
        if (res>=4)
            return true;
        return false;
    }

    public boolean check_double_free4(int x, int y, int val, int[][] map)
    {
        int nb_0;
        int dep;
        int nb_val;
        int cur_x;
        int cur_y;
        int nb_free = 0;
    
        final int sep = val == 1 ? 2 : 1;

        // if (x == 10 && y == 8)
        // {
        //     //Map.printMap(map);
        //     // System.out.printf("%d %d %d \n", x, y, val);
        // }

        for (int i = 0 ; i < 4 ; i++)
        {
            //System.out.printf("direction %d\n", i);
            nb_0 = 0;
            dep = 1;
            nb_val = 0;

            if (check_win(x, y, val, i, map)) //maybe check if 4 more accurate
                return true;

            cur_x = x + dep * dir[i][0];
            cur_y = y + dep * dir[i][1];
            while(in_goban(cur_x, cur_y) && nb_0 != 2 && map[cur_x][cur_y] != sep)
            {
                if (map[cur_x][cur_y] == 0)
                    nb_0++;
                if (map[cur_x][cur_y] == val)
                    nb_val++;
                // if (x == 10 && y == 8)
                //     System.out.printf("%d %d val %d \n",cur_x, cur_y, map[cur_x][cur_y]);

                dep++;
                cur_x = x + dep * dir[i][0];
                cur_y = y + dep * dir[i][1];
            }

            //System.out.printf("dir %d plus %d %d\n", i, nb_val, nb_0);


            //System.out.printf("nb_val %d %d\n", nb_val, i);
            if (in_goban(cur_x, cur_y) && map[cur_x][cur_y] == sep)
            {
                // if (!in_goban(cur_x -dir[i][0], cur_y - dir[i][1]) // not true if 4 forbiden
                // || map[cur_x - dir[i][0]][cur_y - dir[i][1]] !=0)
                // {
                //     System.out.printf("direction %d, yeah\n", i);
                //     continue;
                // }
                if (cur_x-dir[i][0] == x && cur_y - dir[i][1] == y)
                {
                    //System.out.printf("direction %d, yeah 2", i);
                    continue;
                }
            }
            //System.out.printf("nb_val %d %d\n", nb_val, i);

            if ( in_goban( cur_x - dir[i][0], cur_y - dir[i][1]) && in_goban(cur_x, cur_y) &&
                 map[cur_x][cur_y] == 0 && map[cur_x - dir[i][0]][cur_y - dir[i][1]] == 0)
                nb_0 = 0;
            else
                nb_0 -=1;

            dep = 1;
            cur_x = x - (dep * dir[i][0]);
            cur_y = y - (dep * dir[i][1]);

            while( in_goban(cur_x, cur_y) && nb_0 != 2 && map[cur_x][cur_y] != sep)
            {
                if (map[cur_x][cur_y] == 0)
                    nb_0++;
                if (map[cur_x][cur_y] == val)
                    nb_val++;
                // if (x == 10 && y == 8)
                //     System.out.printf("%d %d val %d\n",cur_x, cur_y, map[cur_x][cur_y]);
                dep++;
                cur_x = x - (dep * dir[i][0]);
                cur_y = y - (dep * dir[i][1]);
            }

            //System.out.printf("dir %d plus %d %d\n", i, nb_val, nb_0);

            if (in_goban(cur_x, cur_y) && map[cur_x][cur_y] == sep)
            {
                // if (!in_goban( + dir[i][0], cur_y + dir[i][1]) || not true if 4 forbidden // not true if 4 forbidden
                // map[cur_x + dir[i][0]][cur_y + dir[i][1]] !=0)
                //     continue;
                if (cur_x + dir[i][0] == x && cur_y + dir[i][1] == y)
                    continue;
            }

            // if (x == 10 && y == 8)
            //     System.out.printf("nb val dir %d %d\n", i, nb_val);
            if (nb_val >= 2) // >=2 break, weird !
            {

                nb_free++;
                // if (x == 10 && y == 8)
                //     System.out.printf("direction %d free detected nb_free\n", i, nb_free);

            }
            //System.out.printf("bilan nb_val %d %d\n", nb_val, i);

            // if (nb_free == 2)
            //     System   .out.printf("x == %d y == %d\n", x, y);

            if (nb_free >= 2)
                return false;
        }

        return true;

    }

    public boolean check_double_free(int x, int y, int val, int[][] map)
    {
        int nb_0;
        int dep;
        int nb_val;
        int cur_x;
        int cur_y;
        int nb_free = 0;
    
        final int sep = val == 1 ? 2 : 1;

        // if (x == 8 && y == 5)
        // {
        //     System.out.printf("val %d", val);
        //     MinMax.display_Map();
        //     // System.out.printf("%d %d %d \n", x, y, val);
        // }

        for (int i = 0 ; i < 4 ; i++)
        {
            //System.out.printf("direction %d\n", i);
            nb_0 = 0;
            dep = 1;
            nb_val = 0;

            if (check_win(x, y, val, i, map)) //maybe check if 4 more accurate
                return true;

            cur_x = x + dep * dir[i][0];
            cur_y = y + dep * dir[i][1];
            while(in_goban(cur_x, cur_y) && nb_0 != 2 && map[cur_x][cur_y] != sep)
            {
                if (map[cur_x][cur_y] == 0)
                    nb_0++;
                if (map[cur_x][cur_y] == val)
                    nb_val++;
                // if (x == 10 && y == 8)
                //     System.out.printf("%d %d val %d \n",cur_x, cur_y, map[cur_x][cur_y]);

                dep++;
                cur_x = x + dep * dir[i][0];
                cur_y = y + dep * dir[i][1];
            }

            //System.out.printf("dir %d plus %d %d\n", i, nb_val, nb_0);


            //System.out.printf("nb_val %d %d\n", nb_val, i);
            if (in_goban(cur_x, cur_y) && map[cur_x][cur_y] == sep)
            {
                if (!in_goban(cur_x -dir[i][0], cur_y - dir[i][1]) // not true if 4 forbiden
                || map[cur_x - dir[i][0]][cur_y - dir[i][1]] !=0)
                {
                    //System.out.printf("direction %d, yeah\n", i);
                    continue;
                }
                if (cur_x-dir[i][0] == x && cur_y - dir[i][1] == y)
                {
                    //System.out.printf("direction %d, yeah 2", i);
                    continue;
                }
            }
            //System.out.printf("nb_val %d %d\n", nb_val, i);

            if ( in_goban( cur_x - dir[i][0], cur_y - dir[i][1]) && in_goban(cur_x, cur_y) &&
                 map[cur_x][cur_y] == 0 && map[cur_x - dir[i][0]][cur_y - dir[i][1]] == 0)
                nb_0 = 0;
            else
                nb_0 -=1;

            dep = 1;
            cur_x = x - (dep * dir[i][0]);
            cur_y = y - (dep * dir[i][1]);

            while( in_goban(cur_x, cur_y) && nb_0 != 2 && map[cur_x][cur_y] != sep)
            {
                if (map[cur_x][cur_y] == 0)
                    nb_0++;
                if (map[cur_x][cur_y] == val)
                    nb_val++;
                // if (x == 10 && y == 8)
                //     System.out.printf("%d %d val %d\n",cur_x, cur_y, map[cur_x][cur_y]);
                dep++;
                cur_x = x - (dep * dir[i][0]);
                cur_y = y - (dep * dir[i][1]);
            }

            //System.out.printf("dir %d plus %d %d\n", i, nb_val, nb_0);

            if (in_goban(cur_x, cur_y) && map[cur_x][cur_y] == sep)
            {
                if (!in_goban( + dir[i][0], cur_y + dir[i][1]) || // not true if 4 forbidden
                map[cur_x + dir[i][0]][cur_y + dir[i][1]] !=0)
                    continue;
                if (cur_x + dir[i][0] == x && cur_y + dir[i][1] == y)
                    continue;
            }

            // if (x == 10 && y == 8)
            //     System.out.printf("nb val dir %d %d\n", i, nb_val);
            if (nb_val == 2) // >=2 break, weird !
            {

                nb_free++;
                // if (x == 10 && y == 8)
                //     System.out.printf("direction %d free detected nb_free\n", i, nb_free);

            }
            //System.out.printf("bilan nb_val %d %d\n", nb_val, i);

            // if (nb_free == 2)
            //     System   .out.printf("x == %d y == %d\n", x, y);

            if (nb_free >= 2)
                return false;
        }

        return true;

    }

    public void check_valid(int x, int y, int val, int[][] map)
    {
        if (map[x][y] != 0)
        {
            System.out.printf("Impossible move %d %d, case occuped by %d\n", x, y, map[x][y]);
            return;
        }
        if (check_double_free(x, y, val, map) == false)
            System.out.printf("Double free detetect at %d %d\n", x, y);
        else
        {
            System.out.printf("Move %d %d possible\n", x, y);
            m_play(x, y, val, map);
            m.display_map();
        }
    }
}