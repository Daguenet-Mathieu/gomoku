package org.modelai;
import java.util.ArrayList;

public class Pente extends MinMax {

    // public Candidat.coord [] removed;
    static public int [] prisoners;
    static ArrayList <Pente.Prison> prisonlst;
    public static boolean cut = true;
    public static int stop = 0;

    private class Prison
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
        candidat = new Candidat();
        prisonlst = new ArrayList<Pente.Prison>();
        pos_counter = 0;
        nbmove = 0;
    }

    private void remove(int x, int y, int warx, int wary)
    {
        int val = map[x][y];
        prisoners[val - 1]++;
        //System.out.printf("%d %d removed\n", x, y);
        map[x][y] = 0;
        scsimul.analyse_unmove(x, y, val);
        prisonlst.add(new Pente.Prison(x,y, warx, wary));
    }

    private int is_capture(int x, int y, int dx, int dy, int p, int o)
    {
        int count = 0;

        if (in_goban(x+3*dx, y+3*dy) && map[x + dx][y + dy] == o && map[x + 2 * dx][y + 2 * dy] == o && map[x + 3 * dx][y + 3 * dy] == p)
        {
            count+=2;
        }

        if (in_goban(x-3*dx, y - 3*dy) && map[x - dx][y - dy] == o && map[x - 2 * dx][y - 2 * dy] == o && map[x - 3 * dx][y - 3 * dy] == p)
        {
            count+=2;
        }

        return count;

    }

    private boolean remove_capture(int x, int y, int dx, int dy, int p, int o)
    {
        if (in_goban(x+3*dx, y+3*dy) && map[x + dx][y + dy] == o && map[x + 2 * dx][y + 2 * dy] == o && map[x + 3 * dx][y + 3 * dy] == p)
        {
            remove(x+dx, y+dy, x, y);
            remove(x+2*dx, y+2*dy, x, y);
            return true;
        }

        if (in_goban(x-3*dx, y - 3*dy) && map[x - dx][y - dy] == o && map[x - 2 * dx][y - 2 * dy] == o && map[x - 3 * dx][y - 3 * dy] == p)
        {
            remove(x-dx, y-dy, x, y);
            remove(x-2*dx, y-2* dy, x, y);
            return true;
        }
        return false;
    }

    private boolean is_captured(int x, int y, int turn)
    {  
        final int op = turn == 1 ? 2 : 1;
        int count = 0;

        for (int i = 0 ; i < 4 ; i++)
        {
            count += is_capture(x, y, ddir[i][0], ddir[i][1], turn, op);
        }
        if (prisoners[(turn + 2) %2] + count >= 10)
            return true;
        return false;
    }


    private void remove_captured(int x, int y, int turn)
    {  
        final int op = turn == 1 ? 2 : 1;

        for (int i = 0 ; i < 4 ; i++)
            remove_capture(x, y, ddir[i][0], ddir[i][1], turn, op);
    }


    public boolean play(Candidat.coord c, int player)
    {
        //System.out.printf("pente play %d %d\n", c.x, c.y);


        if (check_win_4_dir(c.x, c.y, player) || is_captured(c.x, c.y, player))
            return true;

        map[c.x][c.y] = player;
        this.move = c;
        remove_captured(c.x, c.y, player);

        this.candidat.save(c);
        scsimul.analyse_move(c.x, c.y, player);

        return false;
    }

    public void play_prisoners(int val, int warx, int wary)
    {
        int o = val == 1 ? 2 : 1;
        Pente.Prison p;

        if (prisonlst.size() >= 2)
        {
            //System.out.println("\n\n\n\nREPLACE PRISONERS\n\n\n\n");

            
            p = prisonlst.get(prisonlst.size()-1);
            while (p.warder.x == warx && p.warder.y == wary)
            {
                map[p.pos.x][p.pos.y] = o;
                prisoners[o - 1]--;
                //System.out.printf("%d %d replayed\n", p.pos.x, p.pos.y);
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
    }

    public Pente (int len)
    {
        this.len = len + 1;
        this.move = new Candidat.coord(-1, -1);
        this.candidat = new Candidat();
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
        nb_candidates = candidat.old_load(depth);

        if (depth == 0)
        {
            pos_counter++;

            //display_map();
            //scsimul.display();

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
        return;
    }

    public int prisonpnt(int player)
    {
        if (player == 1)
        {
            return (prisoners[1] - prisoners[0]) * 2;
        }
        else
        {
            return (prisoners[0] - prisoners[1])  * 2;
        }

    }

    public float minmaxab(int depth, int turn, int player, float alpha, float beta)
    {   
        int nb_candidates;
        float cur_alpha;
        float cur_beta;
        float res;

        //nb_candidates = candidat.old_load(depth, turn);
        nb_candidates = candidat.old_load(depth);
        //display_map();
        // if(pos_counter >= 5820 && pos_counter <= 5824)
        //     showdebug();

        if (depth == 0)
        {
            pos_counter++;
            // if (pos_counter  % 1000 == 7)
            // {
            //     System.out.printf("Counter %d %d\n", pos_counter, nbmove);
            //     display_map();
            //     scsimul.display();
            // }

            // System.out.printf("Counter %d %d\n", pos_counter, nbmove);
            // display_map();
            // scsimul.display();
            res = eval(player, len, turn) + prisonpnt(player);
            debugstr();
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
                    res = value_victory_smarter(player, turn, len);
                    debugstr();
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
                    return cur_alpha;
                }

            }
            else
            {
                if (m.play(candidat.lst.get(i),turn))
                {
                    res = value_victory_smarter(player, turn, len);
                    debugstr();
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
                    return cur_beta;
                }
            }

        }

        if (depth == Game.max_depth)
        {
            //candidat.display_candidat(map);
            //System.out.printf("prisoners[0] : %d, prisoners[1] : %d\n", prisoners[0], prisoners[1]);
            //scsimul.display();
            //System.out.println("At the end !!!!!!!!!!!!!!!!!!!!!!!!!");
            //display_map();
        }

        if (turn == player)
            return max(values);
        else
            return min(values);
    }
}
