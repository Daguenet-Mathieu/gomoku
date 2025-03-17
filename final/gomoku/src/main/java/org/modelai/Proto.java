package org.modelai;
import java.util.ArrayList;

public class Proto extends MinMax {

    // public Candidat.coord [] removed;
    static public int [] prisoners;
    static ArrayList <Candidat.coord> prisonlst;

    public Proto()
    {
        map = new int[19][19];
        //removed = new Candidat.coord[2];
        this.move = new Candidat.coord(-1, -1);
        prisoners = new int[2];
        scsimul = new Miniscore();
        candidat = new Candidat();
        prisonlst = new ArrayList<Candidat.coord>();
        pos_counter = 0;
        nbmove = 0;
    }

    private void remove(int x, int y)
    {
        int val = map[x][y];
        increase_prisoners(val);
        map[x][y] = 0;
        scsimul.analyse_unmove(x, y, val);
        prisonlst.add(new Candidat.coord(x, y));
    }

    private boolean remove_capture(int x, int y, int dx, int dy, int p, int o)
    {
        if (in_goban(x+3*dx, y+3*dy) && map[x + dx][y + dy] == o && map[x + 2 * dx][y + 2 * dy] == o && map[x + 3 * dx][y + 3 * dy] == p)
        {
            remove(x+dx, y+dy);
            remove(x+2*dx, y+2*dy);
            return true;
        }

        if (in_goban(x-3*dx, y - 3*dy) && map[x - dx][y - dy] == o && map[x - 2 * dx][y - 2 * dy] == o && map[x - 3 * dx][y - 3 * dy] == p)
        {
            remove(x-dx, y-dy);
            remove(x-2*dx, y-2* dy);
            return true;
        }
        return false;
    }

    private void increase_prisoners(int turn)
    {
        if (turn == 1)
            prisoners[0]++;
        else
            prisoners[1]++;
    }

    public void remove_captured(int x, int y, int turn)
    {  
        final int op = turn == 1 ? 2 : 1;

        for (int i = 0 ; i < 4 ; i++)
            remove_capture(x, y, ddir[i][0], ddir[i][1], turn, op);
    }
    /*
    public boolean play(Candidat.coord c, int player)
    {
        this.move = c;
        map[c.x][c.y] = player;
        remove_captured(c.x, c.y, player);

        this.candidat.save(c);
        scsimul.analyse_move(c.x, c.y, player);


        return check_win_4_dir(c.x, c.y);
    }*/

    public boolean play(Candidat.coord c, int player)
    {
        this.move = c;
        map[c.x][c.y] = player;
        // remove_captured(c.x, c.y, player);

        // this.candidat.save(c);
        // scsimul.analyse_move(c.x, c.y, player);


        return check_win_4_dir(c.x, c.y);
    }


    public void play_prisoners(int val)
    {
        int o = val == 1 ? 2 : 1;
        Candidat.coord c;

        if (prisonlst.size() >= 2)
        {
            for (int i = 0 ; i < 2 ; i++)
            {
                c = prisonlst.get(prisonlst.size()-1);
                map[c.x][c.y] = o;
                scsimul.analyse_move(c.x, c.y, o);
                prisonlst.remove(prisonlst.size()-1);
            }
        }

    }

    /*
    public void unplay(Candidat.coord c, int depth)
    {
        int val = map[c.x][c.y];
        map[c.x][c.y] = 0;
        scsimul.analyse_unmove(c.x, c.y, val);
        play_prisoners(val);
    } */

    public void unplay(Candidat.coord c, int depth)
    {
        map[c.x][c.y] = 0;
    }

    public Proto (int len)
    {
        this.len = len + 1;
        this.move = new Candidat.coord(-1, -1);
        this.candidat = new Candidat();
    }

    public float minmax(int depth, int turn, int player)
    {   
        int nb_candidates;

        nb_candidates = candidat.old_load(depth);

        if (depth == 0)
        {
            pos_counter++;
            return eval(player, len, turn);
        }

        values = new float[nb_candidates];

        for (int i = 0 ; i < nb_candidates ; i++)
        {
            Proto m = new Proto(this.len);
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
}