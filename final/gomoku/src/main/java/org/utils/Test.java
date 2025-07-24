package org.utils;
//import java.util.ArrayList;
//To be removed
import org.modelai.*;

public class Test {
    MinMax m;

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

    //static int [] factor = {0, 0, 2, 5, 10};
    static int [] factor = {0, 0, 2, 3, 4};

    public Test()
    {
        this.m = new MinMax();
        this.sc = new Score();
        this.str1 = new int[4][19][19];
        this.str2 = new int[4][19][19];
        this.victory = false;
    }

    // public Test(Test t)
    // {
    //     this.str1 = new int[4][19][19];
    //     this.str2 = new int[4][19][19];
    //     this.sc = new Score();

    //     for (int i = 0 ; i < 19 ; i++)
    //     {
    //         for (int j = 0 ; j < 19 ; j++)
    //         {
    //             this.str1[i][j] = t.str1[i][j];
    //             this.str2[i][j] = t.str2[i][j]; 
    //         }
    //     }
    //     this.sc.one = t.sc.one;
    //     this.sc.two = t.sc.two;
    // }

    // private ArrayList<Candidat.coord> alone_blanks(int x, int y)
    // {
    //     ArrayList<Candidat.coord> res = new ArrayList<Candidat.coord>();

    //     if (x + 1 != 19)
    //         res.add(new Candidat.coord(x + 1, y));
    //     if (x - 1 != -1)
    //         res.add(new Candidat.coord(x - 1, y));
    //     if (y + 1 != 19)
    //         res.add(new Candidat.coord(x, y + 1));
    //     if (y - 1 != -1)
    //         res.add(new Candidat.coord(x, y - 1));
    //     if (x + 1 != 19 && y + 1 != 19)
    //         res.add(new Candidat.coord(x + 1, y + 1));
    //     if (x + 1 != 19 && y-1 != -1)
    //         res.add(new Candidat.coord(x + 1, y-1));
    //     if (x - 1 != -1 && y +1 != 19)
    //         res.add(new Candidat.coord(x - 1, y + 1));
    //     if (x - 1 != -1 && y - 1 != -1)
    //         res.add(new Candidat.coord(x - 1, y - 1));

    //     return res;
    // }

    public void display_str()
    {
        String dir [] = {"VERTICAL","HORIZONTAL", "DIAGPOS", "DIAGNEG"};

        for (int d = 0 ; d < 4 ; d++)
        {
            System.out.printf("direction %s\n", dir[d]);
            for (int i  = 0 ; i < 19 ; i++)
            {
                for (int j = 0 ; j < 19 ; j++)
                {
                    System.out.printf("%2d", str1[d][i][j]);
                }
                System.out.println();
            }
        }
    }

    private boolean in_goban(int x, int y)
    {
        if (x >=0 && x < 19 && y >=0 && y < 19)
            return true;
        return false;
    }


    // public void add_case(int x,int y, int st)
    // {
    //     if (x >= 0 && x < 19 && y >= 0 && y < 19)
    //     {
    //             if (MinMax.map[x][y] == 0)
    //             {
    //                 if (cur_turn == 1)
    //                 {
    //                     sc.one -= factor[str1[x][y]];
    //                     str1[x][y] = Math.min(4, str1[x][y] + st);
    //                     sc.one += factor[str1[x][y]];
    //                 }
    //                 else
    //                 {
    //                     sc.two -= factor[str2[x][y]];
    //                     str2[x][y] = Math.min(4, str2[x][y] + st);
    //                     sc.two += factor[str2[x][y]];
    //                 }
    //             }
    //     }
    // }

    private void remp_case(int x, int y, int val)
    {
        System.out.printf("removing case at %d %d val %d\n", x, y, val);
        if (x >=0 && x < 19 && y >=0 && y <19)
        {
            if (cur_turn == 1)
            {                
                sc.one -= factor[str[dir][x][y]];
                if (in_goban(x+dx, y+dy) && MinMax.map[x+dx][y+dy] == cur_turn)
                {
                    str1[dir][x][y] -= val;
                    sc.one += factor[str[dir][x][y]];
                    return;
                }

            }
            else
            {
                sc.two -= factor[str[dir][x][y]];
                if (in_goban(x+dx, y+dy) && MinMax.map[x+dx][y+dy] == cur_turn)
                {
                    str2[dir][x][y] -= val;
                    sc.two += factor[str[dir][x][y]];
                    return;
                }
            }
        }  
    }

    private void rem_case(int x, int y)
    {
        System.out.printf("removing case at %d %d\n", x, y);
        if (x >=0 && x < 19 && y >=0 && y <19)
        {
            if (cur_turn == 1)
                sc.one -= factor[str[dir][x][y]];
            else
                sc.two -= factor[str[dir][x][y]];

            str[dir][x][y] = 0;
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
                if (in_goban(+dx, y+dy) && MinMax.map[x+dx][y+dy] == cur_turn)
                    str2[dir][x][y] = Math.min(4, str[dir][x][y] + st);
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
        System.out.printf("adding case at %d %d of %d\n", x, y, st);
        if (x >= 0 && x < 19 && y >= 0 && y < 19 && MinMax.map[x][y] == 0)
        {
            if (cur_turn == 1)
            {
                sc.one -= factor[str[dir][x][y]];
                str[dir][x][y] = st;
                sc.one += factor[str[dir][x][y]];
            }
            else
            {
                sc.two -= factor[str[dir][x][y]];
                str[dir][x][y] = st;
                sc.two += factor[str[dir][x][y]];
            }
        }
    }

    public void unconnect(int x, int y)
    {
        int decp;
        int decn;

        this.str = cur_player == 1 ? str1 : str2; //generaliser
    
        decp = 0;
        decn = 0;

        for (int i = 1; in_goban(x+i*dx, y+i*dy) && MinMax.map[x + i * dx][y+ i * dy] == cur_turn ; i++)
            decp++;
        for (int i = 1; in_goban(x-i*dx, y-i*dy) && MinMax.map[x- i *dx][y - i *dy] == cur_turn ; i++)
            decn++;
        
        
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

        add_case(x, y, decp+decn);
    }

    // private void d_map(int [][]map)
    // {
    //     for (int i = 0 ; i < 19 ; i++)
    //     {
    //         for(int j = 0 ; j < 19; j++)
    //         {
    //             System.out.printf("%d", map[i][j]);
    //         }
    //         System.out.println();
    //     }
    // }

    public void connect(int x, int y)
    {
        this.str = MinMax.map[x][y] == 1 ? str1 : str2;

        if (str[dir][x][y] == 0)
        {
            //System.out.printf("heeeeeeere %d %d %d\n",x, y, str[x][y]);
            if (in_goban(x-dx, y-dy) && MinMax.map[x-dx][y-dy] == cur_turn)
            {
                rep_case(x - 2 * dx, y - 2 * dy, 3);
                rep_case(x + 2 * dx, y + 2 * dy, 3);
            }
            else
            {
                rep_case(x - dx, y - dy, 2);
                //System.out.printf("sc one %d", sc.one);
                rep_case(x + 2 * dx, y + 2 * dy, 2);
                //System.out.printf("sc two %d", sc.one);
                //System.exit(0);
            }
        }

        if (str[dir][x][y] == 2)
        {
            System.out.println("I am 2 !");
            if (in_goban(x-dx, y-dy) && MinMax.map[x-dx][y-dy] == cur_turn)
            {
                if (str[dir][x - 3*dx][y - 3*dy] == 2)
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
                //System.out.println("correct");
                rep_case(x - dx, y - dy, 3);
                rep_case(x + 3 * dx, y + 3 * dy, 3);
            }

        }

        if (str[dir][x][y] == 3)
        {

            if (in_goban(x-dx, y-dy) && MinMax.map[x-dx][y-dy] == cur_turn)
            {
                //System.out.printf("x %d y %d x-dx %d y-dy %d val %d turn %d\n", x, y, x-dx, y-dy, MinMax.map[x-dx][y-dy], cur_turn);
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

    public void fill(int x, int y)
    {
        for (int i = 0 ; i < 4 ; i++)
        {
            sc.one -= (str1[i][x][y]);
            sc.two -= (str2[i][x][y]);        
            str1[i][x][y]=0;
            str2[i][x][y]=0;
        }
    }

    private boolean is_player(int c)
    {
        if (c == 1 || c == 2)
            return true;
        return false;
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

        System.out.printf("nbp, nbn,val %d %d %d\n", nbp, nbn, val);

        if (nbn < 2)
            nbn = 0;
        if (nbp < 2)
            nbp = 0;

        System.out.printf("nbp, nbn,val %d %d %d\n", nbp, nbn, val);

        spown_case(x, y, nbp + nbn);
    }



    public void analyse_unmove(int x, int y)
    {

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


    public void analyse_move(int x, int y) //adding isplayer==cur turn to save time
    {
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

    public void move(int x, int y, int turn, int player)
    {
        System.out.printf("\nmove %d %d\n", x, y);
        cur_player = player;
        cur_turn = turn;
        m.play(new Candidat.coord(x, y), turn);
        m.display_map();
        analyse_move(x, y);
        display();
    }

    public void unmove(int x, int y, int turn, int player)
    {
        System.out.printf("\nunmove %d %d\n", x, y);
        cur_player=player;
        cur_turn=turn;
        MinMax.map[x][y] = 0;
        analyse_unmove(x, y);
        m.display_map();
        display();
    }

    // public void unmove(int x, int y, int turn, int player)
    // {
    //     System.out.printf("unmove %d %d\n", x, y);
    //     cur_player = player;
    //     cur_turn = turn;
    //     m.unplay(new Candidat.coord(x, y), 0);
    //     m.display_map();
    //     analyse_unmove(x, y);
    //     display();
    // }

    public void display() //to adapt
    {
        display_str(); 
        System.out.printf("score %d %d\n", sc.one, sc.two);
    }

    public void run()
    {
    //TEST 1 Central replace
       move(10, 10, 1, 1);

       move(9, 9, 2, 2);
       //move(10, 12, 1, 1);
       move(9, 10, 1, 1);
       move(8, 10, 2, 2);
       move(11, 10, 1, 1);
       move (13, 10, 2, 2);
       move(9, 8, 1, 1);
       move(7, 11, 2, 2);
       move(12, 10, 1, 1);
       move(7, 9, 2, 2);
       move(11, 9, 1, 1);
       move(9, 11, 2, 2);
       move(9, 7, 1, 1);
       move(12, 8, 2, 2);
       unmove(11, 9, 1, 1);
       unmove(10, 10, 1, 1);


       //unmove(10, 13, 2, 1);

    //    move(10, 13, 1, 1);

    //    move(10, 14, 1, 1);

    //    unmove(10, 10, 1, 1);
    //    unmove(10, 11, 1, 1);
    //    unmove(10, 12, 1, 1);
    //    unmove(10, 13, 1, 1);

       //unmove(10, 14, 1, 1);
    
       //unmove(10, 10, 1, 1);

    // move(10, 10, 1, 1);
    // move(10, 13, 1, 1);
    // move(10, 11, 1, 1);


    //TEST 2
    // move(10,10, 1, 1);
    // move(10, 11, 1, 1);
    // move(10, 13, 1, 1);
    // move(10, 14, 1, 1);

    //TEST 3

    // move(8, 10, 1, 1);

    // move(10, 10, 1, 1);


    // move(11, 10, 1, 1);
    // move(12, 10, 1, 1);
    // move(14, 10, 1, 1);
    // move(13, 10, 1, 1);


    //move(11, 10, 1, 1);
    //move(11, 11, 1, 1);

    // move(10, 10, 1, 1);
    // move(11, 10, 1, 1);
    // move(12, 10, 1, 1);
    //unmove(12, 10, 1, 1);

    //    move(12, 11, 1, 1);
    //    move(12, 9, 1, 1);
    //    move(12, 8, 1, 1);
    //    move(11 ,11, 1, 1);
    //    move(13,10, 2, 1);
    //    move(11, 10, 1,1);
    //    move(11, 11, 1, 1);
    //    move(11, 12, 1, 1);
    //    move(3, 4, 2, 1);s
    //    move(10, 11, 1, 1);
    //    move(4, 3, 2, 1);
    //    move(10,9, 1, 1);
    //    move(6, 6, 2, 1);
    //    move(10, 12, 1, 1);
    //    move (11, 10, 1, 1);

    //    move(1, 1, 2, 1);
    //    move(0, 1, 2, 1);

    //    move(8, 8, 1, 1);
    //    move(7, 7, 1, 1);
    }
}