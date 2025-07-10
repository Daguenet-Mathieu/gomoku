package org.modelai;

import java.util.ArrayList;

public class MinMax
{
    static public int [] [] map;
    static public int [] [] mapc;
    //public ArrayList<Candidat.coord> simu =  new ArrayList<Candidat.coord>(); //to be removed
    //static public int [] [] cmap;
    public Eval ev;
    public Candidat candidat;

    public Candidat.coord best;
    public Candidat.coord move;
    public float [] values;
    public int len = 0;
    static public Miniscore scsimul;
    static int pos_counter;
    static int nbmove;
    static int [] [] ddir = {{1, 0}, {0, 1}, {1, 1}, {1, -1}};
    static public ArrayList<Candidat.coord> forced_capture =  new ArrayList<Candidat.coord>();

    //public int [] [] imap = new int[19][19];

    // public float vmin;
    // public float vmax;
    // public float alpha;
    // public float beta;
    //private int depth;

    // public void display_simu()
    // {
    //     Candidat.coord c;
    //     for (int i = 0 ; i < simu.size() ; i++)
    //     {
    //         c = simu.get(i);
    //         if (c.x < 0)
    //             System.out.printf("unmove %d %d \n", -c.x, -c.y);
    //         else
    //             System.out.printf("move %d %d \n", c.x, c.y);
    //     }
    // }

    public class coord
    {
        int x;
        int y;
    }

    public void load_cur_score(Miniscore score, int turn)
    {
        scsimul.cur_turn = turn;
        for (int d = 0 ; d < 8 ; d++)
        {
            for(int i = 0 ; i < 19 ; i++)
            {
                for (int j = 0 ; j < 19 ; j++)
                {
                    scsimul.str1[d][i][j] = score.str1[d][i][j];
                    scsimul.str2[d][i][j] = score.str2[d][i][j];  
                }
            }
        }
        scsimul.sc.one = score.sc.one;
        scsimul.sc.two = score.sc.two;
        scsimul.capt[0] = score.capt[0];
        scsimul.capt[1] = score.capt[1];
        scsimul.lastcap = false;

        // for (int i = 0 ; i < 2 ; i++)
        // {
        //     scsimul.free3[i] = score.free3[i];
        //     scsimul.free4[i] = score.free4[i];
        //     scsimul.simp4[i] = score.simp4[i]; // compute free
        // }
        pos_counter=0;
        nbmove += 1;

        System.out.println("After load");
        //scsimul.display();
    }

    public MinMax()
    {
        // map = new int[19][19];
        // mapc = new int[19][19];
        // //cmap = new int[19][19];
        // this.ev = new Eval();
        // //this.candidat = new Candidat();
        // this.move = new Candidat.coord(-1, -1);
        // //MinMax.simu.add(new Candidat.coord(-1, -1));
        // scsimul = new Miniscore();
        // candidat = new Candidat();
        // pos_counter = 0;
        // nbmove = 0;

        // for (int i = 0 ; i < 19 ; i++)
        // {
        //     for (int j = 0 ; j < 19 ; j++)
        //     {
        //         map[i][j] = 0;
        //         mapc[i][j] = 0;
        //     }
        // }
    }

    public MinMax(int [][]inimap, int len)
    {
        map = new int[19][19];
        mapc = new int[19][19];
        this.ev = new Eval();
        //this.candidat = new Candidat();
        this.move = new Candidat.coord(-1, -1);
        this.len = len;

        for (int i = 0 ; i < 19 ; i++)
        {
            for (int j = 0 ; j < 19 ; j++)
            {
                map[i][j] = inimap[i][j];
                mapc[i][j] = inimap[i][j];
            }
        }
    }

    public MinMax (int len)
    {
        // map = new int[19][19];
        // mapc = new int[19][19];
        //this.ev = new Eval();
        //this.candidat = new Candidat();
        this.len = len;
        this.move = new Candidat.coord(-1, -1);

    }

    public MinMax (MinMax m, int depth)
    {
        //this.imap = new int[19][19];
        // for (int i = 0 ; i < 19 ; i++)
        // {
        //     for (int j = 0 ; j < 19 ; j++)
        //         this.imap[i][j] = m.imap[i][j];
        // }
        // mapc = new int[19][19];
        this.ev = new Eval();
        //this.candidat = new Candidat();
        this.len = m.len + 1;
        this.move = new Candidat.coord(-1, -1);
        this.candidat = new Candidat(false);
        //this.simu = new ArrayList<Candidat.coord>(m.simu);
        // if (depth != 0)
        //     this.candidat = new Candidat(m.candidat);

    }



    public void display_map()
    {
        for (int i = 0 ; i < 19 ; i ++)
        {
            for (int j = 0 ; j < 19 ; j++)
            {
                System.out.printf("%2d", MinMax.map[i][j]);
            }
            System.out.println("");
        }
        System.out.println("");
    }

    static public void display_Map()
    {
        for (int i = 0 ; i < 19 ; i ++)
        {
            for (int j = 0 ; j < 19 ; j++)
            {
                System.out.printf("%2d", MinMax.map[i][j]);
            }
            System.out.println("");
        }
        System.out.println("");
    }

    private void savemapc()
    {
        for (int i = 0 ; i < 19 ; i++)
        {
            for (int j = 0 ; j < 19 ; j++)
            {
                mapc[i][j] = map[i][j];
            }
        }
    }

    private boolean isplay(int c)
    {
        if (c == 1 || c == 2)
            return true;
        return false;
    }

    private boolean near(int i, int j)
    {
        if (j+1 != 19 && isplay(map[i][j + 1]))
            return true;
        if (j-1 != -1 && isplay(map[i][j - 1]))
            return true;
        if (i+1 != 19 && isplay(map[i + 1][j]))
            return true;
        if (i-1 != -1 && isplay(map[i - 1][j]))
            return true;
        if (i+1 != 19 && j-1 != -1 && isplay(map[i+1][j-1]))
            return true;
        if (i-1 != -1 && j-1 != -1 && isplay(map[i-1][j-1]))
            return true;
        if (i+1 != 19 && j+1 != 19 && isplay(map[i+1][j+1]))
            return true;
        if (i-1 != 19 && j+1 != -1 && isplay(map[i-1][j+1]))
            return true;
        return false;
    }


    public void displayco()
    {
        System.out.printf("co   : %d %d %d %d %d \n", ev.co[0], ev.co[1], ev.co[2], ev.co[3], ev.co[4]);
        System.out.printf("unco : %d %d %d %d %d \n", ev.unco[0], ev.unco[1], ev.unco[2], ev.unco[3], ev.unco[4]);

    }

    private void inc_count(int count, int symb)
    {
        if (symb == 1)
        {
            if (count == 10)
                ev.free3 = 1;
            else
                ev.co[(Math.min(count, 5)) - 1] += 1;
        }
        else
        {
            if (count == 10)
                ev.unfree3 = 1;
            else
                ev.unco[Math.min(count, 5) - 1] += 1;
        }
    }

    public void hort(int player)
    {
        int count = 0;
        int symb;
        savemapc();
        int pow;

        final String dir = "hor";
    
        for (int i = 0 ; i < 19 ; i++)
        {
            for (int j = 0 ; j < 19 ; j++)
            {
                symb = mapc[i][j];
                if (!isplay(symb))
                    continue;
                pow=0;
                for (int k = j ; k != 19 && mapc[i][k] == symb ; k++)
                {
                    count +=1;
                    mapc[i][k] = -1;
                }
                if (count == 5)
                    ev.add_stones(5, 2, player==symb, dir);
                else if (count == 4)
                {
                    if (j - 1 != -1 && mapc[i][j -1] == 0)
                        pow+=1;
                    if (j + 4 != 19 && mapc[i][j+4] == 0)
                        pow+=1;
                    ev.add_stones(4, pow, player==symb, dir);
                }
                else if (count == 3)
                {
                    if (j - 1 != -1 && mapc[i][j -1] == 0)
                        pow+=1;
                    if (j + 3 != 19 && mapc[i][j+3] == 0)
                        pow+=1;
                    ev.add_stones(3, pow, player==symb, dir);
                }
                else if (count == 2)
                {
                    if (j - 1 != -1 && mapc[i][j -1] == 0)
                        pow+=1;
                    if (j + 2 != 19 && mapc[i][j+2] == 0)
                        pow+=1;
                    ev.add_stones(2, pow, player==symb, dir);
                }
                inc_count(count, symb);
                count = 0;
            }
        }
    }

    public void vert(int player)
    {
        int count = 0;
        int symb;
        int pow;
        savemapc();
        final String dir = "ver";
        for (int i = 0 ; i < 19 ; i++)
        {
            for (int j = 0 ; j < 19 ; j++)
            {
                symb = mapc[i][j];
                if (!isplay(symb))
                    continue;
                pow = 0;

                for (int k = i ; k != 19 && mapc[k][j] == symb ; k++)
                {
                    count +=1;
                    mapc[k][j] = -1;
                }
                if (count == 5)
                    ev.add_stones(5, 2, symb == player, dir);
                else if (count == 4)
                {
                    if (i - 1 != -1 && mapc[i - 1][j] == 0)
                        pow+=1;
                    if ( i + 4 != 19 && mapc[i + 4][j] == 0)
                        pow +=1;
                    ev.add_stones(4, pow, symb==player, dir);
                }
                else if (count == 3)
                {
                    if (i - 1 != -1 && mapc[i - 1][j] == 0)
                        pow+=1;
                    if (i + 3 != 19 && mapc[i + 3][j] == 0)
                        pow +=1;
                    ev.add_stones(3, pow, symb==player, dir);
                }
                else if (count == 2)
                {
                    if (i - 1 != -1  && mapc[i-1][j] == 0)
                        pow+=1;
                    if (i + 2 != 19 && mapc[i+2][j] == 0)
                        pow+=1;
                    ev.add_stones(2, pow, player==symb, dir);
                }
                inc_count(count, symb);
                count = 0;
            }
        }
    }

    public void diag1(int player)
    {
        int count = 0;
        int symb;
        savemapc();
        int pow;

        final String dir = "diagp";

        for (int i = 0 ; i < 19 ; i++)
        {
            for (int j = 0 ; j < 19 ; j++)
            {
                symb = mapc[i][j];
                if (!isplay(symb))
                    continue;
                pow = 0;
                for (int k = 0 ; i + k != 19 && j + k != 19 && mapc[i + k][j + k] == symb ; k++)
                {
                    count += 1;
                    mapc[i + k][j + k] = -1;
                }
                if (count == 5)
                    ev.add_stones(5, 2, player==symb, dir);
                else if (count == 4)
                {
                    if (i-1 != -1 && j-1 != -1 && mapc[i-1][j-1] == 0)
                        pow +=1; 
                    if (i + 4 != 19 && j + 4 != 19 && mapc[i+4][j+4] == 0)
                        pow+=1;
                    ev.add_stones(4, pow, player==symb, dir);
                }
                else if (count == 3)
                {
                    if (i-1 != -1 && j-1 != -1 && mapc[i-1][j-1] == 0)
                        pow+=1;
                    if(i + 3 != 19 && j + 3 != 19 && mapc[i+3][j+3] == 0)
                        pow+=1;
                    ev.add_stones(3, pow, player==symb, dir);
                }
                else if (count == 2)
                {
                    if (i-1 != -1 && j-1 != -1 && mapc[i-1][j-1] == 0)
                        pow+=1;
                    if(i + 2 != 19 && j + 2 != 19 && mapc[i+2][j+2] == 0)
                        pow+=1;
                    ev.add_stones(2, pow, player==symb, dir);
                }
                
                inc_count(count, symb);
                count = 0;
            }
        }
    }

    public void diag2(int player)
    {
        int count = 0;
        int symb;
        int pow;
        savemapc();
        final String dir = "diagn";
        for (int i = 0 ; i < 19 ; i++)
        {
            for (int j = 0 ; j < 19 ; j++)
            {
                symb = mapc[i][j];
                if (!isplay(symb))
                    continue;
                pow = 0; 
                for (int k = 0 ; i + k != 19 && j - k != -1 && mapc[i + k][j - k] == symb ; k++)
                {
                    count += 1;
                    mapc[i + k][j - k] = -1;
                }
                if (count == 5)
                    ev.add_stones(5, 2, player==symb, dir);

                if (count == 4)
                {
                    if (i-1 != -1 && j+1 != 19 && mapc[i-1][j+1] == 0)
                        pow+=1; 
                    if (i + 4 != 19 && j - 4 != -1 && mapc[i+4][j-4] == 0)
                        pow+=1;
                    ev.add_stones(4, pow, player==symb, dir);     
                }
                else if (count == 3)
                {
                    if (i-1 != -1 && j+1 != 19 && mapc[i-1][j+1] == 0)
                        pow+=1;
                    if(i + 3 != 19 && j - 3 != -1 && mapc[i+3][j-3] == 0)
                        pow+=1;
                    ev.add_stones(3, pow, player==symb, dir);
                }
                else if (count == 2)
                {
                    if (i-1 != -1 && j+1 != 19 && mapc[i-1][j+1] == 0)
                        pow+=1;
                    if(i + 2 != 19 && j - 2 != -1 && mapc[i+2][j-2] == 0)
                        pow+=1;
                    ev.add_stones(2, pow, player==symb, dir);
                }
                inc_count(count, symb);
                count = 0;
            }
        }
    }

    public float eval(int player, int len, int turn)
    {
        //float ret;
        // hort(player);
        // vert(player);
        // diag1(player);
        // diag2(player);


        //ret= ev.evaluate(len);
        //ev.clear_stones();
       // return ret;
       return scsimul.sc.evaluate(player);

       //return scsimul.sc.evaluate(player) + scsimul.free_score(player -1, turn);
    }

    public void candidat()
    {
        for (int i = 0 ; i < 19 ; i++)
        {
            for (int j = 0 ; j < 19 ; j++)
            {
                if (map[i][j] == 0 && near(i, j))
                    map[i][j] = 3;
            }
        }
    }

    public int nb_forced_capture()
    {
        return forced_capture.size();
    }


    protected boolean in_goban(int x, int y)
    {
        if (x >=0 && x < 19 && y >=0 && y < 19)
            return true;
        return false;
    }

    static public boolean IN_goban(int x, int y)
    {
        if (x >=0 && x < 19 && y >=0 && y < 19)
            return true;
        return false;
    }

    // private boolean check_dir(int x, int y, int dx, int dy)
    // {
    //     int player;
    //     int count;

    //     player = map[x][y];
    //     count = 0;

    //     for (int i=0 , j = 0; in_goban(x+i, y+j) && map[x + i][y + j] == player ; i+=dx, j+=dy)
    //         count +=1;

    //     for (int i = -dx, j = -dy ; in_goban(x + i, y + j) && map[x + i][y + j] == player ; i-=dx, j-=dy)
    //         count+=1;

    //     if (count == 5)
    //         return true;
    //     return false;
    // }

    protected boolean check_dir(int x, int y, int dx, int dy, int player)
    {
        int count;

        count = 0;

        for (int i=dx , j = dy; in_goban(x+i, y+j) && map[x + i][y + j] == player ; i+=dx, j+=dy)
            count +=1;

        for (int i = -dx, j = -dy ; in_goban(x + i, y + j) && map[x + i][y + j] == player ; i-=dx, j-=dy)
            count +=1;

        if (count >= 4)
            return true;
        return false;
    }

    protected boolean capture_add_forced(int x, int y, int dx, int dy, int p, int o)
    {
        // System.out.printf("Checking [%d %d] is %d : %b\n", x-dx, y-dy, o, MinMax.map[x-dx][y-dy] == o);
        // System.out.printf("Checking [%d %d] is %d : %b\n", x+dx, y+dy, p, MinMax.map[x+dx][y+dy] == p);
        // System.out.printf("Checking [%d %d] is %d : %b\n", x+2*dx, y+2*dy, 0, MinMax.map[x+2*dx][y+2*dy]==0);

        // System.out.printf("Conclusion %b\n\n",  MinMax.map[x-dx][y-dy] == o && MinMax.map[x+dx][y+dy] == p &&  MinMax.map[x+2*dx][y+2*dy]==0);
        if (in_goban(x+2*dx, y+2*dy) && in_goban(x-2*dx, y-2*dy))
        {
            if (MinMax.map[x-dx][y-dy] == p)
            {
                if (MinMax.map[x-2*dx][y-2*dy] == o && MinMax.map[x + dx][y + dy] == 0)
                {
                    forced_capture.add(new Candidat.coord(x +dx, y+dy));
                    // if (x == 11 && y == 11)
                    //     System.out.printf("adding focing move : %d %d\n", x+ dx, y+dy); 
                }
                else if (MinMax.map[x-2*dx][y-2*dy] == 0 && MinMax.map[x + dx][y + dy] == o)
                {
                    forced_capture.add(new Candidat.coord(x-2*dx, y-2*dy));
                    // if (x == 11 && y == 11)
                    //     System.out.printf("adding focing move : %d %d\n", x-2*dx, y-2*dy);
                }
            }
            else if (MinMax.map[x+dx][y+dy] == p)
            {
                if (MinMax.map[x+2*dx][y+2*dy] == o && MinMax.map[x - dx][y - dy] == 0)
                {
                    forced_capture.add(new Candidat.coord(x-dx, y-dy));
                    // if (x == 11 && y == 11)
                    //     System.out.printf("adding focing move : %d %d\n", x-dx, y-dy); 
                }
                else if (MinMax.map[x+2*dx][y+2*dy] == 0 && MinMax.map[x - dx][y - dy] == o)
                {
                    forced_capture.add(new Candidat.coord(x+2*dx, y+2*dy));
                    // if (x == 11 && y == 11)
                    //     System.out.printf("adding focing move : %d %d\n", x+2*dx, y+2*dy);
                }

            }
        }

        // if (x == 11 && y == 11)
        //     System.out.printf("Capture add forced on %d %d found %d\n ", x, y, candidat.forced_capture.size());

        return true;

    }

    private boolean complete_check_dir(int x, int y, int dx, int dy, int player)
    {
        int count;
        int opponant = player == 1 ? 2 : 1;

        count = 0;
        if (forced_capture.size() !=0)
            forced_capture.clear();

        for (int i=dx , j = dy; in_goban(x+i, y+j) && map[x + i][y + j] == player ; i+=dx, j+=dy)
            count +=1;

        for (int i = -dx, j = -dy ; in_goban(x + i, y + j) && map[x + i][y + j] == player ; i-=dx, j-=dy)
            count +=1;



        if (count >= 4)
        {
            MinMax.map[x][y] = player;

            for (int i=0 , j = 0; in_goban(x+i, y+j) && map[x + i][y + j] == player ; i+=dx, j+=dy)
            {
                //System.out.printf("Checking if %d %d is capturable(+)\n", x+i, y+j);

                for (int k = 0 ; k < 4 ; k++)
                {
                    capture_add_forced(x + i, y + j, ddir[k][0], ddir[k][1], player, opponant);
                }
            }
            for (int i = -dx, j = -dy ; in_goban(x + i, y + j) && map[x + i][y + j] == player ; i-=dx, j-=dy)
            {
                //System.out.printf("Checking if %d %d is capturable(-)\n", x+i, y+j);
                for (int k = 0 ; k < 4 ; k++)
                {
                    capture_add_forced(x + i, y + j, ddir[k][0], ddir[k][1], player, opponant);
                }
            }
            MinMax.map[x][y] = 0;
            return true;
        }
        return false;
    }

    protected boolean complete_check_win(int x, int y, int player)
    {
        for (int i = 0 ; i < 4 ; i++)
        {
            if (complete_check_dir(x, y, ddir[i][0],ddir[i][1], player))
                return true;
        }
        return false;
    }

    protected boolean check_win_4_dir(int x, int y, int player)
    {
        if (check_dir(x,y, 0, 1, player))
            return true;
        if (check_dir(x, y, 1, 0, player))
            return true;
        if (check_dir(x, y, 1, 1, player))
            return true;
        if (check_dir(x, y, 1, -1, player))
            return true;
    
        return false;
    }


    public boolean play(Candidat.coord c, int player)
    {
        this.move = c;
        map[c.x][c.y] = player;
        //imap[c.x][c.y] = player;
        //candidat.add(map, c);
        //simu.add(c);
        this.candidat.save(c);
        scsimul.analyse_move(c.x, c.y, player);
        //this.scsimul.analyse_move(c.x, player, player);
        //System.out.printf("move %d %d\n", c.x, c.y);
        //return is_victory();
        return check_win_4_dir(c.x, c.y, player);
        
    }

    public void unplay(Candidat.coord c, int depth)
    {
            //MinMax.simu.remove(simu.size() - 1);
        //    candidat.remove(map, c);
        //simu.add(new Candidat.coord(-c.x, -c.y));
        int val = map[c.x][c.y];
        map[c.x][c.y] = 0;
        scsimul.analyse_unmove(c.x, c.y, val);

        //System.out.printf("unmove %d %d\n", c.x, c.y);
    }

    protected int change(int player)
    {
        if (player == 1)
            return 2;
        else
            return 1;
    }

    protected float max(float [] val)
    {
        float res = val[0];
        best = candidat.lst.get(0);

        for (int i = 0 ; i < val.length ; i++)
        {
            if (val[i] > res)
            {
                res = val[i];
                best = candidat.lst.get(i);
            }
        }
        return res;
    }

    protected float min(float [] val)
    {
        float res = val[0];
        for (int i = 0 ; i < val.length ; i++)
        {
            if (val[i] < res)
            {
                res = val[i];
                best = candidat.lst.get(i);
            }
        }
        return res;
    }

    public void display_values(float [] val, ArrayList<Candidat.coord> lst)
    {
        Candidat.coord c;
        for(int i = 0 ; i < val.length ; i++)
        {
            c = lst.get(i);
            System.out.printf("pos %d %d : %f\n", c.y, c.x, val[i]);
        }
    }

    public boolean is_victory()
    {
        return MinMax.scsimul.victory;
    }

    protected float value_victory_intermediate(int player, int turn, int len)
    {
        pos_counter++;
        if (player == turn)
            return 10000 - len * 100;
        else
            return -10000 + len * 100;
    }



    protected float value_victory(int player, int turn, int len)
    {
        pos_counter++;
        if (player == turn)
        {
            if (len == 0)
                return 12000;
            return 10000;
        }
        else
        {
            if (len == 0)
                return -12000;
        }
       return -10000;
    }

    public void info()
    {
        display_map();
        scsimul.display();
        System.out.printf("position %d\n", pos_counter);
    }


    public float minmax(int depth, int turn, int player)
    {   

        int nb_candidates;
        float reteval;

        nb_candidates = candidat.old_load(depth, turn);


        if (depth == 0)
        {

            pos_counter++;
            reteval = eval(player, len, turn);

            return reteval;
        }


        values = new float[nb_candidates];

        for (int i = 0 ; i < nb_candidates ; i++)
        {
            MinMax m = new MinMax(this, depth);
            if (m.play(candidat.lst.get(i), turn))
                values[i] = value_victory(player, turn, len);
            else
                values[i] = m.minmax(depth - 1, change(turn), player);
            
            m.unplay(m.move, depth);
        }

        if (depth == Game.max_depth)//max_dep
        {
            eval(player, 0, 0);
            ev.display();
            ev.clear_stones();

        }
    
        if (turn == player)
            return max(values);
        else
            return min(values);
    }

    public float minmaxab(int depth, int turn, int player, float alpha, float beta)
    {   
        int nb_candidates;
        float cur_alpha;
        float cur_beta;
        float res;

        scsimul.cur_turn = turn;


        nb_candidates = candidat.old_load(depth, turn);


        if (depth == 0)
        {
            pos_counter++;
            res = eval(player, len, turn);
            return res;
        }

        values = new float[nb_candidates];

    
        cur_alpha = Float.NEGATIVE_INFINITY;
        cur_beta = Float.POSITIVE_INFINITY;

        for (int i = 0 ; i < nb_candidates ; i++)
        {
            MinMax m = new MinMax(this.len);

            if (turn == player)
            {
                if (m.play(candidat.lst.get(i), turn))
                {
                    res = value_victory_intermediate(player, turn, len);
                }
                else
                {
                    res = m.minmaxab(depth - 1, change(turn), player, Math.max(alpha, cur_alpha), beta);
                    m.unplay(m.move, depth);
                }

                values[i] = res;
                cur_alpha = Math.max(cur_alpha, res);


                if (cur_alpha >= beta) // beta cut
                {
                    return cur_alpha;
                }

            }
            else
            {
                if (m.play(candidat.lst.get(i),turn))
                {
                    res = value_victory_intermediate(player, turn, len);
                }

                else
                {
                    res = m.minmaxab(depth - 1, change(turn), player, alpha, Math.min(beta, cur_beta));
                    m.unplay(m.move, depth);
                }
                values[i] = res;
                cur_beta = Math.min(cur_beta, res);


                if (alpha >= cur_beta) // alpha cut
                {
                    return cur_beta;
                }
            }
        }

        if (turn == player)
            return max(values);
        else
            return min(values);
    }
}