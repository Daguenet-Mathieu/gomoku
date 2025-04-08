package org.utils;
import org.modelai.*;

public class DoubleFree
{
    static private int [][] dir = {{0, 1}, {1, 0}, {1, 1}, {1, -1}};
    private MinMax m;
    private static int goban_size = 19;

    private void m_play(int x, int y, int val)
    {
        MinMax.map[x][y] = val; 
    }

    private boolean in_goban(int x, int y)
    {
        if (x >=0 && x < goban_size && y >=0 && y < goban_size)
            return true;
        return false;
    }

    public boolean check_double_free(int x, int y, int val)
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

            cur_x = x + dep * dir[i][0];
            cur_y = y + dep * dir[i][1];
            while(in_goban(cur_x, cur_y) && nb_0 != 2 && MinMax.map[cur_x][cur_y] != sep)
            {
                if (MinMax.map[cur_x][cur_y] == 0)
                    nb_0++;
                if (MinMax.map[cur_x][cur_y] == val)
                    nb_val++;
                dep++;
                cur_x = x + dep * dir[i][0];
                cur_y = y + dep * dir[i][1];
            }

            if (in_goban(cur_x, cur_y) && MinMax.map[cur_x][cur_y] == sep)
            {
                if (!in_goban(cur_x -dir[i][0], cur_y - dir[i][1]) 
                || MinMax.map[cur_x - dir[i][0]][cur_y - dir[i][1]] !=0)
                    continue;
                if (cur_x-dir[i][0] == x && cur_y - dir[i][1] == y)
                    continue;
            }

            if ( in_goban( cur_x - dir[i][0], cur_y - dir[i][1]) && in_goban(cur_x, cur_y) &&
                 MinMax.map[cur_x][cur_y] == 0 && MinMax.map[cur_x - dir[i][0]][cur_y - dir[i][1]] == 0)
                nb_0 = 0;
            else
                nb_0 -=1;

            dep = 1;
            cur_x = x - (dep * dir[i][0]);
            cur_y = y - (dep * dir[i][1]);

            while( in_goban(cur_x, cur_y) && nb_0 != 2 && MinMax.map[cur_x][cur_y] != sep)
            {
                if (MinMax.map[cur_x][cur_y] == 0)
                    nb_0++;
                if (MinMax.map[cur_x][cur_y] == val)
                    nb_val++;
                dep++;
                cur_x = x - (dep * dir[i][0]);
                cur_y = y - (dep * dir[i][1]);
            }
            if (in_goban(cur_x, cur_y) && MinMax.map[cur_x][cur_y] == sep)
            {
                if (!in_goban( + dir[i][0], cur_y + dir[i][1]) ||
                MinMax.map[cur_x + dir[i][0]][cur_y + dir[i][1]] !=0)
                    continue;
                if (cur_x + dir[i][0] == x && cur_y + dir[i][1] == y)
                    continue;
            }
            if (nb_val == 2)
            {
                //System.out.printf("direction %d free detected\n", i);
                nb_free++;
            }
            if (nb_free >= 2)
                return false;
        }

        return true;

    }

    public void check_valid(int x, int y, int val)
    {
        if (MinMax.map[x][y] != 0)
        {
            System.out.printf("Impossible move %d %d, case occuped by %d\n", x, y, MinMax.map[x][y]);
            return;
        }
        if (check_double_free(x, y, val) == false)
            System.out.printf("Double free detetect at %d %d\n", x, y);
        else
        {
            System.out.printf("Move %d %d possible\n", x, y);
            m_play(x, y, val);
            m.display_map();
        }
    }
}