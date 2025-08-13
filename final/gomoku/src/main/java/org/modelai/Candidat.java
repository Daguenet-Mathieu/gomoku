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
    private List<Double> order = Arrays.asList(6.0, 5.0, 4.0, 4.8, 4.7, 3.8, 3.7, 3.5, 3.4, 3.0, 2.8, 2.5, 2.4, 2.0, 1.0, 0.0);
    private static int [][] ddir = {{1, 0}, {0, 1}, {1, 1}, {-1, 1}};
    private static int [][][] opencan = {{{0, -1}, {1, -1}, {1, 0}}, {{-1, 0}, {-1, -1}, {0, -1}}, {{-1, 0}, {-1, 1}, {0, 1}}, {{0, 1}, {1, 1}, {1, 0}}};

    private static coord limax = new coord(1, 1);
    private static coord limin = new coord(18, 18);
    static  public boolean display = false; 
    private int turn;
    private int seuil;
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

    private boolean nearv(int i, int j, int val)
    {
        if (j+1 != 19 && MinMax.map[i][j + 1] == val)
            return true;
        if (j-1 != -1 && MinMax.map[i][j - 1] == val)
            return true;
        if (i+1 != 19 && MinMax.map[i + 1][j] == val)
            return true;
        if (i-1 != -1 && MinMax.map[i - 1][j] == val)
            return true;
        if (i+1 != 19 && j-1 != -1 && MinMax.map[i+1][j-1] == val)
            return true;
        if (i-1 != -1 && j-1 != -1 && MinMax.map[i-1][j-1] == val)
            return true;
        if (i+1 != 19 && j+1 != 19 && MinMax.map[i+1][j+1] == val)
            return true;
        if (i-1 != -1 && j+1 != 19 && MinMax.map[i-1][j+1] == val)
            return true;
        return false;
    }

    public void reload_lim()
    {
        limax = new coord(1, 1);
        limin = new coord(18, 18);
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

    private void adding_can(int x, int y, double val)
    {
        if (val >= 3)
            this.seuil+=1;

        this.lst.add(new Candidat.coord(x,y, val));
    }

    private void load_case(int x, int y, int depth)
    {
        double tot_case1 = 0;
        double tot_case2 = 0;
        int val;
    
        for (int i = 0 ; i < 4 ; i++)
        {
            if (capture_possible && MinMax.scsimul.str1[i][x][y] == 2)
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

            if (capture_possible && MinMax.scsimul.str2[i][x][y] == 2)
            {
                if ((in_goban(x-3*ddir[i][0], y-3*ddir[i][1]) && MinMax.map[x-1*ddir[i][0]][y-1*ddir[i][1]] == 2 && MinMax.map[x-3*ddir[i][0]][y-3*ddir[i][1]] == 1) ||
                    (in_goban(x+3*ddir[i][0], y+3*ddir[i][1]) && MinMax.map[x+1*ddir[i][0]][y+1*ddir[i][1]] == 2 && MinMax.map[x+3*ddir[i][0]][y+3*ddir[i][1]] == 1) )
                    {

                        if (Pente.prisoners[1] == 8)
                            tot_case2 = 5;
                        else if (turn == 1)
                            tot_case2 = Math.max(tot_case2, 2.5);
                        else
                            tot_case2= Math.max(tot_case2, 2.4);
                    }
            }

            if (capture_possible && MinMax.scsimul.str1[i][x][y] == 3)
            {
                if (in_goban(x+ddir[i][0], y + ddir[i][1]) && MinMax.map[x+ddir[i][0]][y+ddir[i][1]] == 0)
                {
                    if (nearv(x, y, 2))
                        adding_can(x+ddir[i][0], y+ddir[i][1], 3);
                }
            }
            else if (capture_possible && MinMax.scsimul.str2[i][x][y] == 3)
            {
                if (in_goban(x+ddir[i][0], y + ddir[i][1]) && MinMax.map[x+ddir[i][0]][y+ddir[i][1]] == 0)
                {
                    if (nearv(x, y, 1))
                        adding_can(x+ddir[i][0], y+ddir[i][1], 3);
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
            adding_can(x, y, Math.max(tot_case1, tot_case2));
        else if (val == 1)
            adding_can(x, y, Math.max(tot_case1 + 1, tot_case2));
        else if (val == 2)
            adding_can(x, y, Math.max(tot_case1, tot_case2 + 1));
        return;
    }

    public int interesting_candidate(int depth)
    {
        this.seuil = 0;
        for (int i = limin.x - 1 ; i <= limax.x + 1 ; i++)
        {
           for (int j = limin.y - 1 ; j <= limax.y + 1 ; j++)
            {
                if (MinMax.map[i][j] == 0)
                    load_case(i, j, depth);
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

    private boolean in_candidat_lst(int i, int j)
    {
        for (int k = 0 ; k < this.lst.size() ; k++)
        {
            if (this.lst.get(k).x==i && this.lst.get(k).y==j)
                return true;
        }
        return false;
    }

    public int adding_probable_candidate(int dp, int turn, int ret)
    {
        int res;

        for (int k = 0 ; k < this.lst.size() ; k++)
        {
            if (dp == Game.max_depth)
                this.lst.get(k).st = 5;
        }


        for (int i = limin.x - 1 ; i <= limax.x + 1 ; i++)
        {
            for (int j = limin.y - 1 ; j <= limax.y + 1 ; j++)
            {
                if (in_candidat_lst(i, j))
                    continue;

                res = near_num(i, j);
                if (MinMax.map[i][j] == 0 && res !=0 && (capture_possible == false || doubleFreethree.check_double_free(i, j, turn, MinMax.map)))
                    this.lst.add(new Candidat.coord(i, j, res));
            }
        }

        Collections.sort(this.lst, Comparator.comparing(item -> 
        this.order.indexOf(item.st())));
        if (dp == Game.max_depth && display)
                display_candidat("before Select");
        if (this.lst.size() >= Game.min_can  + 1)
        {
            if (ret == 3)
                this.lst = new ArrayList<>(this.lst.subList(0, 4));
            else     
                this.lst = new ArrayList<>(this.lst.subList(0, Game.min_can + 1));
        }

        if (dp == Game.max_depth && display)
                display_candidat("Candidat Selected");
        return this.lst.size();
    }

    private int numcan(int i, int j)
    {
        if (i <= 9 && j >= 9)
            return 0;
        else if (i > 9 && j >= 9)
            return 1;
        else if (i > 9 && j < 9)
            return 2;
        else
            return 3;
    }

    public int one_move_candidate()
    {
        int num;
        for (int i = limin.x - 1 ; i <= limax.x + 1 ; i++)
        {
           for (int j = limin.y - 1 ; j <= limax.y + 1 ; j++)
            {
                if (MinMax.map[i][j] == 1)
                {
                    if (i !=0 && j !=0 && i != 18 && j != 18)
                    {
                        num = numcan(i, j);
                        this.lst.clear();

                        if (i > j)
                            this.lst.add(new Candidat.coord(i+opencan[num][0][0], j+opencan[num][0][1], 1));
                        else
                            this.lst.add(new Candidat.coord(i+opencan[num][2][0], j+opencan[num][2][1], 1));

                        this.lst.add(new Candidat.coord(i+opencan[num][1][0], j+opencan[num][1][1], 1));
                        return this.lst.size();
                    }
                }
            }
        }
        return 0;
    }

    public int two_move_candidate()
    {
        Candidat.coord b = new Candidat.coord(0, 0);
        Candidat.coord w = new Candidat.coord(0, 0);


        for (int i = limin.x - 1 ; i <= limax.x + 1 ; i++)
        {
           for (int j = limin.y - 1 ; j <= limax.y + 1 ; j++)
            {
                if (MinMax.map[i][j] == 1)
                {
                    b.x = i; b.y=j;
                }
                else if (MinMax.map[i][j] == 2)
                {
                    w.x = i; w.y = j;
                }
            }
        }

        if (Math.abs(w.x - b.x) >=2 || Math.abs(w.y - b.y) >= 2)
        {
            this.lst.clear();
            for (int k = 0 ; k < 3 ; k++)
                this.lst.add(new Candidat.coord(8, 8+k, 1));
            return 3;
        }
        return 0;
    }

    public int all_probable_candidate(int turn, int depth)
    {
        int res;
        int nb_mv = 0;

        for (int i = limin.x - 1 ; i <= limax.x + 1 ; i++)
        {
           for (int j = limin.y - 1 ; j <= limax.y + 1 ; j++)
            {
                res = near_num(i, j);
                if (MinMax.map[i][j] == 1 || MinMax.map[i][j] == 2)
                    nb_mv++;
                if (MinMax.map[i][j] == 0 && res !=0 && (capture_possible == false || doubleFreethree.check_double_free(i, j, turn, MinMax.map)))
                    this.lst.add(new Candidat.coord(i, j, res));
            }
        }

            if (nb_mv == 1 && one_move_candidate() != 0)
                return this.lst.size();

            if (nb_mv == 2 && two_move_candidate() != 0)
                return this.lst.size();

        Collections.sort(this.lst, Comparator.comparing(item -> 
        this.order.indexOf(item.st())));



        if (this.lst.size() >= Game.min_can + 1)
        {
            if (nb_mv >=5)
            {
                if (limax.x - limin.x > 14 || limax.y - limin.y > 14)
                    this.lst = new ArrayList<>(this.lst.subList(0, 3));
                else
                    this.lst = new ArrayList<>(this.lst.subList(0, Game.min_can));
            }
            else
            {
                if (limax.x - limin.x > 14 || limax.y - limin.y > 14)
                    this.lst = new ArrayList<>(this.lst.subList(0, Game.min_can));
                else
                    this.lst = new ArrayList<>(this.lst.subList(0, Game.min_can + 1));
            }
        }
        return this.lst.size();
    }

    public int forced_candidate(ArrayList<Candidat.coord> flst)
    {
        this.lst.add(flst.get(0));
        MinMax.forced_capture.remove(0);
        return 1;
    }

    private int nb_candidates(int depth)
    {
        if (this.seuil > Game.min_can)
        {
            if (depth == Game.max_depth)
                return Math.min(this.seuil, Game.max_can +1);
            else
                return Math.min(this.seuil, 10);
        }
        if (depth == Game.max_depth)
            return Game.max_can;
        else if (depth == Game.max_depth - 1 || depth == Game.max_depth - 2)
            return Game.min_can + 1;
        else
            return Game.min_can;
    }

    public int old_load(int depth, int turn)
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

        ret = interesting_candidate(depth);

        if (ret > 3)
        {
            if (display && depth == Game.max_depth)
                display_candidat("Candidat before sort");

            Collections.sort(this.lst, Comparator.comparing(item -> 
            this.order.indexOf(item.st())));

            if (display && depth == Game.max_depth)
                display_candidat("Candidat after sort");

            if (ret >= nb_candidates(depth) + 1)
            {
                this.lst = new ArrayList<>(this.lst.subList(0, nb_candidates(depth)));
                if (display && depth == Game.max_depth)
                    display_candidat("Candidat selected");
            }
            return this.lst.size();
        }
        else
        {
            if (ret == 0)
                ret = all_probable_candidate(turn, depth);
            else
                ret = adding_probable_candidate(depth, turn, ret);
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

    //display function
    public void display_candidat_map(int [][] map)
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

    //display function
    public void display_candidat(String msg)
    {
        Candidat.coord can;
        System.out.println(msg);
        for (int i = 0 ; i < this.lst.size() ; i++)
        {
             can = this.lst.get(i);
             System.out.printf("%d %d %f\n", can.x, can.y, can.st);
        }
    }
}