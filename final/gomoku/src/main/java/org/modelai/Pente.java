package org.modelai;
import java.util.ArrayList;

public class Pente extends MinMax {

    static public int [] prisoners = new int[2];
    static ArrayList <Pente.Prison> prisonlst = new ArrayList<Pente.Prison>();
    public static boolean cut = true;
    public static int [] prisonersfactor = {0, 2, 2, 4, 4, 8, 8, 16, 16, 32, 32};
    public static boolean victory_capture = false;

    public static class Prison
    {
        public Candidat.coord pos;
        public Candidat.coord warder;
        public int col;

        public Prison(int x, int y, int warx, int wary, int color)
        {
            this.pos = new Candidat.coord(x, y);
            this.warder = new Candidat.coord(warx, wary);
            this.col = color;
        }
    }

    public Pente()
    {
        map = new int[19][19];
        this.move = new Candidat.coord(-1, -1);
        prisoners = new int[2];
        scsimul = new Miniscore();
        candidat = new Candidat(true);
        prisonlst = new ArrayList<Pente.Prison>();
        pos_counter = 0;
        nbmove = 0;
        candidat.reload_lim();
    }

    public Pente (int len)
    {
        this.len = len + 1;
        this.move = new Candidat.coord(-1, -1);
        this.candidat = new Candidat(true);
    }

    //display function
    private void print_values(float [] values)
    {
        Candidat.coord c;
        for (int i = 0 ; i < values.length ; i++)
        {
            c = candidat.lst.get(i);
            System.out.printf("%f, %d %d\n", values[i], c.y, c.x);
        }
    }

    static private void remove(int x, int y, int warx, int wary)
    {
        int val = map[x][y];
        int tmp = map[warx][wary];
        map[warx][wary]=0;
        prisoners[val - 1]++;
        map[x][y] = 0;
        scsimul.analyse_unmove(x, y, val);
        map[warx][wary]=tmp;
        prisonlst.add(new Pente.Prison(x,y, warx, wary, tmp));
    }

    static private int is_capture(int x, int y, int dx, int dy, int p, int o, boolean pot)
    {
        int count = 0;
        final int car = pot ? 0 : p;

        if (MinMax.IN_goban(x+3*dx, y+3*dy) && map[x + dx][y + dy] == o && map[x + 2 * dx][y + 2 * dy] == o && map[x + 3 * dx][y + 3 * dy] == car)
        {
            count+=2;
        }

        if (MinMax.IN_goban(x-3*dx, y - 3*dy) && map[x - dx][y - dy] == o && map[x - 2 * dx][y - 2 * dy] == o && map[x - 3 * dx][y - 3 * dy] == car)
        {
            count+=2;
        }

        return count;

    }

    static private int is_double(int x, int y, int dx, int dy, int p, int o)
    {
        int count = 0;

        if (MinMax.IN_goban(x+dx, y+dy) && map[x + dx][y + dy] == p)
        {
            if (!MinMax.IN_goban(x-dx, y-dy) || !MinMax.IN_goban(x+2*dx, y+2*dy))
                count+=1; 
            else if (MinMax.IN_goban(x-dx, y-dy) && map[x - dx][y - dy] != p && MinMax.IN_goban(x+2*dx, y+2*dy) && map[x + 2*dx][y + 2*dy] != p)
                {
                   if ((map[x - dx][y - dy] == 0 && map[x+ 2*dx][y+2*dy] == o ) || (map[x - dx][y - dy] == o && map[x+ 2*dx][y+2*dy] == 0))
                        count +=3;
                    else if (map[x - dx][y - dy] == 0 || map[x+ 2*dx][y+2*dy] == 0)
                        count +=1;
                }
        }
        if (MinMax.IN_goban(x-dx, y-dy) && map[x - dx][y - dy] == p)
        {
            if (!MinMax.IN_goban(x+dx, y+dy) || !MinMax.IN_goban(x-2*dx, y-2*dy))
                count+=1; 
            else if (MinMax.IN_goban(x+dx, y+dy) && map[x + dx][y + dy] != p && MinMax.IN_goban(x-2*dx, y-2*dy) && map[x - 2*dx][y - 2*dy] != p)
                {
                    if ((map[x + dx][y + dy] == 0 && map[x- 2*dx][y-2*dy] == o ) || (map[x + dx][y + dy] == o && map[x- 2*dx][y-2*dy] == 0))
                        count +=3;
                    else if (map[x + dx][y + dy] == 0 || map[x- 2*dx][y-2*dy] == 0 )
                        count +=1;
                }
        }
        return count;
    }

    static private boolean remove_capture(int x, int y, int dx, int dy, int p, int o)
    {
        if (IN_goban(x+3*dx, y+3*dy) && map[x + dx][y + dy] == o && map[x + 2 * dx][y + 2 * dy] == o && map[x + 3 * dx][y + 3 * dy] == p)
        {
            remove(x+dx, y+dy, x, y);
            remove(x+2*dx, y+2*dy, x, y);
            return true;
        }

        if (IN_goban(x-3*dx, y - 3*dy) && map[x - dx][y - dy] == o && map[x - 2 * dx][y - 2 * dy] == o && map[x - 3 * dx][y - 3 * dy] == p)
        {
            remove(x-dx, y-dy, x, y);
            remove(x-2*dx, y-2* dy, x, y);
            return true;
        }
        return false;
    }

    static public int count_capture(int x, int y, int turn, boolean pot)
    {
        final int op = turn == 1 ? 2 : 1;
        int count = 0;

        for (int i = 0 ; i < 4 ; i++)
        {
            count += is_capture(x, y, ddir[i][0], ddir[i][1], turn, op, pot);
        }
        return count;
    }

    static public int count_double(int x, int y, int turn)
    {
        final int op = turn == 1 ? 2 : 1;
        int count = 0;

        for (int i = 0 ; i < 4 ; i++)
        {
            count += is_double(x, y, ddir[i][0], ddir[i][1], turn, op);
        }
        return count;
    }

    private boolean is_captured(int x, int y, int turn)
    {  
        final int op = turn == 1 ? 2 : 1;
        int count = 0;

        for (int i = 0 ; i < 4 ; i++)
        {
            count += is_capture(x, y, ddir[i][0], ddir[i][1], turn, op, false);
        }
        if (prisoners[(turn + 2) %2] + count >= 10)
        {
            victory_capture = true;
            return true;
        }
        return false;
    }


    static public void remove_captured(int x, int y, int turn)
    {  
        final int op = turn == 1 ? 2 : 1;

        for (int i = 0 ; i < 4 ; i++)
            remove_capture(x, y, ddir[i][0], ddir[i][1], turn, op);
    }

    public float value_victory_smarter(int player, int turn, int len, int nb, boolean print) //not so smart
    {
        pos_counter++;
        float res = 0;
        float win_cap = 0;

        if (player == turn)
        {
            if (victory_capture)
            {
                victory_capture = false;
                win_cap = 10000 - len * 100;
            }
            else
            {
                if (prisoners[(turn + 1) %2] + (nb * 2) >= 10)
                    res =  -10000 + ((len + nb) * 100);
                else
                    res = 10000 - ((len + nb) * 100);
            }
        }
        else
        {
            if (victory_capture)
            {
                victory_capture = false;
                win_cap = -10000 + len * 100;
            }
            else
            {
                if (prisoners[(turn + 1) %2] + (nb * 2) >= 10)
                    res = 10000 - ((len + nb) * 100);
                else
                    res = -10000 + ((len + nb) * 100);
            }
        }

        if (print)
        {
            System.out.printf("Victory ! len %d, nb forced capture %d, vicotry capture %b, res %f, win_cap %f\n", len,  nb, victory_capture, res, win_cap);
            System.out.printf("prisoners %d %d\n", prisoners[0], prisoners[1]);
        }

        if (Math.abs(win_cap) > Math.abs(res))
            return win_cap;
        return res;
    }

    private boolean vicotry_detected(int x, int y, int player)
    {
        boolean res1;
        boolean res2;

        res1 = complete_check_win(x, y, player);
        res2 = is_captured(x, y, player);
        return (res1 || res2);
    }

    public boolean play(Candidat.coord c, int player)
    {
        if (vicotry_detected(c.x, c.y, player))
            return true;
        
        map[c.x][c.y] = player;
        this.move = c;
        remove_captured(c.x, c.y, player);
        candidat.save(c);
        scsimul.analyse_move(c.x, c.y, player);
        return false;
    }

    static public void play_prisoners(int val, int warx, int wary)
    {
        int o = val == 1 ? 2 : 1;
        Pente.Prison p;

        if (prisonlst.size() >= 2)
        {
            p = prisonlst.get(prisonlst.size()-1);
            while (p.warder.x == warx && p.warder.y == wary && p.col == val)
            {
                prisoners[o - 1]--;
                map[p.pos.x][p.pos.y] = o;
                scsimul.analyse_move(p.pos.x, p.pos.y, o);

                prisonlst.remove(prisonlst.size()-1);
                if (prisonlst.size() == 0)
                    return ;
                p = prisonlst.get(prisonlst.size() - 1);
            }
        }
    }

    public void unplay(Candidat.coord c, int depth)
    {
        int val = map[c.x][c.y];
        map[c.x][c.y] = 0;
        scsimul.analyse_unmove(c.x, c.y, val);
        play_prisoners(val, c.x, c.y);
    }

    private int decdepth(int dep, float alpha, float beta)
    {
        float lim;
        float l1;
        float l2;

        if (alpha == Float.NEGATIVE_INFINITY)
            l1 = 0;
        else
            l1 = Math.abs(alpha);

        if (beta == Float.POSITIVE_INFINITY)
            l2 = 0;
        else
            l2 = Math.abs(beta);

        lim = Math.max(l1, l2);
        
        if (lim > 9000)
            return Math.min(dep - 1, (int)((Math.abs(lim - 10000)) / 100) + 1);
        return dep - 1;
    }

    public float minmax(int depth, int turn, int player)
    {   
        int nb_candidates;

        nb_candidates = candidat.old_load(depth, turn);

        if (depth == 0)
        {
            pos_counter++;

            return eval(player, len, turn);
        }

        values = new float[nb_candidates];

        for (int i = 0 ; i < nb_candidates ; i++)
        {
            Pente m = new Pente(this.len);
            if (m.play(candidat.lst.get(i), turn))
                values[i] = value_victory(player, turn, len);
            else
                values[i] = m.minmax(depth - 1, change(turn), player);
            

            m.unplay(m.move, depth);
        }

        if (depth == Game.max_depth)
        {
            print_values(values);
            System.out.printf("prisoners[0] : %d, prisoners[1] : %d\n", prisoners[0], prisoners[1]);
        }

        if (turn == player)
            return max(values);
        else
            return min(values);
    }


    //debug function
    public void showdebug()
    {
        display_map();
        scsimul.display();
    }

    //debug function
    public void debugstr() throws ArithmeticException
    {
        if (scsimul.check_str() == false)
        {
            System.out.printf("Error at %d\n", pos_counter);
            showdebug();
            throw new ArithmeticException();
        }

        if (scsimul.check_capt() == true)
            {
                System.out.printf("Counter %d %d", pos_counter, nbmove);
                System.exit(0);
            }
        return;
    }

    public int prisonpnt(int player)
    {
        if (player == 1)
            return (prisoners[1] - prisoners[0]) * 8;
        else
            return (prisoners[0] - prisoners[1]) * 8;
    }

    public int potentialpnt(int player)
    {
       int sup = 0;
        
        if (player == 1)
            return (MinMax.scsimul.capt[0]*8 - MinMax.scsimul.capt[1]*5 ) + sup;
        else
            return (MinMax.scsimul.capt[1]*8 - MinMax.scsimul.capt[0]*5 ) + sup;
    }

    public int blockedpnt(int player)
    {
        if (player == 1)
            return - scsimul.bpoint[0] + scsimul.bpoint[1];
        else 
            return scsimul.bpoint[0] - scsimul.bpoint[1];
    }

    private float supeval(int player, int len, int turn)
    {
        return eval(player, len, turn) + prisonpnt(player) + potentialpnt(player) + blockedpnt(player);
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
            return supeval(player, len, turn);
        }

        if (nb_candidates == 0)
            return 0;

        values = new float[nb_candidates];

        cur_alpha = Float.NEGATIVE_INFINITY;
        cur_beta = Float.POSITIVE_INFINITY;

        for (int i = 0 ; i < nb_candidates ; i++)
        {
            Pente m = new Pente(this.len);
            if (turn == player)
            {
                //System.out.printf(" p %d %d ", candidat.lst.get(i).x, candidat.lst.get(i).y);
                if (m.play(candidat.lst.get(i), turn))
                {
                    res = value_victory_smarter(player, turn, len, m.nb_forced_capture(), false) + supeval(player, len, turn);
                    pos_counter++;
                }
                else
                {

                    res = m.minmaxab(decdepth(depth, cur_alpha, cur_beta), change(turn), player, Math.max(alpha, cur_alpha), beta);
                    m.unplay(m.move, depth);
                    //System.out.printf(" u %d %d ", candidat.lst.get(i).x, candidat.lst.get(i).y);
                }

                values[i] = res;
                cur_alpha = Math.max(cur_alpha, res);

                if (cut && cur_alpha > beta) // beta cut
                {
                    pos_counter++;
                    return cur_alpha;
                }
            }
            else
            {
                //System.out.printf(" p %d %d ", candidat.lst.get(i).x, candidat.lst.get(i).y);
                if (m.play(candidat.lst.get(i),turn))
                {
                    res = value_victory_smarter(player, turn, len, forced_capture.size(), false) + supeval(player, len, turn);
                    pos_counter++;
                }
                else
                {
                    res = m.minmaxab(decdepth(depth, cur_alpha, cur_beta), change(turn), player, alpha, Math.min(beta, cur_beta));
                    m.unplay(m.move, depth);
                    //System.out.printf(" p %d %d ", candidat.lst.get(i).x, candidat.lst.get(i).y);
                }
                
                values[i] = res;
                cur_beta = Math.min(cur_beta, res);

                if (cut && alpha > cur_beta) // alpha cut
                {
                    pos_counter++;
                    return cur_beta;
                }
            }
        }

        if (depth == Game.max_depth)
            return bonus_point(turn, player, values);

        if (turn == player)
            return max(values);
        else
            return min(values);
    }

    private float bonus_point(int turn, int player, float values[])
    {
        Candidat.coord c = candidat.lst.get(0);
        float max = values[0];
        int idx = 0;
        float max2;
        float bonus;

        best = candidat.lst.get(0);
        for (int i = 0 ; i < values.length ; i++)
            {
                if (turn == player)
                {
                    if (max < values[i])
                    {
                        best = candidat.lst.get(i);
                        max = values[i];
                        idx = i;
                    }
                }
                else
                {
                    if (max > values[i])
                    {
                        best = candidat.lst.get(i);
                        max = values[i];
                        idx = i;
                    }
                }
            }

        max2 = values[idx] + adding_bonus_point(candidat.lst.get(idx).x, candidat.lst.get(idx).y, turn, player);
        for (int i = 0 ; i < values.length ; i++)
        {
            if (values[i] == max)
            {
                c = candidat.lst.get(i);
                bonus = adding_bonus_point(c.x, c.y, turn, player);
                if (player == turn)
                {
                    if (max2 < max + bonus)
                    {
                        max2 = max + bonus;
                        best = c;
                    }
                }
                else
                {
                    if (max2 > max + bonus)
                    {
                        max2 = max + bonus;
                        best = c;
                    }
                }
            }
        }
        return max2;
    }

    private float adding_bonus_point(int x, int y, int turn, int player)
    {
        int pos = count_capture(x, y, turn, true);
        int dou = count_double(x, y, turn);
        int cap = count_capture(x, y, turn, false) / 2;

        if (player == turn)
            return pos - dou + cap * 6;
        else
            return dou - pos - cap * 6; 
    }
}

