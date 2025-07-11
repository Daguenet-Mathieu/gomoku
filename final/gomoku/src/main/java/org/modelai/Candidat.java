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
    //public ArrayList<Candidat.coord> mandatory = new ArrayList<Candidat.coord>();
    // ArrayList<Integer> vanish_lst = new ArrayList<Integer>();
    //public ArrayList<Candidat.coord> histo = new ArrayList<Candidat.coord>();
    //public ArrayList<Candidat.coord> forced_capture = new ArrayList<Candidat.coord>();
    static public DoubleFree doubleFreethree = new DoubleFree();
    public boolean capture_possible;

    // List<Double> order = Arrays.asList(24.0, 23.0, 22.0, 21.0, 20.0, 19.0, 18.0, 17.0, 16.0, 15.0, 14.0, 13.0
    //                                 ,12.0, 11.0, 10.0, 9.0, 8.0, 7.0, 6.0, 5.0, 4.0, 3.0, 2.5, 1.0, 0.0);
    //List<Double> order = Arrays.asList(0.0, 1.0, 2.0, 3.0, 4.0);
    private List<Double> order = Arrays.asList(6.0, 5.0, 4.0, 3.5, 3.4, 3.0, 2.5, 2.4, 2.0, 1.0, 0.0);
    private static int [][] ddir = {{1, 0}, {0, 1}, {1, 1}, {-1, 1}};

    private static coord limax = new coord(1, 1);
    private static coord limin = new coord(18, 18);
    //private int dp;
    private int turn;
    //static public int [][] cmap;
    
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

    // public void init_limcoord()
    // {
    //     limax = new coord(1, 1);
    //     limin = new coord(18, 18);
    // }

    public Candidat(boolean cap)
    {
        this.capture_possible = cap;
        //this.lst =  new ArrayList<Candidat.coord>();
        //cmap = new int[19][19];
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

    void load_case(int x, int y)
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
        // if (tot_case1 != 0 || tot_case2 != 0)
        // {
        //     this.lst.add(new Candidat.coord(x,y, Math.ma        // if (depth == Game.max_depth && forced_capture.size() != 0)
        // {
        //     Candidat.coord c;
        //     for (int i = 0 ; i < forced_capture.size() ; i++)
        //     {
        //         c = forced_capture.get(i);
        //         System.out.printf(ANSI_RED + "forcedcoord %d %d\n" + ANSI_RESET, c.x, c.y);
        //     }

        // }x(tot_case1, tot_case2)));
        //     return;
        // }
        val = inner_alignement(x, y);

        if (val == 0 && tot_case1 == 0 && tot_case2 == 0) 
            return;
        if (capture_possible && doubleFreethree.check_double_free(x, y, turn, MinMax.map) == false) //change place ?
        {
            // if (this.dp == Game.max_depth)
            // {
            // System.out.printf("Double free at %d %d (%d): eliminated\n", x, y, MinMax.scsimul.cur_turn);
            // //if (x == 8 && y == 9)
            //     MinMax.display_Map();
            // }
            return;
        }



        if (val == 0 && (tot_case1 != 0 || tot_case2 != 0))
        {
            this.lst.add(new Candidat.coord(x,y, Math.max(tot_case1, tot_case2)));
        }
        else if (val == 1)
        {
            this.lst.add(new Candidat.coord(x,y, Math.max(tot_case1 + 1, tot_case2)));
        }
        else if (val == 2)
        {
            this.lst.add(new Candidat.coord(x,y, Math.max(tot_case1, tot_case2 + 1)));
        }

        return;
    }

    public int interesting_candidate(int [][] map)
    {
        for (int i = limin.x - 1 ; i <= limax.x + 1 ; i++)
        //for (int i = 0 ; i < 19 ; i++)
        {
           for (int j = limin.y - 1 ; j <= limax.y + 1 ; j++)
            //for (int j = 0 ; j < 19 ; j++)
            {
                if (MinMax.map[i][j] == 0)
                {
                    load_case(i, j);
                }
            }
        }
        return this.lst.size();
    }

    // public int interesting_candidate(int [][] map, int turn) //check double free for candidates
    // {
    //     for (int i = limin.x - 1 ; i <= limax.x + 1 ; i++)
    //     //for (int i = 0 ; i < 19 ; i++)
    //     {
    //        for (int j = limin.y - 1 ; j <= limax.y + 1 ; j++)
    //         //for (int j = 0 ; j < 19 ; j++)
    //         {
    //             if (MinMax.map[i][j] == 0 && is_case(i, j) && doubleFreethree.check_double_free(i, j, turn))
    //                 this.lst.add(new Candidat.coord(i, j));
    //         }
    //     }
    //     return this.lst.size();
    // }

    // private boolean picked(int i, int j)
    // {
    //     if (this.lst.size() == 0)
    //         return true;

    //     if ((i + j) % 5 == 0)
    //         return true;
    //     return false;

    // }

    public int probable_candidate(int num)
    {
        for (int i = limin.x - 1 ; i <= limax.x + 1 ; i++)
        //for (int i = 0 ; i < 19 ; i++)
        {
           for (int j = limin.y - 1 ; j <= limax.y + 1 ; j++)
            //for (int j = 0 ; j < 19 ; j++)
            {
                if (MinMax.map[i][j] == 0 && near(i, j, num) )
                {
                    this.lst.add(new Candidat.coord(i, j, num));
                    //return 1;
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
        //for (int i = 0 ; i < 19 ; i++)
        {
           for (int j = limin.y - 1 ; j <= limax.y + 1 ; j++)
            //for (int j = 0 ; j < 19 ; j++)
            {
                if (x == i && y == j)
                    continue;

                res = near_num(i, j);
                if (MinMax.map[i][j] == 0 && res !=0 && (capture_possible == false || doubleFreethree.check_double_free(i, j, turn, MinMax.map)))
                {
                    this.lst.add(new Candidat.coord(i, j, res));
                    //return 1;
                }
            }
        }
        Collections.sort(this.lst, Comparator.comparing(item -> 
        this.order.indexOf(item.st())));
        if (this.lst.size() >= 7)
        {
            this.lst = new ArrayList<>(this.lst.subList(0, 6));
        }
        return this.lst.size();
    }

    public int all_probable_candidate(int turn)
    {
        int res;
        for (int i = limin.x - 1 ; i <= limax.x + 1 ; i++)
        //for (int i = 0 ; i < 19 ; i++)
        {
           for (int j = limin.y - 1 ; j <= limax.y + 1 ; j++)
            //for (int j = 0 ; j < 19 ; j++)
            {
                res = near_num(i, j);
                if (MinMax.map[i][j] == 0 && res !=0 && (capture_possible == false || doubleFreethree.check_double_free(i, j, turn, MinMax.map)))
                {
                    this.lst.add(new Candidat.coord(i, j, res));
                    //return 1;
                }
            }
        }
        Collections.sort(this.lst, Comparator.comparing(item -> 
        this.order.indexOf(item.st())));
        if (this.lst.size() >= 7)
        {
            this.lst = new ArrayList<>(this.lst.subList(0, 6));
        }
        return this.lst.size();
    }

    public int forced_candidate(ArrayList<Candidat.coord> flst)
    {
        for (int i = 0 ; i < flst.size() ; i++)
        {
            this.lst.add(flst.get(i));
        }
        return lst.size();
    }

    // private int nb_candidates(int depth)
    // {
    //     if (depth == Game.max_depth)
    //         return 7;
    //         //return 8;
    //     else if (depth == Game.max_depth - 1)
    //         return 6;
    //     else
    //         return 6;
    // }

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
        //int max_near;
        //this.dp = depth;
        this.turn = turn;
        // System.out.println("==================");
        // display_map(map);
        // System.out.println("===================")
        
        if (depth == 0)
            return 0;

        this.lst.clear();


        // this.forced_capture.clear();

        // if (forced_capture.size() != 0)
        // {
        //     this.lst = new ArrayList<Candidat.coord>(forced_capture);
        //     return this.lst.size();
        // }

        if (depth == Game.max_depth)
        {
            load_lim(MinMax.map);
            if (MinMax.forced_capture.size() !=0)
                return forced_candidate(MinMax.forced_capture);
        }

        // if (depth != 1)
        // {
        //     ret = interesting_candidate(map); // only for the 1st maybe
        //     if (ret != 0)
        //         return ret;
        // }

        if (true)
        {
            ret = interesting_candidate(MinMax.map); // only for the 1st maybe


            //System.out.printf("is it interessant ? %d\n", ret);
            if (ret > 1)
            {
                Candidat.coord can;
                if (depth == Game.max_depth)
                {
                //MinMax.scsimul.display();
                System.out.println("Candidat before sort");
                for (int i = 0 ; i < this.lst.size() ; i++)
                {
                    can = this.lst.get(i);
                    System.out.printf("%d %d %f\n", can.x, can.y, can.st);
                }
                }
                Collections.sort(this.lst, Comparator.comparing(item -> 
                this.order.indexOf(item.st())));



                if (depth == Game.max_depth)
                {
                System.out.println("Candidat after sort");
                for (int i = 0 ; i < this.lst.size() ; i++)
                {
                    can = this.lst.get(i);
                    System.out.printf("%d %d %f\n", can.x, can.y, can.st);
                }
                // if (this.lst.size() == 6)
                //     Collections.reverse(this.lst);
                }
                if (ret >= nb_candidates(depth) + 1)
                {
                    //this.lst = new ArrayList<>(this.lst.subList(this.lst.size() - 6, this.lst.size()));
                    this.lst = new ArrayList<>(this.lst.subList(0, nb_candidates(depth)));
                    // while (this.lst.size() != 7)
                    // {
                    //     this.lst.remove(this.lst.size() - 1);
                    // }
                    if (depth == Game.max_depth)
                    {
                    System.out.println("Candidat selected");
                    for (int i = 0 ; i < this.lst.size() ; i++)
                    {
                        can = this.lst.get(i);
                        System.out.printf("%d %d %f\n", can.x, can.y, can.st);
                    }
                    }
                }
                // if (this.lst.size() == 6)
                //     Collections.reverse(this.lst);

                return this.lst.size();
            }
            else
            {
                if (ret == 1)
                {
                    ret = adding_probable_candidate(turn);
                }
                else
                    ret = all_probable_candidate(turn);
            }
        }

        return ret;
    }

    // public int old_load(int depth, int turn)
    // {
    //     int ret;

    //     if (depth == 0)
    //         return 0;

    //     this.lst.clear();
    //     if (depth == Game.max_depth)
    //     {
    //         load_lim(MinMax.map);
    //     }

    //     if (depth == Game.max_depth)
    //     {
    //         ret = interesting_candidate(MinMax.map); // only for the 1st maybe
    //         if (ret != 0)
    //             return ret;
    //     }

    //     for (int i = limin.x - 1 ; i <= limax.x + 1 ; i++)

    //     {
    //        for (int j = limin.y - 1 ; j <= limax.y + 1 ; j++)

    //         {
    //             if (MinMax.map[i][j] == 0 && near(i, j) && doubleFreethree.check_double_free(i, j, turn, MinMax.map))
    //                 this.lst.add(new Candidat.coord(i, j));
    //         }
    //     }

    //     if (this.lst.size() == 0)
    //     {
    //         System.out.println("pas de candidats");
    //         System.exit(0);
    //     }
    
    //     return this.lst.size();

    // }

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
                        //cmap[i][j]=3;
                    }
                }
            }
            return this.lst.size();
        }

        if (depth !=0)
            this.add(map, move);
        return this.lst.size();
    }

    private void display_map(int [][] map)
    {
        for (int i = 0 ; i < 19 ; i ++)
        {
            for (int j = 0 ; j < 19 ; j++)
            {
                System.out.printf("%d", map[i][j]);
            }
            System.out.println("");
        }
    }

    // private void load_cmap()
    // {
    //     cmap = new int[19][19];
    //     for (int i = 0 ; i < lst.size() ; i++)
    //     {
    //         cmap[lst.get(i).x][lst.get(i).y] = 3;
    //     }
    // }

    public boolean in_list(int x, int y)
    {
        for (int i = 0 ; i < this.lst.size() ; i++)
        {
            if (lst.get(i).eq(x, y))
                return true;
        }
        return false;
    }

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
                    System.out.printf("%d %d added\n", c.x+dx, c.y+dy);
                    //cmap[c.x+dx][c.y+dy] = 3;
                }
            }
        }
        this.vanish_one(c.x, c.y);
        map[c.x][c.y]=temp;
    }

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
        //cmap[x][y] = 0;
    }

    // private void vanish_vanish_lst()
    // {
    //     int len;
    //     int idx;

    //     len = vanish_lst.size();
    //     if (len != 0)
    //     {
    //         for (int i = len-1 ; i != -1 ; i--)
    //         {
    //             idx = vanish_lst.get(i);
    //             this.lst.remove(idx);
    //         }
    //     }
    //     vanish_lst.clear();
    // }

    // public void remove(int [][] map, Candidat.coord move)
    // {
    //     int len;
    //     coord c;

    //     len = lst.size();
    //     // if (move.x == 11 && move.y == 11)
    //     // {
    //     //     System.out.println("HEEEEEEEEEEERE");
    //     //     display_map(map);
    //     //     display_candidat(map);
    //     // }
    //     for (int i = 0 ; i < len ; i++)
    //     {
    //         c = this.lst.get(i);
    //         if (!near(c.x, c.y))
    //         {
    //             //System.out.printf("index %d removed\n", i);
    //             //cmap[c.x][c.y]=0;
    //             vanish_lst.add(i);
    //         }
    //     }
    //     vanish_vanish_lst();

    //     this.lst.add(new coord(move.x, move.y));
    //     //cmap[move.x][move.y]=3;
    //     // System.out.printf("after remove %d %d\n", move.x,move.y);
    //     // display_candidat(map);
    //     // System.out.println();
    //     // if (move.x == 11 && move.y ==11)
    //     // {
    //     //     System.exit(0);
    //     // }
    // }

    public void display_candidat(int [][] map)
    {
        System.out.printf("candidat number : %d\n", lst.size());
        int [] [] mapc = new int [19][19];
        //System.arraycopy(map, 0, mapc, 0, 19);
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
        display_map(mapc);
        //display_map(map);
    }
}