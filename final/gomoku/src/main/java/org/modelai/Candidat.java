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
    public ArrayList<Integer> vanish_lst = new ArrayList<Integer>();
    public ArrayList<Candidat.coord> histo = new ArrayList<Candidat.coord>();
    public DoubleFree doubleFreethree;
    List<Double> order = Arrays.asList(24.0, 23.0, 22.0, 21.0, 20.0, 19.0, 18.0, 17.0, 16.0, 15.0, 14.0, 13.0
                                    ,12.0, 11.0, 10.0, 9.0, 8.0, 7.0, 6.0, 5.0, 4.0, 3.0, 2.5, 1.0, 0.0);

    static coord limax = new coord(1, 1);
    static coord limin = new coord(18, 18);
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

    public Candidat()
    {
        this.doubleFreethree = new DoubleFree();
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
    private boolean inner_alignement(int i, int j)
    {
        if (j+1 != 19 && j-1 != -1 && isame(MinMax.map[i][j + 1], MinMax.map[i][j - 1]))
            return true;
        if (i+1 != 19 && i-1 != -1 && isame(MinMax.map[i + 1][j], MinMax.map[i - 1][j]))
            return true;
        if (i + 1 != 19 && i-1 != -1 && j + 1 != 19 && j - 1 != -1 && isame(MinMax.map[i+1][j-1], MinMax.map[i-1][j+1]))
            return true;
        if (i + 1 != 19 && i-1 != -1 && j + 1 != 19 && j - 1 != -1 && isame(MinMax.map[i-1][j-1], MinMax.map[i+1][j+1]))
            return true;
            
        return false;
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

    public void clear()
    {
        this.lst.clear();
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
        int tot_case1 = 0;
        int tot_case2 = 0;
    
        for (int i = 0 ; i < 4 ; i++)
        {
            tot_case1 += MinMax.scsimul.str1[i][x][y];
            tot_case2 += MinMax.scsimul.str2[i][x][y];

        }
        if (tot_case1 != 0)
        {
            this.lst.add(new Candidat.coord(x,y, tot_case1));
            return;
        }
        else if (tot_case2 != 0)
        {
            this.lst.add(new Candidat.coord(x,y, tot_case2));
            return;
        }

        if (inner_alignement(x, y))
            this.lst.add(new Candidat.coord(x, y, 3));
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
                    this.lst.add(new Candidat.coord(i, j));
                    return 1;
                }
            }
        }
        return this.lst.size();
    }

    public int old_load(int depth)
    {
        int ret;
        int max_near;
        // System.out.println("==================");
        // display_map(map);
        // System.out.println("===================")
        
        if (depth == 0)
            return 0;

        this.lst.clear();
        if (depth == Game.max_depth)
        {
            load_lim(MinMax.map);
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
            if (ret != 0)
            {
                Collections.sort(this.lst, Comparator.comparing(item -> 
                this.order.indexOf(item.st())));
                return ret;
            }
            else
                {
                    max_near = 3;
                    while(ret == 0)
                    {
                        ret = probable_candidate(max_near);
                        max_near--;
                    }
                    // System.out.println("zeroooo for interesting");
                }
        }

        return ret;

        // for (int i = limin.x - 1 ; i <= limax.x + 1 ; i++)
        // //for (int i = 0 ; i < 19 ; i++)
        // {
        //    for (int j = limin.y - 1 ; j <= limax.y + 1 ; j++)
        //     //for (int j = 0 ; j < 19 ; j++)
        //     {
        //         if (MinMax.map[i][j] == 0 && near(i, j))
        //             this.lst.add(new Candidat.coord(i, j));
        //     }
        // }





        // // if (depth <= 2)
        // // {
        // //     for (int i = limin.x - 1 ; i <= limax.x + 1 ; i++)
        // //     {
        // //         for (int j = limin.y -1 ; j < limax.y + 1 ; j++)
        // //         {
        // //             if (map[i][j] == 0 && is_case(i, j))
        // //                 this.lst.add(new Candidat.coord(i, j)); 
        // //         }
        // //     }
        // //     if (this.lst.size() != 0)
        // //         return this.lst.size();
        // // }

        // //System.out.printf("limitation %d %d %d %d\n", limin.x - 1, limax.x + 1 , limin.y - 1, limax.y + 1);

        // for (int i = limin.x - 1 ; i <= limax.x + 1 ; i++)
        // //for (int i = 0 ; i < 19 ; i++)
        // {
        //    for (int j = limin.y - 1 ; j <= limax.y + 1 ; j++)
        //     //for (int j = 0 ; j < 19 ; j++)
        //     {
        //         if (MinMax.map[i][j] == 0 && near(i, j))
        //             this.lst.add(new Candidat.coord(i, j));
        //     }
        // }
        //     // if (map[9][10] == 2 && depth == 3)
        //     // {
        //     //     display_map(map);
        //     //     display_candidat(map);
        //     //     System.exit(0);
        //     // }
        // //System.out.printf("Size of candidates %d\n", this.lst.size());
        // return this.lst.size();

    }

    public int old_load(int depth, int turn)
    {
        int ret;

        
        if (depth == 0)
            return 0;

        this.lst.clear();
        if (depth == Game.max_depth)
        {
            load_lim(MinMax.map);
        }

        if (depth == Game.max_depth)
        {
            ret = interesting_candidate(MinMax.map); // only for the 1st maybe
            if (ret != 0)
                return ret;
        }

        for (int i = limin.x - 1 ; i <= limax.x + 1 ; i++)

        {
           for (int j = limin.y - 1 ; j <= limax.y + 1 ; j++)

            {
                if (MinMax.map[i][j] == 0 && near(i, j) && doubleFreethree.check_double_free(i, j, turn))
                    this.lst.add(new Candidat.coord(i, j));
            }
        }

        if (this.lst.size() == 0)
        {
            System.out.println("pas de candidats");
            System.exit(0);
        }
    
        return this.lst.size();

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

    private void vanish_vanish_lst()
    {
        int len;
        int idx;

        len = vanish_lst.size();
        if (len != 0)
        {
            for (int i = len-1 ; i != -1 ; i--)
            {
                idx = vanish_lst.get(i);
                this.lst.remove(idx);
            }
        }
        vanish_lst.clear();
    }

    public void remove(int [][] map, Candidat.coord move)
    {
        int len;
        coord c;

        len = lst.size();
        // if (move.x == 11 && move.y == 11)
        // {
        //     System.out.println("HEEEEEEEEEEERE");
        //     display_map(map);
        //     display_candidat(map);
        // }
        for (int i = 0 ; i < len ; i++)
        {
            c = this.lst.get(i);
            if (!near(c.x, c.y))
            {
                //System.out.printf("index %d removed\n", i);
                //cmap[c.x][c.y]=0;
                vanish_lst.add(i);
            }
        }
        vanish_vanish_lst();

        this.lst.add(new coord(move.x, move.y));
        //cmap[move.x][move.y]=3;
        // System.out.printf("after remove %d %d\n", move.x,move.y);
        // display_candidat(map);
        // System.out.println();
        // if (move.x == 11 && move.y ==11)
        // {
        //     System.exit(0);
        // }
    }

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