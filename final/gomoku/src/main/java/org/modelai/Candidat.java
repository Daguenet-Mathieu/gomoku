package org.modelai;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.utils.DoubleFree;

public class Candidat
{
    public ArrayList<Candidat.coord> lst =  new ArrayList<Candidat.coord>();
    static public DoubleFree doubleFreethree = new DoubleFree();
    //private List<Double> order = Arrays.asList(6.0, 5.0, 4.5, 4.4, 4.0, 3.5, 3.4, 3.0, 2.5, 2.4, 2.0, 1.0, 0.0);
    private List<Double> order = Arrays.asList(6.0, 5.0, 4.0, 3.5, 3.4, 3.0, 2.5, 2.4, 2.0, 1.0, 0.0);
    private static int [][] ddir = {{1, 0}, {0, 1}, {1, 1}, {-1, 1}};

    private static coord limax = new coord(1, 1);
    private static coord limin = new coord(18, 18);
    private int turn;
    public boolean capture_possible;
    
    public static class coord
    {
        public int x;
        public int y;
        public double st;
        public coord(int x, int y){this.x=x;this.y=y;}
        public coord(int x, int y, double st ){this.x = x; this.y = y; this.st=st;}
        public boolean eq(int x, int y){return this.x==x&&this.y==y;}
        public double st(){return this.st;}
    }

    public Candidat(boolean cap)
    {
        this.capture_possible = cap;
    }

    public Candidat(Candidat can)
    {
        this.lst = new ArrayList<Candidat.coord>(can.lst);
    }

    private boolean in_goban(int x, int y)
    {
        if (x >=0 && x < 19 && y >=0 && y < 19)
            return true;
        return false;
    }

    private boolean isplay(int c)
    {
        if (c == 1 || c == 2)
            return true;
        return false;
    }

    private boolean isame(int a, int b)
    {
        if (a == b)
            return a != 0;
        return false;
    }

    private int inner_alignement(int i, int j)
    {
        if (j+1 != 19 && j-1 != -1 && isame(MinMax.map[i][j + 1], MinMax.map[i][j - 1]))
            return MinMax.map[i][j + 1];
        if (i+1 != 19 && i-1 != -1 && isame(MinMax.map[i + 1][j], MinMax.map[i - 1][j]))
            return MinMax.map[i + 1][j];
        if (i + 1 != 19 && i-1 != -1 && j + 1 != 19 && j - 1 != -1 && isame(MinMax.map[i+1][j-1], MinMax.map[i-1][j+1]))
            return MinMax.map[i+1][j-1];
        if (i + 1 != 19 && i-1 != -1 && j + 1 != 19 && j - 1 != -1 && isame(MinMax.map[i-1][j-1], MinMax.map[i+1][j+1]))
            return MinMax.map[i-1][j-1];
            
        return 0;
    }


    private boolean near(int i, int j, int num)
    {
        int cmp = 0;
        if (j+1 != 19 && isplay(MinMax.map[i][j + 1]))
            cmp++;
        if (j-1 != -1 && isplay(MinMax.map[i][j - 1]))
            cmp++;
        if (i+1 != 19 && isplay(MinMax.map[i + 1][j]))
            cmp++;
        if (i-1 != -1 && isplay(MinMax.map[i - 1][j]))
            cmp++;
        if (i+1 != 19 && j-1 != -1 && isplay(MinMax.map[i+1][j-1]))
            cmp++;
        if (i-1 != -1 && j-1 != -1 && isplay(MinMax.map[i-1][j-1]))
            cmp++;
        if (i+1 != 19 && j+1 != 19 && isplay(MinMax.map[i+1][j+1]))
            cmp++;
        if (i-1 != -1 && j+1 != 19 && isplay(MinMax.map[i-1][j+1]))
            cmp++;
        
        if (cmp >= num)
            return true;
        return false;
    }

    private int near_num(int i, int j)
    {
        int cmp = 0;
        if (j+1 != 19 && isplay(MinMax.map[i][j + 1]))
            cmp++;
        if (j-1 != -1 && isplay(MinMax.map[i][j - 1]))
            cmp++;
        if (i+1 != 19 && isplay(MinMax.map[i + 1][j]))
            cmp++;
        if (i-1 != -1 && isplay(MinMax.map[i - 1][j]))
            cmp++;
        if (i+1 != 19 && j-1 != -1 && isplay(MinMax.map[i+1][j-1]))
            cmp++;
        if (i-1 != -1 && j-1 != -1 && isplay(MinMax.map[i-1][j-1]))
            cmp++;
        if (i+1 != 19 && j+1 != 19 && isplay(MinMax.map[i+1][j+1]))
            cmp++;
        if (i-1 != -1 && j+1 != 19 && isplay(MinMax.map[i-1][j+1]))
            cmp++;
        return cmp;
    }

    private boolean near(int i, int j)
    {
        if (j+1 != 19 && isplay(MinMax.map[i][j + 1]))
            return true;
        if (j-1 != -1 && isplay(MinMax.map[i][j - 1]))
            return true;
        if (i+1 != 19 && isplay(MinMax.map[i + 1][j]))
            return true;
        if (i-1 != -1 && isplay(MinMax.map[i - 1][j]))
            return true;
        if (i+1 != 19 && j-1 != -1 && isplay(MinMax.map[i+1][j-1]))
            return true;
        if (i-1 != -1 && j-1 != -1 && isplay(MinMax.map[i-1][j-1]))
            return true;
        if (i+1 != 19 && j+1 != 19 && isplay(MinMax.map[i+1][j+1]))
            return true;
        if (i-1 != -1 && j+1 != 19 && isplay(MinMax.map[i-1][j+1]))
            return true;
        return false;
    }


    private void load_lim(int [][] map)
    {
        for (int i = 0 ; i < 19 ; i++)
        {
            for (int j = 0 ; j < 19 ; j++)
            {
                if (isplay(map[i][j]))
                    save(new coord(i, j));
            }
        }
    }

    private void load_case(int x, int y)
    {
        double tot_case1 = 0;
        double tot_case2 = 0;
        int val;
    
        for (int i = 0 ; i < 4 ; i++)
        {
            if (MinMax.scsimul.str1[i][x][y] == 2)
            {
                if ((in_goban(x-3*ddir[i][0], y-3*ddir[i][1]) && MinMax.map[x-1*ddir[i][0]][y-1*ddir[i][1]] == 1 && MinMax.map[x-3*ddir[i][0]][y-3*ddir[i][1]] == 2) ||
                    (in_goban(x+3*ddir[i][0], y+3*ddir[i][1]) && MinMax.map[x+1*ddir[i][0]][y+1*ddir[i][1]] == 1 && MinMax.map[x+3*ddir[i][0]][y+3*ddir[i][1]] == 2))
                        {        
                            if (Pente.prisoners[0] == 8)
                                tot_case1 = 5;
                            else if (turn == 2)
                                tot_case1 = Math.max(tot_case1, 2.5);
                            else
                                tot_case1= Math.max(tot_case1, 2.4);
                        }
            }

            if (MinMax.scsimul.str2[i][x][y] == 2)
            {
                if ((in_goban(x-3*ddir[i][0], y-3*ddir[i][1]) && MinMax.map[x-1*ddir[i][0]][y-1*ddir[i][1]] == 2 && MinMax.map[x-3*ddir[i][0]][y-3*ddir[i][1]] == 1) ||
                    (in_goban(x+3*ddir[i][0], y+3*ddir[i][1]) && MinMax.map[x+1*ddir[i][0]][y+1*ddir[i][1]] == 2 && MinMax.map[x+3*ddir[i][0]][y+3*ddir[i][1]] == 1) )
                    {

                        if (Pente.prisoners[1] == 8)
                            tot_case1 = 5;
                        else if (turn == 1)
                            tot_case1 = Math.max(tot_case1, 2.5);
                        else
                            tot_case1= Math.max(tot_case1, 2.4);
                    }
            }

            tot_case1 = Math.max(tot_case1, MinMax.scsimul.str1[i][x][y]);
            tot_case2 = Math.max(tot_case2, MinMax.scsimul.str2[i][x][y]);
        }

        val = inner_alignement(x, y);

        if (val == 0 && tot_case1 == 0 && tot_case2 == 0) 
            return;
        if (capture_possible && doubleFreethree.check_double_free(x, y, turn, MinMax.map) == false) //change place ?
            return;


        if (val == 0 && (tot_case1 != 0 || tot_case2 != 0))
            this.lst.add(new Candidat.coord(x,y, Math.max(tot_case1, tot_case2)));
        else if (val == 1)
            this.lst.add(new Candidat.coord(x,y, Math.max(tot_case1 + 1, tot_case2)));
        else if (val == 2)
            this.lst.add(new Candidat.coord(x,y, Math.max(tot_case1, tot_case2 + 1)));
        return;
    }

    public int interesting_candidate(int [][] map)
    {
        for (int i = limin.x - 1 ; i <= limax.x + 1 ; i++)
        {
           for (int j = limin.y - 1 ; j <= limax.y + 1 ; j++)
            {
                if (MinMax.map[i][j] == 0)
                    load_case(i, j);
            }
        }
        return this.lst.size();
    }

    //unused
    public int probable_candidate(int num)
    {
        for (int i = limin.x - 1 ; i <= limax.x + 1 ; i++)
        {
           for (int j = limin.y - 1 ; j <= limax.y + 1 ; j++)
            {
                if (MinMax.map[i][j] == 0 && near(i, j, num) )
                {
                    this.lst.add(new Candidat.coord(i, j, num));
                }
            }
        }
        return this.lst.size();
    }

    public int adding_probable_candidate(int turn)
    {
        int res;
        int x = this.lst.get(0).x;
        int y = this.lst.get(0).y;

        for (int i = limin.x - 1 ; i <= limax.x + 1 ; i++)
        {
            for (int j = limin.y - 1 ; j <= limax.y + 1 ; j++)
            {
                if (x == i && y == j)
                    continue;

                res = near_num(i, j);
                if (MinMax.map[i][j] == 0 && res !=0 && (capture_possible == false || doubleFreethree.check_double_free(i, j, turn, MinMax.map)))
                {
                    this.lst.add(new Candidat.coord(i, j, res));
                }
            }
        }

        Collections.sort(this.lst, Comparator.comparing(item -> 
        this.order.indexOf(item.st())));
        if (this.lst.size() >= Game.min_can  + 1)
            this.lst = new ArrayList<>(this.lst.subList(0, Game.min_can));
        return this.lst.size();
    }

    public int all_probable_candidate(int turn)
    {
        int res;
        for (int i = limin.x - 1 ; i <= limax.x + 1 ; i++)
        {
           for (int j = limin.y - 1 ; j <= limax.y + 1 ; j++)
            {
                res = near_num(i, j);
                if (MinMax.map[i][j] == 0 && res !=0 && (capture_possible == false || doubleFreethree.check_double_free(i, j, turn, MinMax.map)))
                    this.lst.add(new Candidat.coord(i, j, res));
            }
        }

        Collections.sort(this.lst, Comparator.comparing(item -> 
        this.order.indexOf(item.st())));
        if (this.lst.size() >= Game.min_can + 1)
            this.lst = new ArrayList<>(this.lst.subList(0, Game.min_can));
        return this.lst.size();
    }

    public int forced_candidate(ArrayList<Candidat.coord> flst)
    {
        for (int i = 0 ; i < flst.size() ; i++)
            this.lst.add(flst.get(i));
        return lst.size();
    }

    private int nb_candidates(int depth)
    {
        if (depth == Game.max_depth)
            return Game.max_can;
        else if (depth == Game.max_depth - 1)
            return Game.min_can;
        else
            return Game.min_can;
    }

    public int old_load(int depth, int turn) // only used
    {
        int ret;
        this.turn = turn;
        if (depth == 0)
            return 0;

        this.lst.clear();

        if (depth == Game.max_depth)
        {
            load_lim(MinMax.map);
            if (MinMax.forced_capture.size() !=0)
                return forced_candidate(MinMax.forced_capture);
        }

        ret = interesting_candidate(MinMax.map);

        if (ret > 1)
        {
            Candidat.coord can;

            if (depth == Game.max_depth) // Print candidat before sort
            {

                System.out.println("Candidat before sort");
                for (int i = 0 ; i < this.lst.size() ; i++)
                {
                    can = this.lst.get(i);
                    System.out.printf("%d %d %f\n", can.x, can.y, can.st);
                }
            }

            Collections.sort(this.lst, Comparator.comparing(item -> 
            this.order.indexOf(item.st())));

            if (depth == Game.max_depth) // Print candidat after sort
            {
                System.out.println("Candidat after sort");
                for (int i = 0 ; i < this.lst.size() ; i++)
                {
                    can = this.lst.get(i);
                    System.out.printf("%d %d %f\n", can.x, can.y, can.st);
                }

            }

            if (ret >= nb_candidates(depth) + 1)
            {
                this.lst = new ArrayList<>(this.lst.subList(0, nb_candidates(depth)));
                if (depth == Game.max_depth) // Print candidat selected
                {
                    System.out.println("Candidat selected");
                    for (int i = 0 ; i < this.lst.size() ; i++)
                    {
                        can = this.lst.get(i);
                        System.out.printf("%d %d %f\n", can.x, can.y, can.st);
                    }
                }
            }

            return this.lst.size();
        }
        else
        {
            if (ret == 1)
                ret = adding_probable_candidate(turn);
            else
                ret = all_probable_candidate(turn);
        }
        return ret;
    }

    public void save(coord c)
    {
        if (c.x < limin.x)
            limin.x = Math.max(1, c.x);
        if (c.y < limin.y )
            limin.y = Math.max(1, c.y);
        if (c.x > limax.x )
            limax.x = Math.min(17, c.x);
        if (c.y > limax.y )
            limax.y = Math.min(17, c.y);
    }

    //unused
    public int load(int [][] map, coord move, int depth)
    {
        if (depth == 3)
        {
            this.lst.clear();
            for (int i = 0 ; i < 19 ; i++)
            {
                for (int j = 0 ; j < 19 ; j++)
                {
                    if (map[i][j] == 0 && near(i, j))
                    {
                        this.lst.add(new Candidat.coord(i, j));
                    }
                }
            }
            return this.lst.size();
        }

        if (depth !=0)
            this.add(map, move);
        return this.lst.size();
    }


    //unused
    private void add(int [][] map, Candidat.coord c)
    {
        int temp = map[c.x][c.y];
        map[c.x][c.y]=0;
        for (int dx = -1 ; dx != 2 ; dx++)
        {
            for (int dy = -1 ; dy != 2 ; dy++)
            {
                if (in_goban(c.x+dx, c.y+dy) && map[c.x+dx][c.y+dy] == 0 && !near(c.x+dx, c.y+dy))
                {
                    this.lst.add(new Candidat.coord(c.x+dx, c.y+dy));
                }
            }
        }
        this.vanish_one(c.x, c.y);
        map[c.x][c.y]=temp;
    }

    //unused
    public void vanish_one(int x, int y)
    {
        int len;
        len = lst.size();
        for (int i = 0 ; i < len ; i++)
        {
            if (this.lst.get(i).eq(x, y))
            {
                this.lst.remove(i);
                return ;
            }
        }
    }

    //display function
    public void display_candidat(int [][] map)
    {
        System.out.printf("candidat number : %d\n", lst.size());
        int [] [] mapc = new int [19][19];
        for (int i = 0 ; i < 19 ; i++)
        {
            for (int j = 0 ; j < 19 ; j++)
            {
                mapc[i][j] = MinMax.map[i][j];
            }
        }
        for (int i = 0 ; i < this.lst.size() ; i++)
        {
            mapc[lst.get(i).x][lst.get(i).y] = 3;
            System.out.printf("%d %d\n", lst.get(i).x, lst.get(i).y);
        }
        MinMax.display_Map(mapc);
    }
}