package org.modelai;

public class Moku extends MinMax {

    public Moku()
    {
        map = new int[19][19];

        this.move = new Candidat.coord(-1, -1);

        scsimul = new Miniscore();
        candidat = new Candidat(false);
        pos_counter = 0;
        nbmove = 0;

    }

    public boolean play(Candidat.coord c, int player)
    {
        //System.out.printf("play %d %d\n", c.x, c.y);
        if (check_win_4_dir(c.x, c.y, player))
            return true;
        this.move = c;
        map[c.x][c.y] = player;

        candidat.save(c);
        scsimul.analyse_move(c.x, c.y, player);

        return false;
    }

    public void unplay(Candidat.coord c, int depth)
    {
        //System.out.printf("unplay %d %d\n", c.x, c.y);
        int val = map[c.x][c.y];
        map[c.x][c.y] = 0;
        scsimul.analyse_unmove(c.x, c.y, val);
    }

    public Moku (int len)
    {
        this.len = len + 1;
        this.move = new Candidat.coord(-1, -1);
        this.candidat = new Candidat(false);
    }

    public float minmax(int depth, int turn, int player)
    {   
        int nb_candidates;

        nb_candidates = candidat.old_load(depth, player);

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

    public float minmaxab(int depth, int turn, int player, float alpha, float beta)
    {   
        int nb_candidates;
        float cur_alpha;
        float cur_beta;
        float res;

        scsimul.cur_turn = turn;


        nb_candidates = candidat.old_load(depth, turn);

        //MinMax.display_Map();


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
            Moku m = new Moku(this.len);

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
