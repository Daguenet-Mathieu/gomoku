package org.modelai;

import java.util.ArrayList;

public class MinMax
{
    static public int [] [] map;
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
    static public ArrayList<Candidat.coord> capwin =  new ArrayList<Candidat.coord>();
    static public boolean after_capwinsim = true;

    public class coord
    {
        int x;
        int y;
    }

    public void load_cur_score(Miniscore score, int turn)
    {
        scsimul.cur_turn = turn;
        for (int d = 0 ; d < 4 ; d++)
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

        pos_counter=0;
        nbmove += 1;
    }

    public MinMax(){}

    public MinMax(int [][]inimap, int len)
    {
        map = new int[19][19];
        this.move = new Candidat.coord(-1, -1);
        this.len = len;

        for (int i = 0 ; i < 19 ; i++)
        {
            for (int j = 0 ; j < 19 ; j++)
                map[i][j] = inimap[i][j];
        }
    }

    public MinMax (int len)
    {
        this.len = len;
        this.move = new Candidat.coord(-1, -1);
    }

    public MinMax (MinMax m, int depth)
    {
        this.len = m.len + 1;
        this.move = new Candidat.coord(-1, -1);
        this.candidat = new Candidat(false);
        candidat.reload_lim();
    }

    public float eval(int player, int len, int turn)
    {
       return scsimul.sc.evaluate(player);
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

    private void adding_capwinsim(int x, int y, int val)
    {
        Candidat.coord c;

        for (int i = 0 ; i < capwin.size() ; i++)
        {
            c = capwin.get(i);

            if (c.x == x && c.y == y)
            {
                MinMax.map[x][y] = val;
                return ;
            } 
        }
        capwin.add(new Candidat.coord(x, y, MinMax.map[x][y]));
        MinMax.map[x][y] = val;
    }

    protected boolean capture_add_forced(int x, int y, int dx, int dy, int p, int o)
    {
        if (in_goban(x+2*dx, y+2*dy) && in_goban(x-2*dx, y-2*dy))
        {
            if (MinMax.map[x-dx][y-dy] == p)
            {
                if (MinMax.map[x-2*dx][y-2*dy] == o && MinMax.map[x + dx][y + dy] == 0)
                {
                    forced_capture.add(new Candidat.coord(x +dx, y+dy));
                    if (after_capwinsim)
                    {
                        adding_capwinsim(x-dx, y-dy, 0);
                        adding_capwinsim(x+dx, y+dy, o);
                        return true;
                    }
                }
                else if (MinMax.map[x-2*dx][y-2*dy] == 0 && MinMax.map[x + dx][y + dy] == o)
                {
                    forced_capture.add(new Candidat.coord(x-2*dx, y-2*dy));
                    if (after_capwinsim)
                    {
                        adding_capwinsim(x-dx, y-dy, 0);
                        adding_capwinsim(x-2*dx, y-2*dy, o);
                        return true;
                    }
                }
            }
            else if (MinMax.map[x+dx][y+dy] == p)
            {
                if (MinMax.map[x+2*dx][y+2*dy] == o && MinMax.map[x - dx][y - dy] == 0)
                {
                    forced_capture.add(new Candidat.coord(x-dx, y-dy));
                    if (after_capwinsim)
                    {
                        adding_capwinsim(x+dx, y+dy, 0);
                        adding_capwinsim(x-dx, y-dy, o);
                        return true;
                    }
                }
                else if (MinMax.map[x+2*dx][y+2*dy] == 0 && MinMax.map[x - dx][y - dy] == o)
                {
                    forced_capture.add(new Candidat.coord(x+2*dx, y+2*dy));
                    if (after_capwinsim)
                    {
                        adding_capwinsim(x+dx, y+dy, 0);
                        adding_capwinsim(x+2*dx, y+2*dy, o);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean complete_check_dir(int x, int y, int dx, int dy, int player)
    {
        int countmax = 0;
        int countmin = 0;
        int opponant = player == 1 ? 2 : 1;

        if (forced_capture.size() !=0)
            forced_capture.clear();

        for (int i=dx , j = dy; in_goban(x+i, y+j) && map[x + i][y + j] == player ; i+=dx, j+=dy)
            countmax +=1;

        for (int i = -dx, j = -dy ; in_goban(x + i, y + j) && map[x + i][y + j] == player ; i-=dx, j-=dy)
            countmin +=1;

        if (countmax + countmin >= 4)
        {
            MinMax.map[x][y] = player;

            for (int i = -countmin ; i <= countmax ; i++)
            {
                for (int k = 0 ; k < 4 ; k++)
                {
                    if (capture_add_forced(x + i*dx, y + i*dy, ddir[k][0], ddir[k][1], player, opponant))
                    {
                        k = 0; i = -countmin;
                    }
                }
            }
            MinMax.map[x][y] = 0;
            restore_capwinsim();
            return true;
        }
        return false;
    }

    private void restore_capwinsim()
    {
        Candidat.coord c;
        for (int i = 0 ; i < capwin.size() ; i++)
        {
            c = capwin.get(i);
            MinMax.map[c.x][c.y] = (int)c.st;
        }
        capwin.clear();
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
        this.candidat.save(c);
        scsimul.analyse_move(c.x, c.y, player);
        return check_win_4_dir(c.x, c.y, player);
    }

    public void unplay(Candidat.coord c, int depth)
    {
        int val = map[c.x][c.y];
        map[c.x][c.y] = 0;
        scsimul.analyse_unmove(c.x, c.y, val);
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


                if (cur_alpha > beta) // beta cut
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


                if (alpha > cur_beta) // alpha cut
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

    //display function
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

    //display function
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

    //display function
    static public void display_Map(int [][] arg_map)
    {
        for (int i = 0 ; i < 19 ; i ++)
        {
            for (int j = 0 ; j < 19 ; j++)
            {
                System.out.printf("%2d", arg_map[i][j]);
            }
            System.out.println("");
        }
        System.out.println("");
    }

    //display function
    public void display_values(float [] val, ArrayList<Candidat.coord> lst)
    {
        Candidat.coord c;
        for(int i = 0 ; i < val.length ; i++)
        {
            c = lst.get(i);
            System.out.printf("pos %d %d : %f\n", c.y, c.x, val[i]);
        }
    }
}