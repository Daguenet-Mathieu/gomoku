package org.modelai;
import java.util.ArrayList;

import org.utils.*;

public class Miniscore {

    Score sc;
    int [][][] str;
    int [][][] str1;
    int [][][] str2;
    int cur_turn;
    int opponant;
    int x;
    int y;
    int dx;
    int dy;
    int dir;

    int [] capt;
    int [] bpoint;

    boolean victory;

    ArrayList<Blocker> blocklist = new ArrayList<Blocker>();

    static int [] factor = {0, 0, 2, 10, 25, 0, 0, 0, 0, 0};
    static int [][] ddir = {{1, 0}, {0, 1}, {1, 1}, {-1, 1}};

    public Miniscore ()
    {
        this.sc = new Score();
        this.str1 = new int[8][19][19];
        this.str2 = new int[8][19][19];
        this.victory = false;
        this.capt = new int[2];
        this.bpoint = new int[2];
    }

    //display utils
    boolean no_case(int [][] str)
    {
        for (int i = 0 ; i < 19 ; i++)
        {
            for (int j = 0 ; j < 19 ; j++)
                if (str[i][j] != 0)
                    return false;
        }
        return true;
    }
    //display function
    public void display_str(int player)
    {
        int [][][] choice = player == 1 ? str1 : str2;

        String dir [] = {"VERTICAL","HORIZONTAL", "DIAGPOS", "DIAGNEG"};

        for (int d = 0 ; d < 4 ; d++)
        {
            if (no_case(choice[d]))
                continue;
            System.out.printf("player %d, direction %s\n", player, dir[d]);
            for (int i  = 0 ; i < 19 ; i++)
            {
                for (int j = 0 ; j < 19 ; j++)
                {
                    System.out.printf("%2d", choice[d][i][j]);
                }
                System.out.println();
            }
        }
    }

    //display function
    public void display()
    {
        display_str(1);
        display_str(2);
        display_blockers();
        display_miniscore();
    }

    //display function
    public void display(boolean blk)
    {
        display_str(1);
        display_str(2);
        if (blk)
            display_blockers();
        display_miniscore();
    }

    //display function
    public void display_miniscore()
    {
        int res = sc.one - sc.two + capt[0]  * 10 - capt[1] * 10;
        System.out.printf("score %d %d diff %d (%d %d) [%d]\nbpoint %d %d\n", sc.one, sc.two, sc.one-sc.two, capt[0], capt[1], res, bpoint[0], bpoint[1]);
    }

    //display function
    public void display_blockers()
    {
        Blocker b;
        if (this.blocklist.size() !=0)
        {
            for (int i = 0 ; i < blocklist.size() ; i++)
            {
                b = blocklist.get(i);
                System.out.printf("blockers [%d %d]  [%d %d] ", 
                b.bl1[0], b.bl1[1], b.bl2[0], b.bl2[1]);
                for (int j = 0 ; j < b.rank ; j++)
                {
                    System.out.printf("|%d %d|", b.cases[j][0], b.cases[j][1]);
                }
                System.out.printf("\n");
            }

        }
        else
        {
            System.out.println("no blockers");
        }
    }

    public void reset_str()
    {
        for (int d = 0 ; d < 4 ; d++)
        {
            for (int i  = 0 ; i < 19 ; i++)
            {
                for (int j = 0 ; j < 19 ; j++)
                {
                    this.str1[d][i][j] = 0;
                    this.str2[d][i][j] = 0;
                }
            }
        }
        this.victory=false;
        this.blocklist.clear();
        this.sc.reset();
    }

    //utils
    private boolean in_goban(int x, int y) 
    {
        if (x >=0 && x < 19 && y >=0 && y < 19)
            return true;
        return false;
    }

    //utils
    private boolean case_in_goban (int x, int y)
    {
        return in_goban(x, y) && MinMax.map[x][y] == 0;
    }

    //utils
    public boolean is_player(int c)
    {
        if (c == 1 || c == 2)
            return true;
        return false;
    }

    private void rep_case(int x, int y, int st)
    {
        if (x >= 0 && x < 19 && y >= 0 && y < 19 && MinMax.map[x][y] == 0)
        {
            if (cur_turn == 1)
            {
                sc.one -= factor[str1[dir][x][y]];
                if (in_goban(x+dx, y+dy) && MinMax.map[x+dx][y+dy] == cur_turn)
                    str1[dir][x][y] = Math.min(4, str1[dir][x][y] + st);
                else
                    str1[dir][x][y] = st;

                sc.one += factor[str1[dir][x][y]];
            }
            else
            {
                sc.two -= factor[str2[dir][x][y]];
                if (in_goban(x+dx, y+dy) && MinMax.map[x+dx][y+dy] == cur_turn)
                    str2[dir][x][y] = Math.min(4, str2[dir][x][y] + st);
                else
                    str2[dir][x][y] = st;
                sc.two += factor[str2[dir][x][y]];
            }
        }
    }

    private void spown_case(int x, int y, int st)
    {
        if (cur_turn == 1)
        {
            str2[dir][x][y] = st;
            sc.two += factor[st];
        }
        else
        {
            str1[dir][x][y] = st;
            sc.one += factor[st];
        }
    }

    private void add_case(int x, int y, int st)
    {
        if ( x >= 0 && x < 19 && y >= 0 && y < 19 && MinMax.map[x][y] == 0)
        {
            if (cur_turn == 1)
            {
                sc.one -= factor[str1[dir][x][y]];
                str1[dir][x][y] = st;
                sc.one += factor[str1[dir][x][y]];
            }
            else
            {
                sc.two -= factor[str2[dir][x][y]];
                str2[dir][x][y] = st;
                sc.two += factor[str2[dir][x][y]];
            }
        }
    }

    public void new_alignment(int dec1, int dec2, int st)
    {
        if (st == 2)
            mod_capt(dec1, dec2, 1);


        if (case_in_goban(x + (dec1 * dx), y + (dec1 * dy)))
            rep_case(x + (dec1 * dx), y + (dec1 * dy), st);

        if (case_in_goban(x + (dec2 * dx), y + (dec2 * dy)))
            rep_case(x + (dec2 * dx), y + (dec2 * dy), st);
    }

    public void connect()
    {
        this.str = MinMax.map[x][y] == 1 ? str1 : str2;
        if (str[dir][x][y] == 0)
        {
            if (in_goban(x-dx, y-dy) && MinMax.map[x-dx][y-dy] == cur_turn)
                new_alignment(-2, 2, 3);
            else
                new_alignment(-1, 2, 2);
        }
        else if (str[dir][x][y] == 2)
        {
            if (in_goban(x-dx, y-dy) && MinMax.map[x-dx][y-dy] == cur_turn)
            {

                if (in_goban(x-2*dx, y-2*dy) && MinMax.map[x - 2*dx][y - 2*dy] == cur_turn)
                    new_alignment(-3, 2, 4);
                else
                    new_alignment(3, -2, 4);

            }
            else
                new_alignment(-1, 3, 3);
        }
        else if (str[dir][x][y] == 3)
        {
            int decp = 0;
            int decn = 0;
            for (int i = 1 ; in_goban(x+i*dx, y+i*dy) && MinMax.map[x+i*dx][y+i*dy] == cur_turn ; i++)
                decp++;
            for (int i = 1 ; in_goban(x-i*dx, y-i*dy) && MinMax.map[x-i*dx][y-i*dy] == cur_turn ; i++)
                decn++;

            if (decp + decn >= 4)
                save_victory();
            else
            {
                rep_case(x - (decn+1)*dx, y - (decn+1)*dy, 4);
                rep_case(x + (decp+1)*dx, y + (decp+1)*dy, 4);
            }

        }
        else if (str[dir][x][y] == 4)
        {
            save_victory();
        }
    }

    public void fill(int x, int y)
    {
        int st;
        for (int i = 0 ; i < 4 ; i++)
        {
            st = str1[i][x][y];
            sc.one -= (factor[st]);
            str1[i][x][y]=0;

            st = str2[i][x][y];
            sc.two -= (factor[st]);
            str2[i][x][y]=0;
        }
    }

    public boolean pos_cap(int x, int y, int dpx, int dpy,  int val)
    {
        if (!in_goban(x+3*dpx, y+3*dpy))
            return false;
        if (MinMax.map[x+3*dpx][y+3*dpy] == 0 &&  MinMax.map[x+dpx][y+dpy]== val && MinMax.map[x+2*dpx][y+2*dpy] == val)
            return true;
        return false;

    }

    public void fill2()
    {
        int st;
        int sig;

        for (int i = 0 ; i < 4 ; i++)
        {

            if (str1[i][x][y] != 0)
            {
                st = str1[i][x][y];

                if (st == 2 || (st == 3 && 
                in_goban(x+ ddir[i][0], y + ddir[i][1]) &&
                MinMax.map[x+ ddir[i][0]][y + ddir[i][1]] == 1 &&
                in_goban(x- ddir[i][0], y - ddir[i][1]) &&
                MinMax.map[x- ddir[i][0]][y - ddir[i][1]] == 1))
                {
                    if (in_goban( x+ ddir[i][0], y + ddir[i][1]) &&
                        MinMax.map[x+ ddir[i][0]][y + ddir[i][1]] == 1 && in_goban(x+2* ddir[i][0], y +2* ddir[i][1]) &&
                    MinMax.map[x+2*ddir[i][0]][y + 2*ddir[i][1]] == 1)
                        sig = 1;
                    else
                        sig = -1;
        
                        if (in_goban(x+ sig * 3* ddir[i][0], y + sig * 3 * ddir[i][1]) &&
                            MinMax.map[x+ sig * 3* ddir[i][0]][y + sig * 3 * ddir[i][1]] == 2)
                        {
                            capt[1]--;
                        }

                    if (cur_turn == 2)
                    {
                        if (in_goban(x+ sig * 3* ddir[i][0], y + sig * 3 * ddir[i][1]) &&
                            MinMax.map[x+ sig * 3* ddir[i][0]][y + sig * 3 * ddir[i][1]] == 0)
                        {
                            capt[1]++;
                        }
                    }
                }

                if (st == 4 && cur_turn == 2)
                {
                    if (in_goban(x + ddir[i][0], y + ddir[i][1]) && in_goban(x - ddir[i][0], y - ddir[i][1]) &&
                        MinMax.map[x + ddir[i][0]][y + ddir[i][1]] == 1 && MinMax.map[x - ddir[i][0]][y - ddir[i][1]] == 1)
                    {
                        if (pos_cap(x, y, ddir[i][0], ddir[i][1], 1))
                            capt[1]++;
                        if (pos_cap(x, y, -ddir[i][0], -ddir[i][1], 1))
                            capt[1]++;
                    }
                }


                sc.one -= (factor[st]);            
                str1[i][x][y]=0;
            }

            if (str2[i][x][y] != 0)
            {
                st = str2[i][x][y];

                if (st == 2 || (st == 3 && 
                in_goban(x+ ddir[i][0], y + ddir[i][1])
                && MinMax.map[x+ ddir[i][0]][y + ddir[i][1]] == 2 && 
                in_goban(x- ddir[i][0], y - ddir[i][1]) &&
                MinMax.map[x- ddir[i][0]][y - ddir[i][1]] == 2))
                {
                    if (in_goban(x+ ddir[i][0], y + ddir[i][1]) && MinMax.map[x+ ddir[i][0]][y + ddir[i][1]] == 2 &&
                     in_goban(x+2*ddir[i][0], y +2*ddir[i][1]) && MinMax.map[x+2*ddir[i][0]][y +2*ddir[i][1]] == 2)
                        sig = 1;
                    else
                        sig = -1;
        
                        if ( in_goban(x+ sig * 3* ddir[i][0], y + sig * 3 * ddir[i][1]) &&
                            MinMax.map[x+ sig * 3* ddir[i][0]][y + sig * 3 * ddir[i][1]] == 1)
                        {
                            capt[0]--;
                        }

                    else if (cur_turn == 1)
                    {
                        if (in_goban(x+ sig * 3* ddir[i][0], y + sig * 3 * ddir[i][1]) &&
                            MinMax.map[x+ sig * 3* ddir[i][0]][y + sig * 3 * ddir[i][1]] == 0)
                        {
                            capt[0]++;
                        }
                    }
                }

                if (st == 4 && cur_turn == 1)
                {
                    if (in_goban(x + ddir[i][0], y + ddir[i][1]) && in_goban(x - ddir[i][0], y - ddir[i][1]) &&
                        MinMax.map[x + ddir[i][0]][y + ddir[i][1]] == 2 && MinMax.map[x - ddir[i][0]][y - ddir[i][1]] == 2)
                    {
                        if (pos_cap(x, y, ddir[i][0], ddir[i][1], 2))
                            capt[0]++;
                        if (pos_cap(x, y, -ddir[i][0], -ddir[i][1], 2))
                            capt[0]++;
                    }
                }

                sc.two -= (factor[st]);

                str2[i][x][y]=0;
            }
            if (in_goban(x + 5 * ddir[i][0], y + 5 * ddir[i][1]))
            {
                if (MinMax.map[x + 5 * ddir[i][0]][y + 5 * ddir[i][1]] == cur_turn)
                {
                    create_blocker(i, 1);   
                }
            }
            else if (Game.large_cut == false && !(x > 4 && x < 14 && y < 4 && y < 14))
                create_blocker(i, 1);

            if (in_goban(x - 5 * ddir[i][0], y - 5 * ddir[i][1]))
            { 
                if (MinMax.map[x - 5 * ddir[i][0]][y - 5 * ddir[i][1]] == cur_turn)
                {
                    create_blocker(i, -1);   
                }
            }
            else if (Game.large_cut == false && !(x > 4 && x < 14 && y < 4 && y < 14))
                create_blocker(i, -1);
        }
    }

    private void create_blocker(int dir, int sig)
    {
        Blocker res = new Blocker(cur_turn, dir, sig);
        res.bl1(x, y);

        if (!in_goban(x+ sig* 5*ddir[dir][0], y + sig * 5 * ddir[dir][1]))
            res.bl2(-1, -1);
        else
            res.bl2(x+ 5*ddir[dir][0]*sig, y + 5 * ddir[dir][1]*sig);

        res.update_block_info();
        this.blocklist.add(res);
    }

    private void update_blocker()
    {
        Blocker b;
        bpoint[0] = 0;
        bpoint[1] = 0;

        for (int i = 0 ; i < this.blocklist.size() ; i++)
        {
            b = this.blocklist.get(i);

            b.update_block_info();
            for (int j = 0 ; j < b.rank ; j++)
            {
                if (b.color == 1)
                    bpoint[0] += factor[str1[b.dir][b.cases[j][0]][b.cases[j][1]]];
                else
                    bpoint[1] += factor[str2[b.dir][b.cases[j][0]][b.cases[j][1]]];

            }
            if (MinMax.map[b.bl1[0]] [b.bl1[1]] != b.blockcolor || ( b.bl2[0] != -1 &&
                MinMax.map[b.bl2[0]] [b.bl2[1]] != b.blockcolor))
            {

                this.blocklist.remove(i);
                i--;
            }
            if (i != -1 && Game.large_cut == true && b.bl2[0] == -1)
                this.blocklist.remove(i);
        }
    }

    private void save_victory()
    {
            if (!this.victory)
            {
                this.victory = true;
                return;
            }
            this.victory=false;
    }

    public void mod_capt(int dec1, int dec2, int val)
    {
        if (!in_goban(x+dec1*dx, y+dec1*dy) || !in_goban(x+dec2*dx, y+dec2*dy))
            return;

        if (MinMax.map[x+dec1*dx][y+dec1*dy] == opponant && MinMax.map[x+dec2*dx][y+dec2*dy] == 0)
            capt[opponant - 1]+=val;
        else if (MinMax.map[x+dec1*dx][y+dec1*dy] == 0 && MinMax.map[x+dec2*dx][y+dec2*dy] == opponant)
            capt[opponant - 1]+=val;
        
    }

    private int min4(int decx, int decxx)
    {
        if (decx == 1 && decxx == 1)
            return 0;
        if (decx + decxx <=1)
            return 0;
        return Math.min(4, decx + decxx);
    }


    public void unconnect()
    {

        int decp;
        int decn;
        int decpp;
        int decnn;
    
        decp = 0;
        decn = 0;
        decpp = 0;
        decnn = 0;

        for (int i = 1; in_goban(x+i*dx, y+i*dy) && MinMax.map[x + i * dx][y+ i * dy] == cur_turn ; i++)
            decp++;
        for (int i = 1; in_goban(x-i*dx, y-i*dy) && MinMax.map[x- i *dx][y - i *dy] == cur_turn ; i++)
            decn++;
        if (in_goban(x+(decp + 1)*dx, y+(decp + 1)*dy) && MinMax.map[x+(decp + 1)*dx][y+(decp + 1)*dy] == 0)
        {
            for (int i = decp+1 ; in_goban(x+(i+1)*dx, y+(i+1)*dy) && MinMax.map[x+(i+1)*dx][y+(i+1)*dy] == cur_turn; i++)
                decpp++;
        }
    
        if (in_goban(x-(decn + 1)*dx, y-(decn + 1)*dy) && MinMax.map[x-(decn + 1)*dx][y-(decn + 1)*dy] == 0)
        {
            for (int i = decn+1 ; in_goban(x-(i+1)*dx, y-(i+1)*dy) && MinMax.map[x-(i+1)*dx][y-(i+1)*dy] == cur_turn; i++)
                decnn++;
        }
        
        if (decp + decn + 1 == 3)
        {
            if (decp == 2 && in_goban(x+3*dx, y+3*dy) && MinMax.map[x+3*dx][y+3*dy] == opponant)
                capt[opponant-1]++;
            else if (decn == 2 && in_goban(x-3*dx, y-3*dy) && MinMax.map[x-3*dx][y-3*dy] == opponant)
                capt[opponant-1]++;
        }

        else if (decp + decn + 1 == 2)
        {
            if (decp !=0)
                mod_capt( -1, 2, -1);
            else if (decn != 0)
                mod_capt(-2, 1, -1);
        }

        else if (decp + decn + 1 == 4)
        {
            if (decp == 2 && in_goban(x+3*dx, y+3*dy) && MinMax.map[x+3*dx][y+3*dy] == opponant)
                capt[opponant-1]++; 
            else if (decn == 2 && in_goban(x-3*dx, y-3*dy) && MinMax.map[x-3*dx][y-3*dy] == opponant)
                capt[opponant-1]++;
        }



        add_case(x + (decp + 1) * dx, y + (decp + 1) * dy, min4(decp, decpp));
        add_case(x - (decn + 1) * dx, y - (decn + 1) * dy, min4(decn, decnn));

        if (decp + decn == 3)
            add_case(x, y, 3);
        else if (decp == 2 || decn == 2)
            add_case(x, y, 2);
        else if (decp + decn >= 4)
            add_case(x, y, 4);

    }

    public void unfill()
    {
        int val = cur_turn == 1 ? 2 : 1;
        int nbp = 0;
        int nbn = 0;

        for (int i = 1; in_goban(x+i*dx, y+i*dy) && MinMax.map[x + i * dx][y+ i * dy] == val ; i++)
            nbp++;
        for (int i = 1; in_goban(x-i*dx, y-i*dy) && MinMax.map[x- i *dx][y - i *dy] == val ; i++)
            nbn++;

        if (nbn == 0 && nbp == 0)
            return;

        if (nbn == 2 && in_goban(x-3*dx, y-3*dy))
        {
            if (MinMax.map[x-3*dx][y-3*dy] == 0)
                capt[cur_turn - 1]--;
            if (MinMax.map[x-3*dx][y-3*dy] == cur_turn)
                capt[cur_turn - 1]++;
        }

        if (nbp == 2 && in_goban(x+3*dx, y+3*dy))
        {
            if (MinMax.map[x+3*dx][y+3*dy] == 0)
                capt[cur_turn - 1]--;
            if (MinMax.map[x+3*dx][y+3*dy] == cur_turn)
                capt[cur_turn - 1]++;
        }

        if (nbn < 2)
            nbn = 0;
        if (nbp < 2)
            nbp = 0;

        spown_case(x, y, Math.min(4, nbp + nbn));
    }

    public void analyse_unmove(int x, int y, int turn)
    {

        this.cur_turn = turn;
        this.str = turn == 1 ? this.str1 : this.str2;
        this.opponant = turn == 1 ? 2 : 1;
        this.x=x;
        this.y=y;

        if ((x + 1 != 19 && is_player(MinMax.map[x+1][y])) || (x - 1 != -1 && is_player(MinMax.map[x-1][y])))
        {
            dir = 0;
            dx = 1; dy = 0;
            if (x + 1 != 19 && MinMax.map[x+1][y] == cur_turn)
                unconnect();

            else if (x - 1 != -1 && MinMax.map[x-1][y] == cur_turn)
            {
                dx=-1;
                unconnect();
            }
            unfill();
        }

        if ((y + 1 != 19 && is_player(MinMax.map[x][y+1])) || (y - 1 != -1 && is_player(MinMax.map[x][y-1])))
        {
            dir = 1;
            dx = 0; dy = 1;
            if (y + 1 != 19 && MinMax.map[x][y+1] == cur_turn)
                unconnect();

            else if (y - 1 != -1 && MinMax.map[x][y-1] == cur_turn)
            {
                dy=-1;
                unconnect();
            }
            unfill();
        }

        if ((x + 1 != 19 && y + 1 != 19 && is_player(MinMax.map[x+1][y+1])) || ( x - 1 != -1 && y - 1 != -1 && is_player(MinMax.map[x-1][y-1])))
        {
            dir = 2;
            dx = 1; dy = 1;
            if (x + 1 != 19 && y + 1 != 19 && MinMax.map[x+1][y+1] == cur_turn)
                unconnect();

            else if ( x - 1 != -1 && y - 1 != -1 && MinMax.map[x-1][y-1] == cur_turn)
            {
                dx=-1; dy=-1;
                unconnect();
            }
            unfill();
        }

        if (x + 1 != 19 && y - 1 != -1 && is_player(MinMax.map[x+1][y-1]) || (x - 1 != -1 && y + 1 != 19 && is_player(MinMax.map[x-1][y+1])))
        {
            dir = 3;
            dx = 1; dy = -1;
            if (x + 1 != 19 && y - 1 != -1 && MinMax.map[x+1][y-1] == cur_turn)
                unconnect();  

            else if (x - 1 != -1 && y + 1 != 19 && MinMax.map[x-1][y+1] == cur_turn)
            {
                dx = -1; dy=1;
                unconnect();
            }
            
            unfill();
        }

        for (int i = 0; i < 4 ; i++)
        {
            unfill0(x, y, i);
        }

        update_blocker();
    }


    public void unfill0(int x, int y, int dir)
    {
        int cmp;
        if (in_goban(x+ddir[dir][0], y+ddir[dir][1]) &&
        MinMax.map[x+ddir[dir][0]][y+ddir[dir][1]] == 0)
        {
            if (cur_turn == 1 && str1[dir][x+ddir[dir][0]][y+ddir[dir][1]] > 2 
            || cur_turn == 2 && str2[dir][x+ddir[dir][0]][y+ddir[dir][1]] > 2)
            {
                cmp = 0;
                for (int i = 2 ; in_goban(x+i*ddir[dir][0], y+i*ddir[dir][1]) 
                && MinMax.map[x+i*ddir[dir][0]][y+i*ddir[dir][1]] == cur_turn ; i++)
                    cmp++;
                if (cur_turn == 1)
                {
                    sc.one -= factor[str1[dir][x+ddir[dir][0]][y+ddir[dir][1]]];
                    str1[dir][x+ddir[dir][0]][y+ddir[dir][1]]=cmp;
                    sc.one += factor[cmp];
                }
                else
                {
                    sc.two -= factor[str2[dir][x+ddir[dir][0]][y+ddir[dir][1]]];
                    str2[dir][x+ddir[dir][0]][y+ddir[dir][1]]=cmp;
                    sc.two += factor[cmp];
                }
            }
        }

        if (in_goban(x-ddir[dir][0], y-ddir[dir][1]) &&
        MinMax.map[x-ddir[dir][0]][y-ddir[dir][1]] == 0)
        {
            if (cur_turn == 1 && str1[dir][x-ddir[dir][0]][y-ddir[dir][1]] > 2 
            || cur_turn == 2 && str2[dir][x-ddir[dir][0]][y-ddir[dir][1]] > 2)
            {
                cmp = 0;
                for (int i = 2 ; in_goban(x-i*ddir[dir][0], y-i*ddir[dir][1]) 
                && MinMax.map[x-i*ddir[dir][0]][y-i*ddir[dir][1]] == cur_turn ; i++)
                    cmp++;
                if (cur_turn == 1)
                {
                    sc.one -= factor[str1[dir][x-ddir[dir][0]][y-ddir[dir][1]]];
                    str1[dir][x-ddir[dir][0]][y-ddir[dir][1]]=cmp;
                    sc.one += factor[cmp];
                }
                else
                {
                    sc.two -= factor[str2[dir][x-ddir[dir][0]][y-ddir[dir][1]]];
                    str2[dir][x-ddir[dir][0]][y-ddir[dir][1]]=cmp;
                    sc.two += factor[cmp];
                }
            }
        }
    }

    public void analyse_move(int x, int y, int turn)
    {
        this.cur_turn = turn;
        this.opponant = turn == 1 ? 2 : 1;
        this.str = turn == 1 ? str1 : str2;
        this.x = x;
        this.y = y;

        if (x + 1 != 19 && MinMax.map[x+1][y] == cur_turn)
        {
            dir=0;
            dx=1;dy=0;
            connect();

        }
        else if (x - 1 != -1 && MinMax.map[x-1][y] == cur_turn)
        {
            dir=0;
            dx=-1; dy=0;
            connect();
        }

        if (y + 1 != 19 && MinMax.map[x][y+1] == cur_turn)
        {
            dir=1;
            dx=0;dy=1;
            connect();
        }
        else if (y - 1 != -1 && MinMax.map[x][y-1] == cur_turn)
        {
            dir = 1;
            dx=0;dy=-1;
            connect();
        }

        if (x + 1 != 19 && y + 1 != 19 && MinMax.map[x+1][y+1] == cur_turn)
        {
            dir = 2;
            dx=1;dy=1;
            connect();
        }
        else if (x - 1 != -1 && y - 1 != -1 && MinMax.map[x-1][y-1] == cur_turn)
        {
            dir = 2;
            dx=-1;dy=-1;
            connect();
        }

        if (x + 1 != 19 && y - 1 != -1 && MinMax.map[x+1][y-1] == cur_turn)
        {
            dir = 3;
            dx=1;dy=-1;
            connect();
        }
        else if (x - 1 != -1 && y + 1 != 19 && MinMax.map[x-1][y+1] == cur_turn)
        {
            dir = 3;
            dx=-1;dy=1;
            connect();
        }
        fill2();
        update_blocker();
    }


    //debug function
    public boolean check_str()
    {
        int score1 = 0;
        int score2 = 0;
    
        for (int d = 0 ; d < 4 ; d++)
        {
            for (int i = 0 ; i < 19 ; i++)
            {
                for (int j = 0 ; j < 19 ; j++)
                {
                    if (str1[d][i][j] != 0)
                        score1 += factor[str1[d][i][j]];
                    if (str2[d][i][j] != 0)
                        score2 += factor[str2[d][i][j]];
                    if (str1[d][i][j] >= 5 || str2[d][i][j] >=5)
                        return false;
                    if (str1[d][i][j] != 0 && MinMax.map[i][j] != 0)
                        return false;
                    if (str2[d][i][j] != 0 && MinMax.map[i][j] != 0)
                        return false;
                }
            }
        }
        if (score1 != sc.one || score2 != sc.two)
        {
            System.out.printf("%d %d found (%d %d)\n", sc.one, sc.two, score1, score2);
            return false;
        }
        return true;
    }

    //debug function
    private int iscapt(int x, int y)
    {
        int res = 0;
        int o = MinMax.map[x][y] == 1 ? 2 : 1;

        for (int d = 0 ; d < 4 ; d++)
        {
            if ( in_goban(x+3*ddir[d][0], y+3*ddir[d][1]) &&
                MinMax.map[x+ddir[d][0]][y+ddir[d][1]] == o && MinMax.map[x+2*ddir[d][0]][y+2*ddir[d][1]] == o 
            && MinMax.map[x+3*ddir[d][0]][y+3*ddir[d][1]] ==0)
                res++;
            if (
                in_goban(x-3*ddir[d][0], y-3*ddir[d][1]) &&
                MinMax.map[x-ddir[d][0]][y-ddir[d][1]] == o && MinMax.map[x-2*ddir[d][0]][y-2*ddir[d][1]] == o 
            && MinMax.map[x-3*ddir[d][0]][y-3*ddir[d][1]]==0)
                res++;
        }
        return res;
    }

    //debug function
    public boolean check_capt()
    {
        int capt1 = 0;
        int capt2 = 0;
        int nb_cap;

        for (int i = 0 ; i < 19 ; i++)
        {
            for (int j = 0 ; j < 19 ; j ++)
            {
                if (MinMax.map[i][j] != 0)
                {
                    nb_cap = iscapt(i, j);
                    if (nb_cap != 0) 
                    {
                        if (MinMax.map[i][j] == 1)
                            capt1+=nb_cap;
                        else
                            capt2+=nb_cap;
                    }
                }
            }
        }

        if (check_str() == false)
        {
            System.out.println("check str wrong");
            return true;
        }

        if (capt1 != this.capt[0] || capt2 != this.capt[1])
        {
            System.out.printf("Check capt : (%d %d) found %d %d\n", this.capt[0], this.capt[1], capt1, capt2);
            return true;
        }
        return false;
    }
}