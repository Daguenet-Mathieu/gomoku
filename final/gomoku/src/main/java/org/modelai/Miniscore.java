package org.modelai;
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

    boolean victory;

    //static int [] factor = {0, 0, 2, 5, 10, 10};
    static int [] factor = {0, 0, 2, 10, 25, 0, 0};
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

    private boolean in_goban(int x, int y) //move utils
    {
        if (x >=0 && x < 19 && y >=0 && y < 19)
            return true;
        return false;
    }

    private boolean is_player(int c) //move utils
    {
        if (c == 1 || c == 2)
            return true;
        return false;
    }

    private void remp_case(int x, int y, int val)
    {
        //System.out.printf("removing case at %d %d val %d\n", x, y, val);
        if (x >=0 && x < 19 && y >=0 && y <19)
        {
            if (cur_turn == 1)
            {                
                sc.one -= factor[str1[dir][x][y]];
                if (in_goban(x+dx, y+dy) && MinMax.map[x+dx][y+dy] == cur_turn && MinMax.map[x][y] == 0)
                {
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
            }
            else
            {
                //System.out.println("HEEEERE2");
                sc.two -= factor[str2[dir][x][y]];
                if (in_goban(x+dx, y+dy) && MinMax.map[x+dx][y+dy] == cur_turn && MinMax.map[x][y] == 0)
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

    private void rem_case(int x, int y)
    {
        //System.out.printf("removing case at %d %d\n", x, y);
        if (x >=0 && x < 19 && y >=0 && y <19)
        {
            if (cur_turn == 1)
            {
                sc.one -= factor[str1[dir][x][y]];
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
        //System.out.printf("adding case at %d %d of %d\n", x, y, st);
        if (x >= 0 && x < 19 && y >= 0 && y < 19 && MinMax.map[x][y] == 0)
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

    public void new_alignment(int dec1, int dec2, int st)
    {
        int res = 0;
        if (case_in_goban(x + (dec1 * dx), y + (dec1 * dy)))
        {
            rep_case(x + (dec1 * dx), y + (dec1 * dy), st);
            res++;
        }
        if (case_in_goban(x + (dec2 * dx), y + (dec2 * dy)))
        {
            rep_case(x + (dec2 * dx), y + (dec2 * dy), st);
            res++;
        }

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
                // rep_case(x + 2 * dx, y + 2 * dy, 2);
                new_alignment(-1, 2, 2);
            }
        }

        else if (str[dir][x][y] == 2)
        {
            //System.out.println("I am 2 !");
            if (in_goban(x-dx, y-dy) && MinMax.map[x-dx][y-dy] == cur_turn)
            {
                if (in_goban(x-3*dx, y-3*dy) && str[dir][x - 3*dx][y - 3*dy] == 2)
                {
                    // rep_case(x - 3 * dx, y - 3 * dy, 4);
                    // rep_case(x + 2 * dx, y + 2 * dy, 4);
                    new_alignment(-3, 2, 4);
                    //check_free(x, y, -3, 2, 4);
                }
                else
                {
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

            if (in_goban(x-dx, y-dy) && MinMax.map[x-dx][y-dy] == cur_turn)
            {
                save_victory();
            }
            else
            {
                // rep_case(x - dx, y - dy, 4);
                // rep_case(x + 4 * dx, y + 4 * dy, 4);
                new_alignment(-1, 4, 4);
            }

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

    public void fill2(int x, int y)
    {
        int st;
        int sig = 0;
        for (int i = 0 ; i < 4 ; i++)
        {
            if (str1[i][x][y] != 0)
            {
                //System.out.printf("filling %d %d\n", x, y);
                st = str1[i][x][y];
                sc.one -= (factor[st]);

                if (in_goban(x + ddir[i][0], y + ddir[i][1]) && MinMax.map[x + ddir[i][0]][y + ddir[i][1]] == 1)
                    sig = 1;
                if (in_goban(x - ddir[i][0], y - ddir[i][1]) && MinMax.map[x - ddir[i][0]][y - ddir[i][1]] == 1)
                    sig = -1;

                //System.out.printf("testing %d %d : %d\n", x + (st + 1) * sig * ddir[i][0], y + (st + 1) * sig * ddir[i][1], str1[i+4][x + (st + 1) * sig * ddir[i][0]][y + (st + 1) * sig * ddir[i][1]]);

                if (cur_turn == 2 && in_goban(x + (st + 1) * sig * ddir[i][0], y + (st + 1) * sig * ddir[i][1])
                && str1[i+4][x + (st + 1) * sig * ddir[i][0]][y + (st + 1) * sig * ddir[i][1]] == 2) //check pos
                {
                    str1[i+4][x + (st + 1) * sig * ddir[i][0]][y + (st + 1) * sig * ddir[i][1]] = 1;
                }
                
                str1[i][x][y]=0;
                str1[i+4][x][y]=0;
            }

            if (str2[i][x][y] != 0)
            {
                st = str2[i][x][y];
                sc.two -= (factor[st]);

                if (in_goban(x + ddir[i][0], y + ddir[i][1]) && MinMax.map[x + ddir[i][0]][y + ddir[i][1]] == 2)
                    sig = 1;
                if (in_goban(x - ddir[i][0], y - ddir[i][1]) && MinMax.map[x - ddir[i][0]][y - ddir[i][1]] == 2)
                    sig = -1;

                if (cur_turn == 1 && in_goban(x + (st + 1) * sig * ddir[i][0], y + (st + 1) * sig * ddir[i][1]) && 
                    str2[i+4][x + (st + 1) * sig * ddir[i][0]][y + (st + 1) * sig * ddir[i][1]] == 2)
                {
                    str2[i+4][x + (st + 1) * sig * ddir[i][0]][y + (st + 1) * sig * ddir[i][1]] = 1;
                }
        
                str2[i][x][y]=0;
                str2[i+4][x][y]=0;
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


    public void unconnect(int x, int y)
    {
        int decp;
        int decn;

        //System.out.println("HEEEEERE");

       // this.str = cur_player == 1 ? str1 : str2; //generaliser
        
    
        decp = 0;
        decn = 0;

        for (int i = 1; in_goban(x+i*dx, y+i*dy) && MinMax.map[x + i * dx][y+ i * dy] == cur_turn ; i++)
            decp++;
        for (int i = 1; in_goban(x-i*dx, y-i*dy) && MinMax.map[x- i *dx][y - i *dy] == cur_turn ; i++)
            decn++;
        //System.out.printf("unconnect %d %d %d %d %d %d\n", x, y, dx, dy, decp, decn);
        // if (str2[dir][x+ (decp + 1 ) * dx][y + (decp + 1) * dy] == 0)
        // {
        //     System.out.printf("Info %d %d %d", x, y, dir);
        //     MinMax.display_Map();
        //     display();
        // }
        // if (decn == 0)
        //     update_free_unconnect(decp + 1, x, y);
        // if (decp == 0)
        //     update_free_unconnect(decn + 1, x, y);
        //System.out.printf("decp, decn %d %d", decp, decn);

        //newupdate_free_unconnect(decp, decn, x, y); compute frees
        
        remp_case(x + (decp +1 ) * dx, y + (decp + 1) * dy, decp + 1); // could be 6
        rem_case(x - (decn + 1) * dx, y - (decn + 1) * dy);

        if (decp >=2)
            add_case(x + (decp+1) * dx, y + (decp+1) * dy, decp); // vould be 6
        if (decn >= 2)
            add_case(x - (decn+1) * dx, y - (decn+1) * dy, decn); // could be 6


        
        if (decp < 2)
            decp =0;
        if (decn <2)
            decn=0;

        add_case(x, y, Math.min(decp+decn, 4));
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


        for (int i = 1; in_goban(x+i*dx, y+i*dy) && MinMax.map[x + i * dx][y+ i * dy] == val ; i++)
            nbp++;
        for (int i = 1; in_goban(x-i*dx, y-i*dy) && MinMax.map[x- i *dx][y - i *dy] == val ; i++)
            nbn++;

        if (nbn == 0 && nbp == 0)
            return;
        
        
        //update_free_unfill(x, y, nbp, nbn); compute frees

        //System.out.printf("nbp, nbn,val %d %d %d\n", nbp, nbn, val);

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

    public void analyse_unmove(int x, int y, int turn)
    {
        //System.out.printf("unmove %d %d\n", x, y);
        this.cur_turn = turn;
        this.str = turn == 1 ? this.str1 : this.str2;
        if (victory)
        {
            save_victory();
            return ;
        }
        if (x + 1 != 19 && is_player(MinMax.map[x+1][y]))
        {
            //System.out.println("oh no");
            dir = 0;
            dx=1;dy=0;
            if (MinMax.map[x+1][y] == cur_turn)
                unconnect(x, y);
            unfill(x, y);
        }
        else if (x - 1 != -1 && is_player(MinMax.map[x-1][y]))
        {
            //System.out.println("this dir");
            //display();
            dir = 0;
            dx=-1; dy=0;
            if (MinMax.map[x-1][y] == cur_turn)
                unconnect(x, y);
            unfill(x, y);
        }

        if (y + 1 != 19 && is_player(MinMax.map[x][y+1]))
        {
            dir = 1;
            dx=0;dy=1;
            if (MinMax.map[x][y+1] == cur_turn)
                unconnect(x, y);
            unfill(x, y);
        }
        else if (y - 1 != -1 && is_player(MinMax.map[x][y-1]))
        {
            dir = 1;
            dx=0;dy=-1;
            if (MinMax.map[x][y-1] == cur_turn)
                unconnect(x, y);
            unfill(x, y);
        }
        if (x + 1 != 19 && y + 1 != 19 && is_player(MinMax.map[x+1][y+1]))
        {
            dir = 2;
            dx=1;dy=1;
            if (MinMax.map[x+1][y+1] == cur_turn)
                unconnect(x, y);
            unfill(x, y);
        }
        else if (x - 1 != -1 && y - 1 != -1 && is_player(MinMax.map[x-1][y-1]))
        {
            dir = 2;
            dx=-1;dy=-1;
            if (MinMax.map[x-1][y-1] == cur_turn)
                unconnect(x, y);
            unfill(x, y);
        }
        if (x + 1 != 19 && y - 1 != -1 && is_player(MinMax.map[x+1][y-1]))
        {
            dir = 3;
            dx=1;dy=-1;
            if (MinMax.map[x+1][y-1] == cur_turn)
                unconnect(x, y);
            unfill(x, y);
        }
        else if (x - 1 != -1 && y + 1 != 19 && is_player(MinMax.map[x-1][y+1]))
        {
            dir = 3;
            dx=-1;dy=1;
            if (MinMax.map[x-1][y+1] == cur_turn)
                unconnect(x, y);
            unfill(x, y);
        }

    }

    public void analyse_move(int x, int y, int turn) //adding isplayer==cur turn to save time
    {
        this.cur_turn = turn;
        this.opponant = turn == 1 ? 2 : 1;
        this.str = turn == 1 ? str1 : str2;
        this.x = x;
        this.y = y;

        if (x + 1 != 19 && is_player(MinMax.map[x+1][y]))
        {
            //System.out.println("oh no");
            dir=0;
            dx=1;dy=0;
            if (MinMax.map[x+1][y] == cur_turn)
                connect(x, y);

        }
        else if (x - 1 != -1 && is_player(MinMax.map[x-1][y]))
        {
            //System.out.println("this dir");
            //display();
            dir=0;
            dx=-1; dy=0;
            if (MinMax.map[x-1][y] == cur_turn)
                connect(x, y);
        }

        if (y + 1 != 19 && is_player(MinMax.map[x][y+1]))
        {
            dir=1;
            dx=0;dy=1;
            if (MinMax.map[x][y+1] == cur_turn)
                connect(x, y);
        }
        else if (y - 1 != -1 && is_player(MinMax.map[x][y-1]))
        {
            dir = 1;
            dx=0;dy=-1;
            if (MinMax.map[x][y-1] == cur_turn)
                connect(x, y);
        }

        if (x + 1 != 19 && y + 1 != 19 && is_player(MinMax.map[x+1][y+1]))
        {
            dir = 2;
            dx=1;dy=1;
            if (MinMax.map[x+1][y+1] == cur_turn)
                connect(x, y);
        }
        else if (x - 1 != -1 && y - 1 != -1 && is_player(MinMax.map[x-1][y-1]))
        {
            dir = 2;
            dx=-1;dy=-1;
            if (MinMax.map[x-1][y-1] == cur_turn)
                connect(x, y);
        }

        if (x + 1 != 19 && y - 1 != -1 && is_player(MinMax.map[x+1][y-1]))
        {
            dir = 3;
            dx=1;dy=-1;
            if (MinMax.map[x+1][y-1] == cur_turn)
                connect(x, y);
        }
        else if (x - 1 != -1 && y + 1 != 19 && is_player(MinMax.map[x-1][y+1]))
        {
            dir = 3;
            dx=-1;dy=1;
            if (MinMax.map[x-1][y+1] == cur_turn)
                connect(x, y);
        }
        //if (!victory)
        fill2(x, y);
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
        System.out.printf("score %d %d diff %d\n", sc.one, sc.two, sc.one-sc.two);
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
            return false;
        return true;
    }


    public void display()
    {
        //System.out.println("player1");
        display_str(1);
        //System.out.println("player2");
        display_str(2);
        display_miniscore();
        //display_free();
    }

}