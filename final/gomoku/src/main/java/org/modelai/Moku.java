package org.modelai;

public class Moku extends MinMax {

    public Moku()
    {
        map = new int[19][19];

        this.move = new Candidat.coord(-1, -1);

        scsimul = new Miniscore();
        candidat = new Candidat();
        pos_counter = 0;
        nbmove = 0;

    }

    public boolean play(Candidat.coord c, int player)
    {
        this.move = c;
        map[c.x][c.y] = player;

        this.candidat.save(c);
        scsimul.analyse_move(c.x, c.y, player);


        return check_win_4_dir(c.x, c.y);
    }

    public void unplay(Candidat.coord c, int depth)
    {
        int val = map[c.x][c.y];
        map[c.x][c.y] = 0;
        scsimul.analyse_unmove(c.x, c.y, val);
    }

    public Moku (int len)
    {
        this.len = len + 1;
        this.move = new Candidat.coord(-1, -1);
        this.candidat = new Candidat();
    }

    public float minmax(int depth, int turn, int player)
    {   
        int nb_candidates;

        nb_candidates = candidat.old_load(map, depth);

        if (depth == 0)
        {
            //display_map();
            // scsimul.display();
            pos_counter++;
            return eval(player, len, turn);
        }

        values = new float[nb_candidates];

        for (int i = 0 ; i < nb_candidates ; i++)
        {
            Moku m = new Moku(this.len);
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
