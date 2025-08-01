package org.utils;
import org.modelai.*;

public class DoubleFree
{
    static private int [][] dir = {{0, 1}, {1, 0}, {1, 1}, {1, -1}};
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

    public boolean check_double_free(int x, int y, int val, int[][] map)
    {
        int nb_0;
        int dep;
        int nb_val;
        int cur_x;
        int cur_y;
        int nb_free = 0;
    
        final int sep = val == 1 ? 2 : 1;

        for (int i = 0 ; i < 4 ; i++)
        {
            nb_0 = 0;
            dep = 1;
            nb_val = 0;

            if (check_win(x, y, val, i, map))
                return true;

            cur_x = x + dep * dir[i][0];
            cur_y = y + dep * dir[i][1];
            while(in_goban(cur_x, cur_y) && nb_0 != 2 && map[cur_x][cur_y] != sep)
            {
                if (map[cur_x][cur_y] == 0)
                    nb_0++;
                if (map[cur_x][cur_y] == val)
                    nb_val++;

                dep++;
                cur_x = x + dep * dir[i][0];
                cur_y = y + dep * dir[i][1];
            }

            if (in_goban(cur_x, cur_y) && map[cur_x][cur_y] == sep)
            {
                if (!in_goban(cur_x -dir[i][0], cur_y - dir[i][1]) || map[cur_x - dir[i][0]][cur_y - dir[i][1]] !=0)
                    continue;
                if (cur_x-dir[i][0] == x && cur_y - dir[i][1] == y)
                    continue;
            }


            if (in_goban( cur_x - dir[i][0], cur_y - dir[i][1]) && in_goban(cur_x, cur_y) &&
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
                dep++;
                cur_x = x - (dep * dir[i][0]);
                cur_y = y - (dep * dir[i][1]);
            }

            if (in_goban(cur_x, cur_y) && map[cur_x][cur_y] == sep)
            {
                if (!in_goban( + dir[i][0], cur_y + dir[i][1]) ||
                map[cur_x + dir[i][0]][cur_y + dir[i][1]] !=0)
                    continue;
                if (cur_x + dir[i][0] == x && cur_y + dir[i][1] == y)
                    continue;
            }

            if (nb_val == 2)
                nb_free++;

            if (nb_free >= 2)
                return false;
        }
        return true;
    }

    //display function
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
            MinMax.display_Map();
        }
    }
}