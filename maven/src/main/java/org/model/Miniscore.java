package org.model;
import org.utils.*;

public class Miniscore {

    Score sc;
    int [][][] str;
    int [][][] str1;
    int [][][] str2;
    int cur_player;
    int cur_turn;
    int dx;
    int dy;
    int dir;
    boolean victory;

    static int [] factor = {0, 0, 2, 5, 10, 100};
    //static int [] factor = {0, 0, 2, 3, 4};

    public Miniscore ()
    {
        this.sc = new Score();
        this.str1 = new int[4][19][19];
        this.str2 = new int[4][19][19];
        this.victory = false;
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

    

    public void connect(int x, int y)
    {
        this.str = MinMax.map[x][y] == 1 ? str1 : str2;

        if (str[dir][x][y] == 0)
        {
            if (in_goban(x-dx, y-dy) && MinMax.map[x-dx][y-dy] == cur_turn)
            {
                rep_case(x - 2 * dx, y - 2 * dy, 3);
                rep_case(x + 2 * dx, y + 2 * dy, 3);
            }
            else
            {
                rep_case(x - dx, y - dy, 2);
                rep_case(x + 2 * dx, y + 2 * dy, 2);
            }
        }

        if (str[dir][x][y] == 2)
        {
            //System.out.println("I am 2 !");
            if (in_goban(x-dx, y-dy) && MinMax.map[x-dx][y-dy] == cur_turn)
            {
                if (in_goban(x-3*dx, y-3*dy) && str[dir][x - 3*dx][y - 3*dy] == 2)
                {
                    rep_case(x - 3 * dx, y - 3 * dy, 4);
                    rep_case(x + 2 * dx, y + 2 * dy, 4);
                }
                else
                {
                    rep_case(x + 3 *dx, y + 3 * dy, 4);
                    rep_case(x - 2 * dx, y - 2 * dy, 4);
                }

            }
            else
            {
                rep_case(x - dx, y - dy, 3);
                rep_case(x + 3 * dx, y + 3 * dy, 3);
            }

        }

        if (str[dir][x][y] == 3)
        {

            if (in_goban(x-dx, y-dy) && MinMax.map[x-dx][y-dy] == cur_turn)
            {
                save_victory();
            }
            else
            {
                rep_case(x - dx, y - dy, 4);
                rep_case(x + 4 * dx, y + 4 * dy, 4);
            }

        }

        if (str[dir][x][y] == 4)
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


    public void fill(int x, int y)
    {
        for (int i = 0 ; i < 4 ; i++)
        {
            sc.one -= (factor[str1[i][x][y]]);
            sc.two -= (factor[str2[i][x][y]]);        
            str1[i][x][y]=0;
            str2[i][x][y]=0;
        }
    }

    private void save_victory()
    {
            if (!this.victory)
            {
                sc.victory(cur_turn);
                this.victory = true;
                return;
            }
            sc.unvictory(cur_turn);
            this.victory=false;
    }


    public void unconnect(int x, int y)
    {
        int decp;
        int decn;

        //System.out.println("HEEEEERE");

        //this.str = cur_player == 1 ? str1 : str2; //generaliser
        
    
        decp = 0;
        decn = 0;

        for (int i = 1; in_goban(x+i*dx, y+i*dy) && MinMax.map[x + i * dx][y+ i * dy] == cur_turn ; i++)
            decp++;
        for (int i = 1; in_goban(x-i*dx, y-i*dy) && MinMax.map[x- i *dx][y - i *dy] == cur_turn ; i++)
            decn++;
        //System.out.printf(" %d %d\n", decp, decn);
        // if (str2[dir][x+ (decp + 1 ) * dx][y + (decp + 1) * dy] == 0)
        // {
        //     System.out.printf("Info %d %d %d", x, y, dir);
        //     MinMax.display_Map();
        //     display();
        // }
        
        remp_case(x + (decp +1 ) * dx, y + (decp + 1) * dy, decp + 1);
        rem_case(x - (decn + 1) * dx, y - (decn + 1) * dy);

        if (decp >=2)
            add_case(x + (decp+1) * dx, y + (decp+1) * dy, decp);
        if (decn >= 2)
            add_case(x - (decn+1) * dx, y - (decn+1) * dy, decn);
        
        if (decp < 2)
            decp =0;
        if (decn <2)
            decn=0;

        add_case(x, y, Math.min(decp+decn, 4));
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

        //System.out.printf("nbp, nbn,val %d %d %d\n", nbp, nbn, val);

        if (nbn < 2)
            nbn = 0;
        if (nbp < 2)
            nbp = 0;

        //System.out.printf("nbp, nbn,val %d %d %d\n", nbp, nbn, val);

        spown_case(x, y, Math.min(4, nbp + nbn));
    }



    public void analyse_unmove(int x, int y, int turn)
    {
        this.cur_turn = turn;
        //this.str = turn == 1 ? str1 : str2;
        if (victory)
            save_victory();
        if (x + 1 != 19 && is_player(MinMax.map[x+1][y]))
        {
            //System.out.println("oh no");
            dir = 0;
            dx=1;dy=0;
            if (MinMax.map[x+1][y] == cur_turn)
                unconnect(x, y);
            else 
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
            else
                unfill(x, y);
        }

        if (y + 1 != 19 && is_player(MinMax.map[x][y+1]))
        {
            dir = 1;
            dx=0;dy=1;
            if (MinMax.map[x][y+1] == cur_turn)
                unconnect(x, y);
            else
                unfill(x, y);
        }
        else if (y - 1 != -1 && is_player(MinMax.map[x][y-1]))
        {
            dir = 1;
            dx=0;dy=-1;
            if (MinMax.map[x][y-1] == cur_turn)
                unconnect(x, y);
            else
                unfill(x, y);
        }
        if (x + 1 != 19 && y + 1 != 19 && is_player(MinMax.map[x+1][y+1]))
        {
            dir = 2;
            dx=1;dy=1;
            if (MinMax.map[x+1][y+1] == cur_turn)
                unconnect(x, y);
            else
                unfill(x, y);
        }
        else if (x - 1 != -1 && y - 1 != -1 && is_player(MinMax.map[x-1][y-1]))
        {
            dir = 2;
            dx=-1;dy=-1;
            if (MinMax.map[x-1][y-1] == cur_turn)
                unconnect(x, y);
            else
                unfill(x, y);
        }
        if (x + 1 != 19 && y - 1 != -1 && is_player(MinMax.map[x+1][y-1]))
        {
            dir = 3;
            dx=1;dy=-1;
            if (MinMax.map[x+1][y-1] == cur_turn)
                unconnect(x, y);
            else
                unfill(x, y);
        }
        else if (x - 1 != -1 && y + 1 != 19 && is_player(MinMax.map[x-1][y+1]))
        {
            dir = 3;
            dx=-1;dy=1;
            if (MinMax.map[x-1][y+1] == cur_turn)
                unconnect(x, y);
            else
                unfill(x, y);
        }

        //unfill(x, y);
    }

    public void analyse_move(int x, int y, int turn) //adding isplayer==cur turn to save time
    {
        this.cur_turn = turn;
       // this.str = turn == 1 ? str1 : str2;

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

        fill(x, y);
    }

    public void display()
    {
        //System.out.println("player1");
        display_str(1);
        //System.out.println("player2");
        display_str(2);
        System.out.printf("score %d %d diff %d\n", sc.one, sc.two, sc.one-sc.two);
    }

}
