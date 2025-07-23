package org.modelai;
import java.util.ArrayList;

import org.utils.*;

public class Miniscore {

    Score sc;
    int [][][] str;
    int [][][] str1;
    int [][][] str2;
    //int cur_player;
    int cur_turn;
    int opponant;
    int x;
    int y;
    int dx;
    int dy;
    int dir;

    int [] free4;
    int [] simp4;
    int [] free3;
    int [] capt;
    int [] bpoint;

    boolean victory;
    boolean lastcap;

    ArrayList<Blocker> blocklist = new ArrayList<Blocker>();

    //static int [] factor = {0, 0, 2, 5, 10, 10};
    static int [] factor = {0, 0, 2, 10, 25, 0, 0, 0, 0, 0};
    //static int [] factor = {0, 0, 2, 3, 4};
    static int [][] ddir = {{1, 0}, {0, 1}, {1, 1}, {-1, 1}};

    public Miniscore ()
    {
        this.sc = new Score();
        this.str1 = new int[8][19][19];
        this.str2 = new int[8][19][19];
        this.victory = false;
        this.free4 = new int[2];
        this.simp4 = new int[2];
        this.free3 = new int[2];
        this.capt = new int[2];
        this.bpoint = new int[2];
    
        free4[0] = 0; free4[1] = 0;
        free3[0] = 0; free3[1] = 0;
        simp4[0] = 0; simp4[1] = 0;
    }

    int free_score(int player, int turn)
    {
        int other = player == 0 ? 1 : 0;

        if (simp4[player] >= 2)
            {
                if (simp4[other] >= 2)
                    return turn == other ? 5000 : -5000;
                return 5000; 
            }
        else if (simp4[other] >= 2)
        {
            return -5000; 
        }

        else if (free4[player] >=1)
        {
            if (free4[other] >=1)
                return turn == other ? 4000 : -4000;
        }

        else if (free4[other] >=1)
        {
            return -4000;
        }

        else if (free3[player] >=2)
        {
            if (free3[other] >=2)
                return turn == other ? 3000 : -3000;
        }

        else if (free3[other] >=2)
        {
            return -3000;
        }

        return 0;
    }
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
        this.sc.reset();
    }

    private boolean in_goban(int x, int y) //move utils
    {
        if (x >=0 && x < 19 && y >=0 && y < 19)
            return true;
        return false;
    }

    public boolean is_player(int c) //move utils
    {
        if (c == 1 || c == 2)
            return true;
        return false;
    }

    public void remp_case(int x, int y, int val, int sig)
    {
        //System.out.printf("removing case at %d %d val %d\n", x, y, val);
        if (x >=0 && x < 19 && y >=0 && y <19)
        {
            if (cur_turn == 1)
            {                
                sc.one -= factor[str1[dir][x][y]];
                if (in_goban(x+sig*dx, y+sig*dy) && MinMax.map[x+sig*dx][y+sig*dy] == cur_turn && MinMax.map[x][y] == 0)
                {
                    //System.out.println("here");
                    str1[dir][x][y] -= val;
                    if (str1[dir][x][y] <0)
                    {
                        // System.out.printf("info %d %d %d\n", x, y, val);
                        // MinMax.display_Map();
                        // display();
                        str1[dir][x][y] = 0;
                    }
                    sc.one += factor[str1[dir][x][y]];
                    return;
                }
                str1[dir][x][y] = 0;
                //System.out.printf("Removed %d \n", str1[dir][x][y]);
            }
            else
            {
                //System.out.println("HEEEERE2");
                sc.two -= factor[str2[dir][x][y]];
                if (in_goban(x+sig*dx, y+sig*dy) && MinMax.map[x+sig*dx][y+sig*dy] == cur_turn && MinMax.map[x][y] == 0)
                {
                    str2[dir][x][y] -= val;
                    if (str2[dir][x][y] <0)
                    {
                        // System.out.printf("info %d %d %d", x, y, val);
                        // MinMax.display_Map();
                        // display();
                        str2[dir][x][y] = 0;
                    }
                    sc.two += factor[str2[dir][x][y]];
                    return;
                }
                str2[dir][x][y] = 0;
            }
        }

    }

    public void rem_case(int x, int y)
    {
        //System.out.printf("removing case at %d %d\n", x, y);
        if (x >=0 && x < 19 && y >=0 && y <19)
        {
            if (cur_turn == 1)
            {
                sc.one -= factor[str1[dir][x][y]];
                // if (MinMax.nbmove == 2 && MinMax.pos_counter == 918)
                //     {
                //         System.out.printf("DONE %d %d to 0", x, y);
                //     }
                str1[dir][x][y] = 0;
            }
            else
            {
                sc.two -= factor[str2[dir][x][y]];
                str2[dir][x][y] = 0;
            }
        }
    }

    private void dec(int []v, int player)
    {
        if (v[player] != 0)
            v[player] -=1;
    }

    private void dec_free(int st, int player)
    {
        if (st == 4)
            dec(free4, player-1);
        else if (st == 3)
            dec(free3, player - 1); 
    }

    private void dec_simp(int st, int player)
    {
        if (st == 4)
            dec(simp4, player -1);
    }

    private void inc_free(int st, int player)
    {
        if (st == 4)
            free4[player - 1] +=1;
        else if (st == 3)
            free3[player - 1] +=1;
    }



    // private void inc_free(int st)
    // {
    //     if (st == 4)
    //         free4[cur_turn - 1] +=1;
    //     else if (st == 3)
    //         free3[cur_turn - 1] +=1;
    // }

    private void inc_simp(int st, int player)
    {
        if (st == 4)
            simp4[player - 1] +=1;
    }

    private void inc_simp(int st)
    {
        if (st == 4)
            simp4[cur_turn - 1] +=1;
    }

    public void dec_frees(int st, int player)
    {
        dec_free(st, player);
        if (st == 4)
            inc_simp(st, player);
    }

    public void check_free(int x, int y, int pos, int neg, int st)
    {
        str = cur_turn == 1 ? str1 : str2;

        if (in_goban(x+pos*dx, y+pos*dy) && str[dir][x+ pos * dx][y + pos * dy] !=0 &&
            in_goban(x+neg*dx, y+neg*dy) && str[dir][x+ neg * dx][y + neg * dy] !=0)
        inc_free(st, cur_turn);

        else if ((in_goban(x+pos*dx, y+pos*dy) && str[dir][x+ pos * dx][y + pos * dy] !=0) ||
        (in_goban(x+neg*dx, y+neg*dy) && str[dir][x+ neg * dx][y + neg * dy] !=0))
            inc_simp(st);
    }


    private void rep_case(int x, int y, int st)
    {
        if (x >= 0 && x < 19 && y >= 0 && y < 19 && MinMax.map[x][y] == 0)
        {
            if (cur_turn == 1)
            {
                // if (st == 2 && in_goban(x-dx, y-dy) && MinMax.map[x-dx][y-dy] == opponant)
                // {
                //     sc.one -=10;
                // }
                sc.one -= factor[str1[dir][x][y]];
                if (in_goban(x+dx, y+dy) && MinMax.map[x+dx][y+dy] == cur_turn)
                    str1[dir][x][y] = Math.min(4, str1[dir][x][y] + st);
                else
                    str1[dir][x][y] = st;

                sc.one += factor[str1[dir][x][y]];
            }
            else
            {
                // if (st == 2 && in_goban(x-dx, y-dy) && MinMax.map[x-dx][y-dy] == opponant)
                // {
                //     sc.two -=10;
                // }
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
        // if (st <= 1)
        //     st = 0;
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

    private int free_hor(int x, int y, int dx, int dy)
    {
        int symb = MinMax.map[x][y];
        if (symb == 0)
            return 0;
        if (in_goban(x-dx, y-dy) && in_goban(x+3*dy, y+3*dx) && MinMax.map[x-dx][y-dy] == 0 && 
                MinMax.map[x+3*dx][y+3*dy] == 0 && MinMax.map[x+dx][y+dy] == symb
                && MinMax.map[x+2*dx][y+2*dy] == symb)
                return symb;
        return 0;

    }

    public boolean test_free3(int val1, int val2)
    {

        int ret = 0;
        int [] res = new int[2];

        int [][] direc = {{0, 1}, {1, 0}, {1, 1}, {-1, 1}};
        for (int i = 0 ; i < 19 ; i++)
        {
            for (int j = 0 ; j < 19 ; j++)
            {
                for (int d = 0 ; d < 4 ; d++)
                {
                ret = free_hor(i, j, direc[d][0], direc[d][1]);
                    if (ret !=0)
                        res[ret - 1]+=1;
                }
            }
        }
        if (res[0] == val1 && res[1] == val2)
            return true;
        System.out.printf("test_free3 failed %d %d founded instead of %d %d", res[0], res[1], val1, val2);
        return false;
    }

    private boolean case_in_goban (int x, int y)
    {
        return in_goban(x, y) && MinMax.map[x][y] == 0;
    }

    public void new_alignment_free_info(int res, int st, int dec1, int dec2)
    {
        if (res == 1 && st == 3)
        {
            //System.out.printf("deb x%d y%d minmax : ", x + (dec1 * dx), y + (dec1 * dy), MinMax.map[x + (dec1 * dx)][y + (dec1 * dy)]);
            //System.out.printf("deb x%d y%d minmax : ", x + (dec2 * dx), y + (dec2 * dy), MinMax.map[x + (dec2 * dx)][y + (dec2 * dy)]);
            if (in_goban(x + dec1 * dx, y + dec1 * dy) && MinMax.map[x + (dec1 * dx)][y + (dec1 * dy)] == 0)
            {
                //System.out.println("one");
                int sig = (int)Math.signum(dec1);
                if (in_goban(x + (dec1 + sig) * dx, y + (dec1 + sig) * dy) && MinMax.map[x + (dec1 + sig) * dx][y + (dec1 + sig) * dy] == opponant)
                    str[dir+4][x + (dec1 * dx)][y + (dec1 * dy)] = 3;
                else
                    str[dir+4][x + (dec1 * dx)][y + (dec1 * dy)] = 1;
            }
            else
            {
               //System.out.println("two");
                int sig = (int)Math.signum(dec2);
                //System.out.printf("x %d y %d\n", x + dec2 * dx, y + dec2 * dy);
                //System.out.printf("%d %d %d x %d y %d\n", sig, opponant, MinMax.map[x + (dec2 + sig) * dx][y + (dec2 + sig) * dy],
               //x + (dec2 + sig) * dx, y + (dec2 + sig) * dy);
                if (in_goban(x + (dec2 + sig) * dx, y + (dec2 + sig) * dy) && MinMax.map[x + (dec2 + sig) * dx][y + (dec2 + sig) * dy] == opponant)
                    str[dir+4][x + (dec2 * dx)][y + (dec2 * dy)] = 3;
                else
                    str[dir+4][x + (dec2 * dx)][y + (dec2 * dy)] = 1;
            }
            return;
        }

        if (case_in_goban(x + (dec1 * dx), y + (dec1 * dy)))
            str[dir+4][x + (dec1 * dx)][y + (dec1 * dy)] = res;
        if (case_in_goban(x + (dec2 * dx), y + (dec2 * dy)))
            str[dir+4][x + (dec2 * dx)][y + (dec2 * dy)] = res;
    }

    public void new_alignment(int dec1, int dec2, int st)
    {
        if (st == 2)
        {
           //System.out.printf("dec1 %d , dec2, %d dx %d dy %d\n", dec1, dec2, dx, dy);
            mod_capt(dec1, dec2, 1);
        }

        // if (st == 3)
        // {
        //     mod_capt(dec1, dec2, -1);
        // }


        if (case_in_goban(x + (dec1 * dx), y + (dec1 * dy)))
        {
            rep_case(x + (dec1 * dx), y + (dec1 * dy), st);
            //res++;
        }
        if (case_in_goban(x + (dec2 * dx), y + (dec2 * dy)))
        {
            rep_case(x + (dec2 * dx), y + (dec2 * dy), st);
            //res++;
        }
        //new_alignment_free_info(res, st, dec1, dec2);
    }


    public void connect(int x, int y) //directly x y
    {
        this.str = MinMax.map[x][y] == 1 ? str1 : str2;
        if (str[dir][x][y] == 0)
        {
            if (in_goban(x-dx, y-dy) && MinMax.map[x-dx][y-dy] == cur_turn)
            {
                // rep_case(x - 2 * dx, y - 2 * dy, 3);
                // rep_case(x + 2 * dx, y + 2 * dy, 3);
                new_alignment(-2, 2, 3);
                //check_free(x, y , -2, 2, 3);
            }
            else
            {
                // rep_case(x - dx, y - dy, 2);
                // rep_case(x + 2 * dx, y + 2 * dy, 2)

                new_alignment(-1, 2, 2);
            }
        }

        else if (str[dir][x][y] == 2)
        {
            //System.out.printf("I am 2 ! %d %d %d %d\n", x, y, dx, dy);
            if (in_goban(x-dx, y-dy) && MinMax.map[x-dx][y-dy] == cur_turn)
            {

                if (in_goban(x-2*dx, y-2*dy) && MinMax.map[x - 2*dx][y - 2*dy] == cur_turn)
                {
                    //System.out.println("yes");
                    // rep_case(x - 3 * dx, y - 3 * dy, 4);
                    // rep_case(x + 2 * dx, y + 2 * dy, 4);
                    new_alignment(-3, 2, 4);
                    //check_free(x, y, -3, 2, 4);
                }
                else
                {
                    //System.out.println("no");
                    // rep_case(x + 3 *dx, y + 3 * dy, 4);
                    // rep_case(x - 2 * dx, y - 2 * dy, 4);
                    new_alignment(3, -2, 4);
                    //check_free(x, y, 3, -2, 4);
                }

            }
            else
            {
                // rep_case(x - dx, y - dy, 3);
                // rep_case(x + 3 * dx, y + 3 * dy, 3);
                new_alignment(-1, 3, 3);
                //check_free(x, y, -1, 3, 3);
            }
        }

        else if (str[dir][x][y] == 3)
        {
            int decp = 0;
            int decn = 0;
            for (int i = 1 ; in_goban(x+i*dx, y+i*dy) && MinMax.map[x+i*dx][y+i*dy] == cur_turn ; i++)
                decp++;
            for (int i = 1 ; in_goban(x-i*dx, y-i*dy) && MinMax.map[x-i*dx][y-i*dy] == cur_turn ; i++)
                decn++;
            //System.out.printf("decp decn %d %d\n", decp, decn);
            if (decp + decn >= 4)
            {
                save_victory();
            }

            else
            {
                // rep_case(x - dx, y - dy, 4);
                // rep_case(x + 4 * dx, y + 4 * dy, 4);
                // System.out.printf("recp %d recn %d\n", decp, decn);
                // System.out.printf("modify %d %d and %d %d\n",x - (decn+1)*dx, y - (decn+1)*dy, 
                // x + (decp+1)*dx, y + (decp+1)*dy, 4);
                rep_case(x - (decn+1)*dx, y - (decn+1)*dy, 4);
                rep_case(x + (decp+1)*dx, y + (decp+1)*dy, 4);
                //new_alignment(-(decn+1), decp + 1, 4);
            }

            // if (decn == 2 && in_goban(x-3*dx, y-3*dy) && MinMax.map[x-3*dx][y-3*dy] == opponant)
            //     this.capt[opponant-1]-=1;
            // if (decp == 2 && in_goban(x+3*dx, y+3*dy) && MinMax.map[x+3*dx][y+3*dy] == opponant)
            //     this.capt[opponant-1] -=1;

        }

        else if (str[dir][x][y] == 4)
        {
            save_victory();
        }

        //fill(x,y);

    }

    // public void unconnect(int x, int y)
    // {
    //     int decp;
    //     int decn;

    //     this.str = cur_player == 1 ? str1 : str2; //generaliser
    
    //     decp = 0;
    //     decn = 0;

    //     for (int i = 1; in_goban(x+i*dx, y+i*dy) && MinMax.map[x + i * dx][y+ i * dy] == cur_turn ; i++)
    //         decp++;
    //     for (int i = 1; in_goban(x-i*dx, y-i*dy) && MinMax.map[x- i *dx][y - i *dy] == cur_turn ; i++)
    //         decn++;
        
        
    //     remp_case(x + (decp +1 ) * dx, y + (decp + 1) * dy, decp + 1);
    //     rem_case(x - (decn + 1) * dx, y - (decn + 1) * dy);

    //     if (decp >=2)
    //         add_case(x + (decp+1) * dx, y + (decp+1) * dy, decp);
    //     if (decn >= 2)
    //         add_case(x - (decn+1) * dx, y - (decn+1) * dy, decn);
        
    //     if (decp < 2)
    //         decp =0;
    //     if (decn <2)
    //         decn=0;

    //     add_case(x, y, decp+decn);
    // }

    public boolean is_free(int x, int y, int [][][] str, int st, int i)
    {
        int dx = (st + 1 )* ddir[i][0];
        int dy = (st + 1) * ddir[i][1];

        if (st <= 2)
            return false;
        

        if ( (in_goban(x+dx, y+dy) && str[i][x+dx][y+dy] !=0 ) ||
             (in_goban(x-dx, y-dy) && str[i][x-dx][y - dy] !=0))
            return true;
        return false;
    }

    public void fill(int x, int y)
    {
        int st;
        for (int i = 0 ; i < 4 ; i++)
        {
            st = str1[i][x][y];
            sc.one -= (factor[st]);

    
                // if (is_free(x, y, str1, st, i)) compute frees
                //     dec_frees(st, 1);
                // else
                //     dec_simp(st, 1);
    
            str1[i][x][y]=0;

            st = str2[i][x][y];
            sc.two -= (factor[st]);
    
            // if (is_free(x, y, str2, st, i)) compute frees
            //     dec_frees(st, 2);
            // else
            //     dec_simp(st, 2);
            str2[i][x][y]=0;
        }
    }

    public void fill_free_info(int i, int st, int player)
    {
        int sig = 0;
        int [][][] cur_str;
        int cur_op;
        
        if (player == 1)
        {
            cur_str = str1;
            cur_op = 2;
        }
        else
        {
            cur_str = str2;
            cur_op = 1;
        }

        if (in_goban(x + ddir[i][0], y + ddir[i][1]) && MinMax.map[x + ddir[i][0]][y + ddir[i][1]] == player)
            sig = 1;
        if (in_goban(x - ddir[i][0], y - ddir[i][1]) && MinMax.map[x - ddir[i][0]][y - ddir[i][1]] == player)
            sig = -1;


        if (cur_turn == cur_op && in_goban(x + (st + 1) * sig * ddir[i][0], y + (st + 1) * sig * ddir[i][1])
            && cur_str[i+4][x + (st + 1) * sig * ddir[i][0]][y + (st + 1) * sig * ddir[i][1]] == player) //check pos
        {
            cur_str[i+4][x + (st + 1) * sig * ddir[i][0]][y + (st + 1) * sig * ddir[i][1]] = 1;
        }
        cur_str[i+4][x][y]=0;
    }

    public boolean pos_cap(int x, int y, int dpx, int dpy,  int val)
    {
        if (!in_goban(x+3*dpx, y+3*dpy))
            return false;
        if (MinMax.map[x+3*dpx][y+3*dpy] == 0 &&  MinMax.map[x+dpx][y+dpy]== val && MinMax.map[x+2*dpx][y+2*dpy] == val)
            return true;
        return false;

    }


    public void fill2(int x, int y)
    {
        int st;
        int sig;

        for (int i = 0 ; i < 4 ; i++)
        {

            //System.out.printf("test fill dir %d, %d\n", i, str1[i][x][y]);
            if (str1[i][x][y] != 0)
            {
                //System.out.printf("filling %d %d %d\n", x, y, str1[i][x][y]);
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
        
                    //if (cur_turn == 1)
                    //{
                        if (in_goban(x+ sig * 3* ddir[i][0], y + sig * 3 * ddir[i][1]) &&
                            MinMax.map[x+ sig * 3* ddir[i][0]][y + sig * 3 * ddir[i][1]] == 2)
                        {
                            //System.out.println("DIM1");
                            capt[1]--;
                        }
                    //}
                    if (cur_turn == 2)
                    {
                        if (in_goban(x+ sig * 3* ddir[i][0], y + sig * 3 * ddir[i][1]) &&
                            MinMax.map[x+ sig * 3* ddir[i][0]][y + sig * 3 * ddir[i][1]] == 0)
                        {
                            //System.out.println("inc fill 1");
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
                //fill_free_info(i, st, 1);

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
        
                    //if (cur_turn == 2)
                    //{
                        if ( in_goban(x+ sig * 3* ddir[i][0], y + sig * 3 * ddir[i][1]) &&
                            MinMax.map[x+ sig * 3* ddir[i][0]][y + sig * 3 * ddir[i][1]] == 1)
                        {
                            //System.out.println("DIM2");
                            capt[0]--;
                        }
                    //}
                    else if (cur_turn == 1)
                    {
                        if (in_goban(x+ sig * 3* ddir[i][0], y + sig * 3 * ddir[i][1]) &&
                            MinMax.map[x+ sig * 3* ddir[i][0]][y + sig * 3 * ddir[i][1]] == 0)
                        {
                            //System.out.println("inc fill2");
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

                //fill_free_info(i, st, 2);
                str2[i][x][y]=0;
            }
            if (in_goban(x + 5 * ddir[i][0], y + 5 * ddir[i][1]))
            {
                if (MinMax.map[x + 5 * ddir[i][0]][y + 5 * ddir[i][1]] == cur_turn)
                {
                    create_blocker(i, 1);   
                }
            }
            //else if (x == 14 && i != 1 || y == 14 && i != 0 || x == 4 && i == 3)
            else if (!(x > 4 && x < 14 && y < 4 && y < 14))
            {
                //System.out.printf("create1 %d %d %d\n", x, y, i);
                create_blocker(i, 1);
            }

            if (in_goban(x - 5 * ddir[i][0], y - 5 * ddir[i][1]))
            { 
                if (MinMax.map[x - 5 * ddir[i][0]][y - 5 * ddir[i][1]] == cur_turn)
                {
                    create_blocker(i, -1);   
                }
            }
            else if (!(x > 4 && x < 14 && y < 4 && y < 14))
            //else if (x == 4 && i != 1 || y == 4 && i != 0 || x == 14 && i == 3)
            {
                //System.out.printf("create2 %d %d %d\n", x, y, i);
                create_blocker(i, -1);
            }
        }
    }

    private void create_blocker(int dir, int sig)
    {
        Blocker res = new Blocker(cur_turn, dir, sig);
        res.bl1(x, y);

        //System.out.printf("check limits %d %d\n ", x+ sig* 5*ddir[dir][0], y + sig * 5 * ddir[dir][1]);

        if (!in_goban(x+ sig* 5*ddir[dir][0], y + sig * 5 * ddir[dir][1]))
            res.bl2(-1, -1);
        else
            res.bl2(x+ 5*ddir[dir][0]*sig, y + 5 * ddir[dir][1]*sig);
        //System.out.printf("after creation %d %d %d %d\n", res.bl1[0], res.bl1[1], res.bl2[0], res.bl2[1]);
        res.update_block_info();
        this.blocklist.add(res);
        //System.out.printf("added %d\n", this.blocklist.size());
    }

    // private void search_blocker(int dir, int sig)
    // {
    //     int val = 0;
    //     int nb = 0;
    //     int xval=-1;
    //     int yval=-1;

    //     for (int i = 1 ; in_goban(x+ddir[dir][0]*sig*i, y + ddir[dir][1]*sig*i) && i < 5;i++)
    //     {
    //         //System.out.printf("x y cur opp %d %d %d %d\n", x+ddir[dir][0]*sig*i, y + ddir[dir][1]*sig*i, 
    //         //MinMax.map[x+ddir[dir][0]*sig*i][y + ddir[dir][1]*sig*i], opponant);
    
    //         if (MinMax.map[x+ddir[dir][0]*sig*i][y + ddir[dir][1]*sig*i] == opponant)
    //         {
    //             nb++;
    //         }
    //         else if (MinMax.map[x+ddir[dir][0]*sig*i][y + ddir[dir][1]*sig*i] == 0)
    //         {
    //             xval = x+ddir[dir][0]*sig*i;
    //             yval = y+ddir[dir][1]*sig*i;
    //         }
    //     }



    //     //System.out.printf("info search blockers %d %d %d\n", nb, xval, yval);

    //     if (nb == 3 && xval !=-1 && yval != -1)
    //     {
    //         if (cur_turn == 1)
    //             val = str2[dir][xval][yval];
    //         else
    //             val = str1[dir][xval][yval];

    //         Blocker res = new Blocker(val, opponant, dir, sig);
    //         res.bl1(x, y);
    //         res.bl2(x+ 5*ddir[dir][0]*sig, y + 5 * ddir[dir][1]*sig);
    //         res.val(xval, yval);

    //         this.blocklist.add(res);
    //     }
    // }

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
                // if ( ( b.bl2[0] != -1 &&
                // MinMax.map[b.bl2[0]] [b.bl2[1]] != b.blockcolor))
                    //System.out.printf("correct remove %d", i);
                this.blocklist.remove(i);
                //System.out.println("removed");
                i--;
            }
        }
    }

    private void save_victory()
    {
            if (!this.victory)
            {
                //sc.victory(cur_turn);
                this.victory = true;
                return;
            }
            //sc.unvictory(cur_turn);
            this.victory=false;
    }

    public void newupdate_free_unconnect(int decp, int decn, int x, int y)
    {
        if (decp + decn + 1 == 3)
        {
            // System.out.printf("debug %d %d %d %d %d\n", x, y, x + (decp+1) *dx,y +  (decp+1 *dy), this.str1[dir][x+(decp+1) *dx][y+(decp+1)*dy]);
            // System.out.printf("impossiburu %d\n", decn-this.str[dir][10][13]);
            if (in_goban( x + (decp+1)*dx, y + (decp+1) * dy) && this.str[dir][x+ (decp + 1 )* dx][y + (decp + 1 ) * dy] != 0 && 
                in_goban(x - (decn+1) * dx, y- (decn+1) *dy) && this.str[dir][x-(decn +1) * dx][y - (decn+1) * dy] !=0)
                dec_free(3, cur_turn);
        }
        else if (decp + decn + 1 == 4)
        {
            //System.out.printf("chek here %d %d\ncheck here %d %d\n", x+(decp+1)*dx, y+ (decp+1) * dy, x -(decn+1)*dx, y - (decn+1) *dy);
            if (in_goban(x+(decp+1)*dx, y+(decp+1)*dy) && this.str[dir][x+(decp+1)*dx][y+(decp+1)*dy] != 0)
            {
                if (in_goban(x-(decn+1)*dx, y-(decn+1)*dy) && this.str[dir][x-(decn+1)*dx][y-(decn+1)*dy] != 0)
                {
                    dec_free(4, cur_turn);
                }
                else
                {
                    dec_simp(4, cur_turn);
                }
                if ( decn == 0)
                    inc_free(3, cur_turn);
            }
            else if (in_goban(x-(decn+1)*dx, y-(decn+1)*dy) && this.str[dir][x-(decn+1)*dx][y-(decn+1)*dy] != 0)
            {
                dec_simp(4, cur_turn);
                if (decp == 0)
                    inc_free(3, cur_turn);
            }
        }
    }

    public void mod_capt(int dec1, int dec2, int val)
    {
        if (!in_goban(x+dec1*dx, y+dec1*dy) || !in_goban(x+dec2*dx, y+dec2*dy))
            return;
        // System.out.printf("op %d\n", opponant);
        // System.out.printf("idx1 %d %d\n", x + dec1 * dx, y + dec1*dy);
        // System.out.printf("idx2 %d %d\n", x + dec2 * dx, y + dec2*dy);
        //System.out.printf("check decapt %d %d\n", MinMax.map[x+dec1*dx][y+dec1*dy], MinMax.map[x+dec2*dx][y+dec2*dy]);

        if (MinMax.map[x+dec1*dx][y+dec1*dy] == opponant && MinMax.map[x+dec2*dx][y+dec2*dy] == 0)
        {
            //System.out.printf("DIMmod1 %d\n", val);
            capt[opponant - 1]+=val;
        }
        else if (MinMax.map[x+dec1*dx][y+dec1*dy] == 0 && MinMax.map[x+dec2*dx][y+dec2*dy] == opponant)
        {
            //System.out.printf("DIMmod2 %d\n", val);
            capt[opponant - 1]+=val;
        }
        
    }

    private int min4(int decx, int decxx)
    {
        if (decx == 1 && decxx == 1)
            return 0;
        if (decx + decxx <=1)
            return 0;
        return Math.min(4, decx + decxx);
    }


    public void unconnect(int x, int y)
    {

        int decp;
        int decn;
        int decpp;
        int decnn;

        //System.out.println("HEEEEERE");
       // this.str = cur_player == 1 ? str1 : str2; //generaliser

       //System.out.printf("unconnect %d %d %d %d\n", x, y, dx, dy);
    
        decp = 0;
        decn = 0;
        decpp=0;
        decnn=0;

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
            //System.out.printf("INDEED %d %d\n", decp, decn);
            if (decp == 2 && in_goban(x+3*dx, y+3*dy) && MinMax.map[x+3*dx][y+3*dy] == opponant)
                capt[opponant-1]++;
            else if (decn == 2 && in_goban(x-3*dx, y-3*dy) && MinMax.map[x-3*dx][y-3*dy] == opponant)
                capt[opponant-1]++;
        }

        else if (decp + decn + 1 == 2)
        {
            //System.out.printf("YES %d %d\n", decp, decn);
            if (decp !=0)
            {
                //System.out.printf("minus p 1 %d %d\n", dx, dy);
                mod_capt( -1, 2, -1);
            }
            else if (decn != 0)
            {
                //System.out.printf("minus n 1 %d %d\n", dx, dy);
                mod_capt(-2, 1, -1);
            }
            // if (decp != 0 && in_goban(x+2*dx, y+2*dy) && MinMax.map[x+2*dx][y+2*dy] == opponant)
            //     capt[opponant-1]--;
            // else if (decn != 0 && in_goban(x-2*dx, y-2*dy) && MinMax.map[x-2*dx][y-2*dy] == opponant)
            //     capt[opponant-1]--;
        }

        else if (decp + decn + 1 == 4)
        {
            if (decp == 2 && in_goban(x+3*dx, y+3*dy) && MinMax.map[x+3*dx][y+3*dy] == opponant)
                capt[opponant-1]++; 
            else if (decn == 2 && in_goban(x-3*dx, y-3*dy) && MinMax.map[x-3*dx][y-3*dy] == opponant)
                capt[opponant-1]++;
        }

        //System.out.printf("unconnect %d %d %d %d %d %d %d %d\n", x, y, decp, decn, decpp, decnn, dx, dy);

        if (true)
        //if (!(decp == 1 && decpp == 1))
        {
            //System.out.printf("min %d\n", min4(decp, decpp));
            //System.out.printf("add+, %d %d %d\n",x + (decp + 1) * dx, y + (decp + 1) * dy, min4(decp, decpp) );
            add_case(x + (decp + 1) * dx, y + (decp + 1) * dy, min4(decp, decpp));
        }
        //if (!(decn ==1 && decnn == 1))
        if (true)
        {
            //System.out.printf("add- %d %d %d\n", x - (decn + 1) * dx, y - (decn + 1) * dy, min4(decn, decnn));
            add_case(x - (decn + 1) * dx, y - (decn + 1) * dy, min4(decn, decnn));
        }
        // remp_case(x + (decp +1 ) * dx, y + (decp + 1) * dy, decp + 1, 1); // could be 6
        // //remp_case(x + (decp +1 ) * dx, y + (decp + 1) * dy, decp + 1, 1); // could be 6
        // //remp_case(x - (decn + 1) * dx, y - (decn + 1) * dy, decp + decn, -1);
        // rem_case(x - (decn + 1) * dx, y - (decn + 1) * dy);

        // if (decp >=2)
        //     add_case(x + (decp+1) * dx, y + (decp+1) * dy, decp); // vould be 6
        // if (decn >= 2)
        //     add_case(x - (decn+1) * dx, y - (decn+1) * dy, decn); // could be 6

        // if (decp < 2)
        //     decp =0;
        // if (decn < 2)
        //     decn=0;
        if (decp + decn == 3)
            add_case(x, y, 3);
        else if (decp == 2 || decn == 2)
            add_case(x, y, 2);
        else if (decp + decn >= 4)
            add_case(x, y, 4);

    }

    public void update_free_unfill(int x, int y, int nbp, int nbn)
    {
        //System.out.printf("X an Y %d %d\n", x, y);
        int other = cur_turn == 1 ? 2 : 1;
        int [][][] strp = cur_turn == 1 ? str2 : str1; 
        int st;
        int sig;

        if (nbp == 0)
        {
            st = nbn;
            sig = -1;
        }
        else if (nbn == 0)
        {
            st = nbp;
            sig=1;
        }
        else
            return;

        if (st == 3)
        {
            if (in_goban(x+4*sig*dx, y+4*sig*dy) && strp[dir][x+4*sig*dx][y+4*sig*dy] != 0)
            {
                inc_free(3, other);
            }
        }

        else if (st== 4)
        {
            if (in_goban(x+5*sig*dx, y+5*sig*dy) && strp[dir][x+5*sig*dx][y+5*sig*dy] != 0)
            {
                dec_simp(4, other);
                inc_free(4, other);
            }
            else
                inc_simp(4, other);
        }
    }

    public void unfill(int x, int y)
    {
        int val = cur_turn == 1 ? 2 : 1;
        int nbp = 0;
        int nbn = 0;

        //System.out.printf("simple unfill executed ?%d %d \n", x, y);


        for (int i = 1; in_goban(x+i*dx, y+i*dy) && MinMax.map[x + i * dx][y+ i * dy] == val ; i++)
            nbp++;
        for (int i = 1; in_goban(x-i*dx, y-i*dy) && MinMax.map[x- i *dx][y - i *dy] == val ; i++)
            nbn++;

        if (nbn == 0 && nbp == 0)
            return;
        
        
        //update_free_unfill(x, y, nbp, nbn); compute frees

        //System.out.printf("nbp, nbn,val %d %d %d\n", nbp, nbn, val);

        if (nbn == 2 && in_goban(x-3*dx, y-3*dy))
        {
            if (MinMax.map[x-3*dx][y-3*dy] == 0)
            {
                //System.out.println("DIM4");
                capt[cur_turn - 1]--;
            }
            if (MinMax.map[x-3*dx][y-3*dy] == cur_turn)
            {
                //System.out.println("Inc unfill");
                capt[cur_turn - 1]++;
            }
        }

        if (nbp == 2 && in_goban(x+3*dx, y+3*dy))
        {
            if (MinMax.map[x+3*dx][y+3*dy] == 0)
            {
                //System.out.println("DIM5");
                capt[cur_turn - 1]--;
            }
            if (MinMax.map[x+3*dx][y+3*dy] == cur_turn)
                capt[cur_turn - 1]++;
        }

        if (nbn < 2)
            nbn = 0;
        if (nbp < 2)
            nbp = 0;



        //System.out.printf("nbp, nbn,val %d %d %d\n", nbp, nbn, val);

        spown_case(x, y, Math.min(4, nbp + nbn));
    }

    // public void analyse_unmove(int x, int y, int turn)
    // {
    //     this.cur_turn = turn;
    //     this.str = turn == 1 ? this.str1 : this.str2;
    //     if (victory)
    //     {
    //         save_victory();
    //         return ;
    //     }
    //     if (x + 1 != 19 && is_player(MinMax.map[x+1][y]))
    //     {
    //         //System.out.println("oh no");
    //         dir = 0;
    //         dx=1;dy=0;
    //         if (MinMax.map[x+1][y] == cur_turn)
    //             unconnect(x, y);
    //         else
    //             unfill(x, y);
    //     }
    //     else if (x - 1 != -1 && is_player(MinMax.map[x-1][y]))
    //     {
    //         //System.out.println("this dir");
    //         //display();
    //         dir = 0;
    //         dx=-1; dy=0;
    //         if (MinMax.map[x-1][y] == cur_turn)
    //             unconnect(x, y);
    //         else
    //             unfill(x, y);
    //     }

    //     if (y + 1 != 19 && is_player(MinMax.map[x][y+1]))
    //     {
    //         dir = 1;
    //         dx=0;dy=1;
    //         if (MinMax.map[x][y+1] == cur_turn)
    //             unconnect(x, y);
    //         else
    //             unfill(x, y);
    //     }
    //     else if (y - 1 != -1 && is_player(MinMax.map[x][y-1]))
    //     {
    //         dir = 1;
    //         dx=0;dy=-1;
    //         if (MinMax.map[x][y-1] == cur_turn)
    //             unconnect(x, y);
    //         else
    //             unfill(x, y);
    //     }
    //     if (x + 1 != 19 && y + 1 != 19 && is_player(MinMax.map[x+1][y+1]))
    //     {
    //         dir = 2;
    //         dx=1;dy=1;
    //         if (MinMax.map[x+1][y+1] == cur_turn)
    //             unconnect(x, y);
    //         else
    //             unfill(x, y);
    //     }
    //     else if (x - 1 != -1 && y - 1 != -1 && is_player(MinMax.map[x-1][y-1]))
    //     {
    //         dir = 2;
    //         dx=-1;dy=-1;
    //         if (MinMax.map[x-1][y-1] == cur_turn)
    //             unconnect(x, y);
    //         else
    //             unfill(x, y);
    //     }
    //     if (x + 1 != 19 && y - 1 != -1 && is_player(MinMax.map[x+1][y-1]))
    //     {
    //         dir = 3;
    //         dx=1;dy=-1;
    //         if (MinMax.map[x+1][y-1] == cur_turn)
    //             unconnect(x, y);
    //         else
    //             unfill(x, y);
    //     }
    //     else if (x - 1 != -1 && y + 1 != 19 && is_player(MinMax.map[x-1][y+1]))
    //     {
    //         dir = 3;
    //         dx=-1;dy=1;
    //         if (MinMax.map[x-1][y+1] == cur_turn)
    //             unconnect(x, y);
    //         else        
    //             unfill(x, y);
    //     }
    // }

    // public void analyse_unmove(int x, int y, int turn) //Maybe wrong modif
    // {
    //     //System.out.printf("unmove %d %d\n", x, y);
    //     this.cur_turn = turn;
    //     this.str = turn == 1 ? this.str1 : this.str2;
    //     if (victory)
    //     {
    //         save_victory();
    //         return ;
    //     }
    //     if (x + 1 != 19 && MinMax.map[x+1][y] == cur_turn)
    //     {
    //         //System.out.println("oh no");
    //         dir = 0;
    //         dx=1;dy=0;
    //         unconnect(x, y);
    //         unfill(x, y);
    //     }
    //     else if (x - 1 != -1 && MinMax.map[x-1][y] == cur_turn)
    //     {
    //         //System.out.println("this dir");
    //         //display();
    //         dir = 0;
    //         dx=-1; dy=0;
    //         unconnect(x, y);
    //         unfill(x, y);
    //     }

    //     if (y + 1 != 19 && MinMax.map[x][y+1] == cur_turn)
    //     {
    //         dir = 1;
    //         dx=0;dy=1;
    //         unconnect(x, y);
    //         unfill(x, y);
    //     }
    //     else if (y - 1 != -1 && MinMax.map[x][y-1] == cur_turn)
    //     {
    //         dir = 1;
    //         dx=0;dy=-1;
    //         unconnect(x, y);
    //         unfill(x, y);
    //     }
    //     if (x + 1 != 19 && y + 1 != 19 && MinMax.map[x+1][y+1] == cur_turn)
    //     {
    //         dir = 2;
    //         dx=1;dy=1;
    //         unconnect(x, y);
    //         unfill(x, y);
    //     }
    //     else if (x - 1 != -1 && y - 1 != -1 && MinMax.map[x-1][y-1] == cur_turn)
    //     {
    //         dir = 2;
    //         dx=-1;dy=-1;
    //         unconnect(x, y);
    //         unfill(x, y);
    //     }
    //     if (x + 1 != 19 && y - 1 != -1 && MinMax.map[x+1][y-1] == cur_turn)
    //     {
    //         dir = 3;
    //         dx=1;dy=-1;
    //         unconnect(x, y);
    //         unfill(x, y);
    //     }
    //     else if (x - 1 != -1 && y + 1 != 19 && MinMax.map[x-1][y+1] == cur_turn)
    //     {
    //         dir = 3;
    //         dx=-1;dy=1;
    //         unconnect(x, y);
    //         unfill(x, y);
    //     }

    // }

    public void analyse_unmove(int x, int y, int turn)
    {
        //System.out.printf("unmove %d %d of %d (%d %d) \n", x, y, turn, this.capt[0], this.capt[1]);
        // if (x == 11 && y == 7)
        // {
        //         System.out.println("info unfmove 11 7");
        //         MinMax.display_Map();
        //         display();
        // }
        this.cur_turn = turn;
        this.str = turn == 1 ? this.str1 : this.str2;
        this.opponant = turn == 1 ? 2 : 1;
        this.x=x;
        this.y=y;



        // if (victory)
        // {
        //     save_victory();
        //     return ;
        // }

        if ((x + 1 != 19 && is_player(MinMax.map[x+1][y])) || (x - 1 != -1 && is_player(MinMax.map[x-1][y])))
        {
            dir = 0;
            dx = 1; dy = 0;
            if (x + 1 != 19 && MinMax.map[x+1][y] == cur_turn)
            {
                unconnect(x, y);
            }

            else if (x - 1 != -1 && MinMax.map[x-1][y] == cur_turn)
            {
                dx=-1;
                unconnect(x, y);
            }

            unfill(x, y);
        }
        // else
        //     unfill0(x, y, 0);

        if ((y + 1 != 19 && is_player(MinMax.map[x][y+1])) || (y - 1 != -1 && is_player(MinMax.map[x][y-1])))
        {
            dir = 1;
            dx = 0; dy = 1;
            if (y + 1 != 19 && MinMax.map[x][y+1] == cur_turn)
            {
                unconnect(x, y);
            }

            else if (y - 1 != -1 && MinMax.map[x][y-1] == cur_turn)
            {
                dy=-1;
                unconnect(x, y);
            }

            unfill(x, y);
        }
        // else
        //     unfill0(x, y, 1);

        if ((x + 1 != 19 && y + 1 != 19 && is_player(MinMax.map[x+1][y+1])) ||( x - 1 != -1 && y - 1 != -1 && is_player(MinMax.map[x-1][y-1])))
        {
            dir = 2;
            dx = 1; dy = 1;
            if (x + 1 != 19 && y + 1 != 19 && MinMax.map[x+1][y+1] == cur_turn)
            {
                unconnect(x, y);
            }

            else if ( x - 1 != -1 && y - 1 != -1 && MinMax.map[x-1][y-1] == cur_turn)
            {
                dx=-1; dy=-1;
                unconnect(x, y);
            }

            unfill(x, y);
        }
        // else
        //     unfill0(x, y, 2);

        if (x + 1 != 19 && y - 1 != -1 && is_player(MinMax.map[x+1][y-1]) || (x - 1 != -1 && y + 1 != 19 && is_player(MinMax.map[x-1][y+1])))
        {
            //System.out.println("diagneg1 detected");
            dir = 3;
            dx = 1; dy = -1;
            if (x + 1 != 19 && y - 1 != -1 && MinMax.map[x+1][y-1] == cur_turn)
            {
                unconnect(x, y);  
            }

            else if (x - 1 != -1 && y + 1 != 19 && MinMax.map[x-1][y+1] == cur_turn)
            {
                dx = -1; dy=1;
                unconnect(x, y);
            }
            
            unfill(x, y);
        }
        for (int i = 0; i < 4 ; i++)
        {
            unfill0(x, y, i);
        }

        update_blocker();
        // else
        //     unfill0(x, y, 3);
    }
            //     if (MinMax.map[x][y+1] == 0)
            // {
            //     if (cur_turn == 1 && str1[1][x][y+1] > 2 || cur_turn == 2 && str2[1][x][y+1] > 2)
            //     {
            //         cmp = 0;
            //         for (int i = 2 ; in_goban(x, y+i) && MinMax.map[x][y+i] == cur_turn ; i++)
            //             cmp++;
            //         str[1][x][y+1]=cmp;
            //     }
            // }

    public void unfill0(int x, int y, int dir)
    {
        int cmp;
        //System.out.printf("executed %d %d\n", cur_turn, dir);
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
                    //System.out.println("one");
                    sc.one -= factor[str1[dir][x+ddir[dir][0]][y+ddir[dir][1]]];
                    str1[dir][x+ddir[dir][0]][y+ddir[dir][1]]=cmp;
                    sc.one += factor[cmp];
                }
                else
                {
                    //System.out.println("two");
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
                    //System.out.println("three");
                    sc.one -= factor[str1[dir][x-ddir[dir][0]][y-ddir[dir][1]]];
                    str1[dir][x-ddir[dir][0]][y-ddir[dir][1]]=cmp;
                    sc.one += factor[cmp];
                }
                else
                {
                    //System.out.println("four");
                    sc.two -= factor[str2[dir][x-ddir[dir][0]][y-ddir[dir][1]]];
                    str2[dir][x-ddir[dir][0]][y-ddir[dir][1]]=cmp;
                    sc.two += factor[cmp];
                }
            }
        }
    }

    public void analyse_move(int x, int y, int turn) //adding isplayer==cur turn to save time
    {
        this.cur_turn = turn;
        this.opponant = turn == 1 ? 2 : 1;
        this.str = turn == 1 ? str1 : str2;
        this.x = x;
        this.y = y;

        //System.out.printf("move %d %d of %d (%d %d)\n", x, y, turn, this.capt[0], this.capt[1]);

        if (x + 1 != 19 && MinMax.map[x+1][y] == cur_turn)
        {
            //System.out.println("vertical pos detected");
            dir=0;
            dx=1;dy=0;
            connect(x, y);

        }
        else if (x - 1 != -1 && MinMax.map[x-1][y] == cur_turn)
        {
            //System.out.println("vertical neg detected");
            //display();
            dir=0;
            dx=-1; dy=0;
            connect(x, y);
        }

        if (y + 1 != 19 && MinMax.map[x][y+1] == cur_turn)
        {
            dir=1;
            dx=0;dy=1;
            connect(x, y);
        }
        else if (y - 1 != -1 && MinMax.map[x][y-1] == cur_turn)
        {
            dir = 1;
            dx=0;dy=-1;
            connect(x, y);
        }

        if (x + 1 != 19 && y + 1 != 19 && MinMax.map[x+1][y+1] == cur_turn)
        {
            dir = 2;
            dx=1;dy=1;
            connect(x, y);
        }
        else if (x - 1 != -1 && y - 1 != -1 && MinMax.map[x-1][y-1] == cur_turn)
        {
            dir = 2;
            dx=-1;dy=-1;
            connect(x, y);
        }

        if (x + 1 != 19 && y - 1 != -1 && MinMax.map[x+1][y-1] == cur_turn)
        {
            dir = 3;
            dx=1;dy=-1;
            connect(x, y);
        }
        else if (x - 1 != -1 && y + 1 != 19 && MinMax.map[x-1][y+1] == cur_turn)
        {
            dir = 3;
            dx=-1;dy=1;
            connect(x, y);
        }
        //if (!victory)
        fill2(x, y);
        update_blocker();
    }

    public void display_free()
    {
        for (int i = 0 ; i < 2 ; i++)
        {
            System.out.printf("player %d free4 %d, simp4 %d, free3 %d\n", i + 1, free4[i], simp4[i], free3[i]);
        }
        // if (free3[0] >=3 || free3[1] >=3)
        //     System.exit(0);
    }

    public boolean no_free()
    {
        for (int i = 0 ; i < 2 ; i++)
        {
            if (this.free3[i] != 0 || this.free4[i] != 0 || this.simp4[i] != 0)
                return false;
        }
        return true;
    }

    public void display_miniscore()
    {
        int res = sc.one - sc.two + capt[0]  * 10 - capt[1] * 10;
        System.out.printf("score %d %d diff %d (%d %d) [%d]\nbpoint %d %d\n", sc.one, sc.two, sc.one-sc.two, capt[0], capt[1], res, bpoint[0], bpoint[1]);
    }

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


    public void display()
    {
        //System.out.println("player1");
        display_str(1);
        //System.out.println("player2");
        display_str(2);
        display_blockers();
        display_miniscore();
        //display_free();
    }

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

    // public void display(int mod)
    // {
    //     System.out.println("DISPLAYED");
    //     if (mod > 0)
    //     {
    //         display_str(1);
    //         display_str(2);
    //     }
    //     display_miniscore();
    //     display_blockers();
    //     if (mod == -1 || mod == 2)
    //     {
    //         if (check_capt())
    //             System.exit(0);
    //     }
    // }

    void first_capt(int len, int x, int y)
    {
        //System.out.printf("Entering %d %d %d", len, x, y);
        if (len == 1)
        {
            System.out.printf("Checking fist cap %d %d\n", x, y);
            if (iscapt(x, y) != 0)
            {   
                System.out.println("it is");
                lastcap = true;
            }
            else
            {
                System.out.println("it is not");
                lastcap = false;
            }
        }
    }

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
        //System.out.printf("found %d %d correct\n", capt1, capt2);

        return false;
    }
}