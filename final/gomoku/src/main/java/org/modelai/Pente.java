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

        public Prison(int x, int y, int warx, int wary)
        {
            pos = new Candidat.coord(x, y);
            warder = new Candidat.coord(warx, wary);
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
    }

    public Pente (int len)
    {
        this.len = len + 1;
        this.move = new Candidat.coord(-1, -1);
        this.candidat = new Candidat(true);
    }

    static public void remove(int x, int y, int warx, int wary)
    {
        int val = map[x][y];
        int tmp = map[warx][wary];
        map[warx][wary]=0;
        prisoners[val - 1]++;
        map[x][y] = 0;
        scsimul.analyse_unmove(x, y, val);
        map[warx][wary]=tmp;
        prisonlst.add(new Pente.Prison(x,y, warx, wary));
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

        if (MinMax.IN_goban(x+dx, y+dy) && map[x + dx][y + dy] == p && MinMax.IN_goban(x-dx, y-dy) && map[x - dx][y - dy] != p)
            if (MinMax.IN_goban(x+2*dx, y+2*dy) && map[x + 2*dx][y + 2*dy] != p)
                {
                   if (map[x - dx][y - dy] == 0 && map[x+ 2*dx][y+2*dy] == o || map[x - dx][y - dy] == o && map[x+ 2*dx][y+2*dy] == 0)
                        count +=2;
                    else if ( map[x - dx][y - dy] == 0 || map[x+ 2*dx][y+2*dy] == 0 )
                        count +=1;
                }
        if (MinMax.IN_goban(x+dx, y+dy) && map[x + dx][y + dy] != p && MinMax.IN_goban(x-dx, y-dy) && map[x - dx][y - dy] == p)
            if (MinMax.IN_goban(x-2*dx, y-2*dy) && map[x - 2*dx][y - 2*dy] != p)
                {
                    if (map[x + dx][y + dy] == 0 && map[x- 2*dx][y-2*dy] == o || map[x + dx][y + dy] == o && map[x- 2*dx][y-2*dy] == 0)
                        count +=2;
                    else if ( map[x + dx][y + dy] == 0 || map[x- 2*dx][y-2*dy] == 0 )
                        count +=1;
                }
        return count;
    }

    static public boolean remove_capture(int x, int y, int dx, int dy, int p, int o)
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
            // if (remove_capture(x, y, ddir[i][0], ddir[i][1], turn, op))
            //     MinMax.scsimul.capt[turn - 1]--;
    }

    //inside nb

    public float value_victory_smarter(int player, int turn, int len, int nb, boolean print) //not so smart
    {
        pos_counter++;
        float res = 0;
        float win_cap = 0;

        // if (pos_counter % 1000 == 7)
        // {        
        //     System.out.printf("Counter %d %d\n", pos_counter, nbmove);
        //     display_map();
        //     scsimul.display();
        // }
        //int nb = candidat.forced_capture.size();

        if (player == turn)
        {
            if (victory_capture)
            {
                victory_capture = false;
                win_cap = 10000 - len * 100;
                //return win_cap;
                //return win_cap;
                //return 10000 - len * 100;
            }
            else
            {

            if (prisoners[(turn + 1) %2] + (nb * 2) >= 10)
            {
                res =  -10000 + ((len + nb) * 100);

                //return -10000 + ((len + nb) * 100);
            }
            else
                res = 10000 - ((len + nb) * 100);

            //return 10000 - ((len + nb) * 100);
            }

        }
        else
        {
            if (victory_capture)
            {
                victory_capture = false;
                win_cap = -10000 + len * 100;
                //return win_cap;
                //return -10000 + len * 100;
            }
            else
            {
            if (prisoners[(turn + 1) %2] + (nb * 2) >= 10)
            {
                res = 10000 - ((len + nb) * 100);
                //return 10000 - ((len + nb) * 100);
            }
            else
                res = -10000 + ((len + nb) * 100);
            }
            //return -10000 + ((len + nb) * 100);
        }

        if (print)
        {
            System.out.printf("Victory ! len %d, nb forced capture %d, vicotry capture %b, res %f, win_cap %f\n", len,  nb, victory_capture, res, win_cap);
            System.out.printf("prisoners %d %d\n", prisoners[0], prisoners[1]);
        }

        // if (len == 1 && player == 2 && MinMax.forced_capture.size() != 0)
        // {
        //     System.out.printf( ANSI_GREEN + "Victory ! nb capture %d, val %d\n" + ANSI_RESET, nb, res);
        // }

        // if (len == 0)
        // {
        //     System.out.printf("Victory ! nb capture %d, val %d\n", nb, res);
        //     System.out.printf("prisoners %d %d\n", prisoners[0], prisoners[1]);
        // }
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
        //System.out.printf("pente play %d %d\n", c.x, c.y);
        // if (check_win_4_dir(c.x, c.y, player) || is_captured(c.x, c.y, player))
        //     return true;


        // if (complete_check_win(c.x, c.y, player) || is_captured(c.x, c.y, player))
        //     return true;

        if (vicotry_detected(c.x, c.y, player))
            return true;
        
        map[c.x][c.y] = player;

        this.move = c;
        remove_captured(c.x, c.y, player);
        candidat.save(c);
        //scsimul.first_capt(this.len, c.x, c.y); first cap on not
        scsimul.analyse_move(c.x, c.y, player);


        // if (nbmove == 2 && (pos_counter >=918 && pos_counter <= 920))
        // {
        //     System.out.printf("pente play %d %d\n", c.x, c.y);
        //     MinMax.display_Map();
        //     scsimul.display();
        // }

        return false;


    }

    static public void play_prisoners(int val, int warx, int wary)
    {
        int o = val == 1 ? 2 : 1;
        Pente.Prison p;

        if (prisonlst.size() >= 2)
        {
            //System.out.println("\n\n\n\nREPLACE PRISONERS\n\n\n\n");

            
            p = prisonlst.get(prisonlst.size()-1);
            while (p.warder.x == warx && p.warder.y == wary)
            {

                prisoners[o - 1]--;
                //System.out.printf("%d %d replayed\n", p.pos.x, p.pos.y);
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
        //System.out.printf("pente unplay %d %d\n", c.x,c.y);
        int val = map[c.x][c.y];
        map[c.x][c.y] = 0;
        scsimul.analyse_unmove(c.x, c.y, val);
        play_prisoners(val, c.x, c.y);

        // if ( nbmove == 2 && (pos_counter >= 918 && pos_counter <= 920))
        // {
        //     System.out.printf("pente unplay %d %d\n", c.x,c.y);
        //     MinMax.display_Map();
        //     scsimul.display();
        // }
    }

    private void print_values(float [] values)
    {
        Candidat.coord c;
        for (int i = 0 ; i < values.length ; i++)
        {
            c = candidat.lst.get(i);
            System.out.printf("%f, %d %d\n", values[i], c.y, c.x);
        }
    }

    public float minmax(int depth, int turn, int player)
    {   
        int nb_candidates;

        //nb_candidates = candidat.old_load(depth, turn);
        nb_candidates = candidat.old_load(depth, turn);

        if (depth == 0)
        {
            pos_counter++;

            // if (this.scsimul.blocklist.size() != 0)
            // {

            //     display_map();
            //     scsimul.display();
            // }

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

    public void showdebug()
    {
        display_map();
        scsimul.display();
    }

    public void debugstr() throws ArithmeticException
    {
        // if (scsimul.check_str() == false)
        // {
        //     System.out.printf("Error at %d\n", pos_counter);
        //     showdebug();
        //     throw new ArithmeticException();
        // }
        //System.out.println("checked");

        // System.out.printf("Counter %d %d\n", pos_counter, nbmove);
        // display_map();
        // scsimul.display(2);

        // if (pos_counter >= 25898 && pos_counter <= 28900 && nbmove == 3)
        // {
        //     display_map();
        //     scsimul.display(2);
        // }

        if (scsimul.check_capt() == true)
            {
                System.out.printf("Counter %d %d", pos_counter, nbmove);
                System.exit(0);
            }
        return;
    }

    // public int prisonpnt(int player)
    // {
    //     int res = 0;
    //     if (player == 1)
    //     {
    //         res = prisonersfactor[prisoners[1]] - prisonersfactor[prisoners[0]];
    //     }
    //     else
    //     {
    //         res = prisonersfactor[prisoners[0]] - prisonersfactor[prisoners[1]];
    //     }
    //     return res;
    // }


    // invert value
    public int prisonpnt(int player)
    {
        if (player == 1)
            return (prisoners[1] - prisoners[0]) * 8; //check sens
        else
            return (prisoners[0] - prisoners[1]) * 8;
    }


    public int potentialpnt(int player)
    {
       // System.out.printf("potentiel added %d %d\n", MinMax.scsimul.capt[0], MinMax.scsimul.capt[1]);
       int sup = 0;

        if (scsimul.lastcap)
            sup = 0;
        
        if (player == 1)
            return (MinMax.scsimul.capt[0]*8 - MinMax.scsimul.capt[1]*5 ) + sup;
        else
            return (MinMax.scsimul.capt[1]*8 - MinMax.scsimul.capt[0]*5 ) + sup;
    }

    public int blockedpnt(int player)
    {
        if (player == 1)
        {
            return - scsimul.bpoint[0] + scsimul.bpoint[1];
        }
        else 
        {
            return scsimul.bpoint[0] - scsimul.bpoint[1];
        }
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

        //nb_candidates = candidat.old_load(depth, turn);s
        scsimul.cur_turn = turn;

        //MinMax.display_Map();
        
        // if (depth == Game.max_depth && forced_capture.size() != 0)
        // {
        //     Candidat.coord c;
        //     for (int i = 0 ; i < forced_capture.size() ; i++)
        //     {
        //         c = forced_capture.get(i);
        //         System.out.printf(ANSI_RED + "forcedcoord %d %d\n" + ANSI_RESET, c.x, c.y);
        //     }

        // }

        // if (forced_capture.size() !=0)
        //     forced_capture.clear();

        //if (depth == Game.max_depth && forced_capture.size() !=0)
        nb_candidates = candidat.old_load(depth, turn);

        //display_map();
        // if(pos_counter >= 5820 && pos_counter <= 5824)
        //     showdebug();

        //if (pos_counter%1000 == 0)
        // if (false)
        // {
        //         System.out.printf("Counter %d %d\n", pos_counter, nbmove);
        //         display_map();
        //         scsimul.display(2);
        // }
        // if (scsimul.check_capt())
        //     System.exit(0);
        // if (pos_counter == 28900)
        //     System.exit(0);

        //if (pos_counter >= 10374 && nbmove == 7)
        // if (pos_counter <= 10)
        // {
        //     display_map();
        //     scsimul.display();
        // }


        if (depth == 0)
        {
            pos_counter++;

            //if (pos_counter  % 100 == 0 && (scsimul.capt[0] !=0 || scsimul.capt[1] != 0))
            //if (pos_counter % 100 == 0)
            //if ( nbmove == 2 && (pos_counter >=1921 && pos_counter <= 1922)) //<=12
            // if (nbmove == 2 && ((pos_counter >=918 && pos_counter <= 920)))

            //if (scsimul.blocklist.size() != 0)
            // if (pos_counter % 10 == 0)
            // {
            //     System.out.printf("Counter %d %d\n", pos_counter, nbmove);
            //     display_map();
            //     scsimul.display();
            // }
            //MinMax.display_Map();
            //System.exit(0);
            res = eval(player, len, turn) + prisonpnt(player) + potentialpnt(player) + blockedpnt(player);
            //debugstr();
            // if (res > 1000)
            // {
            //         scsimul.display();
            //         display_map();
            //         stop++;
            // }
            // if (stop == 5)
            //     System.exit(0);
            return res;
        }

        values = new float[nb_candidates];

    
        cur_alpha = Float.NEGATIVE_INFINITY;
        cur_beta = Float.POSITIVE_INFINITY;

        for (int i = 0 ; i < nb_candidates ; i++)
        {
            Pente m = new Pente(this.len);

            if (turn == player)
            {
                if (m.play(candidat.lst.get(i), turn))
                {

                    //System.out.printf("after play %d\n", m.nb_forced_capture());
                    //  if (candidat.lst.get(i).x == 8 && candidat.lst.get(i).y == 14)
                    //     res = value_victory_smarter(player, turn, len, m.nb_forced_capture(), true) + supeval(player, len, turn);
                    // else
                    //System.out.printf("Victory on %d %d\n", candidat.lst.get(i).x, candidat.lst.get(i).y);
                    res = value_victory_smarter(player, turn, len, m.nb_forced_capture(), false) + supeval(player, len, turn);
                    //debugstr();
                    // if (res == 12000 || res == -12000)
                    // {
                    //     best = new Candidat.coord(12, 9);
                    //     System.out.println("BESTIIIIE");
                    //     values[i] = res;
                    //     return res;
                    // }
                }
                else
                {
                    res = m.minmaxab(depth - 1, change(turn), player, Math.max(alpha, cur_alpha), beta);
                    m.unplay(m.move, depth);
                }

                values[i] = res;
                cur_alpha = Math.max(cur_alpha, res);

                //m.unplay(m.move, depth);

                if (cut && cur_alpha >= beta) // beta cut
                {
                    //System.out.println("betacut");
                    //m.unplay(m.move, depth);
                    // m.best = candidat.lst.get(i);
                    // System.out.printf("best %d %d\n", m.best.x, m.best.y);
                    // if (depth == Game.max_depth - 1)
                    // {
                    //     for (int j = 0 ; j < nb_candidates ; j++)
                    //     {
                    //         System.out.printf("\tCandidat %d %d %f\n", candidat.lst.get(j).x, candidat.lst.get(j).y, values[j]);
                    //     }
                    //     System.out.println();
                    // }
                    
                    return cur_alpha;
                }

            }
            else
            {
                if (m.play(candidat.lst.get(i),turn))
                {
                    // if (candidat.lst.get(i).x == 9 && candidat.lst.get(i).y == 6)
                    //     res = value_victory_smarter(player, turn, len, forced_capture.size(), true) + supeval(player, len, turn);
                    // else
                    res = value_victory_smarter(player, turn, len, forced_capture.size(), false) + supeval(player, len, turn);
                    //debugstr();
                    // if (res == 12000 || res == -12000)
                    // {
                    //     best = new Candidat.coord(12, 9);
                    //     System.out.println("BESTIIIIE");
                    //     values[i] = res;
                    //     return res;
                    // }
                }
                else
                {
                    res = m.minmaxab(depth - 1, change(turn), player, alpha, Math.min(beta, cur_beta));
                    m.unplay(m.move, depth);
                }
                values[i] = res;
                cur_beta = Math.min(cur_beta, res);

                //m.unplay(m.move, depth);

                if (cut && alpha >= cur_beta) // alpha cut
                {
                    //System.out.println("alphacut");
                    //m.unplay(m.move, depth);
                    // m.best = candidat.lst.get(i);
                    // System.out.printf("best %d %d", m.best.x, m.best.y);
                    // if (depth == Game.max_depth - 1)
                    // {
                    //     for (int j = 0 ; j < nb_candidates ; j++)
                    //     {
                    //         System.out.printf("\tCandidat %d %d %f\n", candidat.lst.get(j).x, candidat.lst.get(j).y, values[j]);
                    //     }
                    //     System.out.println();
                    // }
                    return cur_beta;
                }
            }
        }

        if (depth == Game.max_depth)
        {
            System.out.println("At the end !!!!!!!!!!!!!!!!!!!!!!!!!");
            //candidat.display_candidat(map);
            System.out.printf("prisoners[0] : %d, prisoners[1] : %d\n", Pente.prisoners[0], Pente.prisoners[1]);
            display_map();
            scsimul.display();

        }

        // if (depth == Game.max_depth)
        // {
        //     for (int i = 0 ; i < nb_candidates ; i++)
        //         {
        //             System.out.printf("Candidat %d %d %f\n", candidat.lst.get(i).x, candidat.lst.get(i).y, values[i]);
        //         }
        //         System.out.println();
        // }

        // if (depth == Game.max_depth - 1)
        // {
        //     for (int i = 0 ; i < nb_candidates ; i++)
        //         {
        //             System.out.printf("\tCandidat %d %d %f\n", candidat.lst.get(i).x, candidat.lst.get(i).y, values[i]);
        //         }
        //         System.out.println();
        // }

        // if (depth == Game.max_depth)
        //     return bonus_point(turn, player, values);

        if (turn == player)
            return max(values);
        else
            return min(values);
    }

    private float bonus_point(int turn, int player, float values[])
    {
        Candidat.coord c;
        float max = values[0];
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
                    }
                }
                else
                {
                    if (max > values[i])
                    {
                        best = candidat.lst.get(i);
                        max = values[i];
                    }
                }
            }

        for (int i = 0 ; i < values.length ; i++)
        {
            if (values[i] == max)
            {
                c = candidat.lst.get(i);
                bonus = adding_bonus_point(c.x, c.y, turn, player);
                values[i] += adding_bonus_point(c.x, c.y, turn, player);
                if (player == turn)
                {
                    if (max < max + bonus)
                    {
                        max = max + bonus;
                        best = c;
                    }
                }
                else
                {
                    if (max > max + bonus)
                    {
                        max = max + bonus;
                        best = c;
                    }
                }
            }
        }
        // for (int i = 0 ; i < values.length ; i++)
        // {
        //     System.out.printf(" %d %d %f", candidat.lst.get(i).x, candidat.lst.get(i).y ,values[i]);
        // }
        // System.out.printf("best %d %d", best.x, best.y);
        // System.exit(0);
        return max;
    }

    private float adding_bonus_point(int x, int y, int turn, int player)
    {
        if (player == turn)
            return count_capture(x, y, turn, true) - count_double(x, y, turn);
        else
            return count_double(x, y, turn) - count_capture(x, y,turn, true); 
    }
}

