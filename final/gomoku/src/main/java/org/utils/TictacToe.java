package org.utils;

import java.util.ArrayList;
import java.util.Scanner;

public class TictacToe
{

    public int [] [] grid = new int[9][9];
    public int [] [] simu = new int[9][9];
    static char[] symb = {' ', 'X', 'O'};
    public Scanner sc = new Scanner(System.in);
    public int player_humain = -1;
    public int player_ai;
    public static int[][] dir = {{0, 1}, {1, 0}, {1, 1}};
    boolean victory = false;
    boolean request_move = true;
    public int nb_move = 0;
    int best;
    static int max_depth = 5; // 4 encore un peu vulnerable
    static String mode = "both";
    static boolean cut = true;
    int nb_pos = 0;

    public class Coord {
        public int x;
        public int y;
        Coord(int x, int y)
        {
            this.x = x;
            this.y = y;
        }
    }

    public boolean ingrid(int i, int j)
    {
        if (i > 2 || i < 0 || j > 2 || j < 0)
        {
            return false;
        }
        return true;
    }

    public void print_stat(int score)
    {
        print_simu();

        System.out.printf("score %d\n", score);

    }

    public void print_simu()
    {
        for (int i = 0 ; i < 3 ; i++)
        {
            for (int j = 0 ; j < 3 ; j++)
            {
               System.out.printf("|%c|", symb[simu[i][j]]); 
            }
            System.out.println();
        }

    }


    public void print_stat(int score, int [] [] mysimu)
    {

        for (int i = 0 ; i < 3 ; i++)
        {
            for (int j = 0 ; j < 3 ; j++)
            {
               System.out.printf("|%c|", symb[mysimu[i][j]]); 
            }
            System.out.println();
        }
        System.out.printf("score %d\n", score);

    }

    public void add_score(int score [], int i, int j, int[][] mysimu)
    {
        int sy = mysimu[i][j];

        if (sy == 0)
            return;

        if (i == j && i == 1)
        {
            if (mysimu[i][j+1] == sy)
                score[sy-1] +=1;
            if (mysimu[i][j-1] == sy)
                score[sy-1] +=1;
            if (mysimu[i+1][j] == sy)
                score[sy-1] +=1;
            if (mysimu[i-1][j] == sy)
                score[sy-1] +=1;
            if (mysimu[i+1][j+1] == sy)
                score[sy-1] +=1;
            if (mysimu[i-1][j+1] == sy)
                score[sy-1] +=1;
            if (mysimu[i+1][j-1] == sy)
                score[sy-1] +=1;
            if (mysimu[i-1][j-1] == sy)
                score[sy-1] +=1;
        }
        else
        {
            if (ingrid(i, j+1) && mysimu[i][j+1] == sy)
                score[sy-1]+=1;
            if (ingrid(i, j-1) && mysimu[i][j-1] == sy)
                score[sy-1]+=1;
            if (ingrid(i+1, j) && mysimu[i+1][j] == sy)
                score[sy-1]+=1;
            if (ingrid(i-1, j) && mysimu[i-1][j] == sy)
                score[sy-1]+=1;
        }
    }


    public void add_score(int score [], int i, int j)
    {
        int sy = simu[i][j];

        if (sy == 0)
            return;

        if (i == j && i == 1)
        {
            if (simu[i][j+1] == sy)
                score[sy-1] +=1;
            if (simu[i][j-1] == sy)
                score[sy-1] +=1;
            if (simu[i+1][j] == sy)
                score[sy-1] +=1;
            if (simu[i-1][j] == sy)
                score[sy-1] +=1;
            if (simu[i+1][j+1] == sy)
                score[sy-1] +=1;
            if (simu[i-1][j+1] == sy)
                score[sy-1] +=1;
            if (simu[i+1][j-1] == sy)
                score[sy-1] +=1;
            if (simu[i-1][j-1] == sy)
                score[sy-1] +=1;
        }
        else
        {
            if (ingrid(i, j+1) && simu[i][j+1] == sy)
                score[sy-1]+=1;
            if (ingrid(i, j-1) && simu[i][j-1] == sy)
                score[sy-1]+=1;
            if (ingrid(i+1, j) && simu[i+1][j] == sy)
                score[sy-1]+=1;
            if (ingrid(i-1, j) && simu[i-1][j] == sy)
                score[sy-1]+=1;
        }
    }

    void print_eval(ArrayList<Integer> lst, int turn)
    {
        if (turn == player_ai)
            maxL(lst);
        else
            minL(lst);
        System.out.printf("evaluation best %d\n", best);
        for (int i = 0 ; i < lst.size() ; i++)
        {
            System.out.printf("%d\n", lst.get(i));
        }
    }

    public int eval()
    {
        int score[] = new int[2];

        for (int i = 0 ; i < 3 ; i++)
        {
            for (int j = 0 ; j < 3 ; j++)
            {
                add_score(score, i, j);
            }
        }

        if (player_ai == 1)
            return score[0] - score[1];
        else
            return score[1] - score[0];
    }

    public int eval(int [] [] mysimu)
    {
        int score[] = new int[2];

        for (int i = 0 ; i < 3 ; i++)
        {
            for (int j = 0 ; j < 3 ; j++)
            {
                add_score(score, i, j, mysimu);
            }
        }

        if (player_ai == 1)
            return score[0] - score[1];
        else
            return score[1] - score[0];
    }


    public int other(int player)
    {
        if (player == 1)
            return 2;
        if (player == 2)
            return 1;
        return 0;
    }

    public int maxL(ArrayList<Integer> lst)
    {
        if (lst.size() == 0)
            return 0;
        int cur_max = lst.get(0);
        for (int i = 0 ; i < lst.size() ; i++)
        {
            if (lst.get(i) >= cur_max)
            {
                cur_max = lst.get(i);
                best = i;
            }
        }
        return cur_max;
    }

    public int minv(int a, int b)
    {
        if (a < b)
            return a;
        return b;
    }

    public int maxv(int a, int b)
    {
        if (a > b)
            return a;
        return b;
    }

    public int minL(ArrayList<Integer> lst)
    {
        if (lst.size() == 0)
            return 0;
        int cur_min = lst.get(0);
        for (int i = 0 ; i < lst.size() ; i++)
        {
            if (lst.get(i) <= cur_min)
            {
                cur_min = lst.get(i);
                best = i;
            }
        }
        return cur_min;
    }

    public int value_victory(int turn)
    {
        if ((turn == 1 && player_ai == 1) || (turn == 2 && player_ai == 2))
            return 1000;
        else
            return -1000;
    }

    public int circular_add(int dep, int end)
    {
        if (dep == 0)
            return end;
        if (dep == 1)
            {
                if (end == 1)
                    return 1;
                if (end == 2)
                    return -1;
            }
        if (dep == 2)
        {
            if (end == 1)
                return -2;
            if (end == 2)
                return -1;
        }
        return 0;
    }

    public boolean is_win(int i, int j)
    {
        int sy = simu[i][j];

        if (simu[i][j + circular_add(j, 1)] == sy && simu[i][j + circular_add(j, 2)] == sy)
            return true;
        if (simu[i + circular_add(i, 1)][j] == sy && simu[ i + circular_add(i, 2)][j] == sy)
            return true;
        if (i == j)
            {
                if (simu[0][0] == sy && simu[1][1] == sy && simu[2][2] == sy)
                    return true;
            }
        if (i == 0 && j == 2 || i == 2 && j == 0)
            {
                if (simu[0][2] == sy && simu[1][1] == sy && simu[2][0] == sy)
                    return true;
            }
        return false;
        
    }

    public int ended()
    {
        boolean blank = false;
        for (int i = 0 ; i < 3 ; i ++)
        {
            for (int j = 0 ; j < 3 ; j++)
            {
                if (simu[i][j] == 0)
                    blank = true;
                else
                {
                    if (is_win(i, j))
                    {
                        //System.out.printf("victory %d %d\n", player_ai, simu[i][j]);
                        if (player_ai == simu[i][j])
                            return 1000;
                        return -1000;
                    }
                }
            }
        }
        if (blank == false)
            return 0;
        return -1;
    }

    // private void load_mysimu(int [][] psimu, int [] [] mysimu)
    // {
    //     for (int i = 0 ; i < 3 ; i++)
    //         for (int j = 0 ; j < 3 ; j++)
    //             mysimu[i][j] = psimu[i][j];

    // }

    public int abminmax(int depth, int turn, int alpha, int beta)
    {
        int res;
        int alphap;
        int betap;
        int end;
        //int [][] mysimu = new int [3][3];

        //System.out.printf("\tdepth %d\n", depth);

        
        //load_mysimu(psimu, mysimu);

        ArrayList<Integer> val = new ArrayList<Integer>();

        end = ended();
        
        if (end != -1)
        {
            nb_pos++;
            //print_stat(end);
            return end;
        }


        if (depth == 0)
        {
            res = eval();
            nb_pos++;
            //print_stat(res);
            return res;
        }
        else
        {
            //print_simu();
        }

        alphap = -5000;
        betap = 5000;

            for (int x = 0 ; x < 3 ; x++)
            {
                for (int y = 0 ; y < 3 ; y++)
                {
                    if (simu[x][y] == 0)
                    {
                        simu[x][y] = turn;

                        if (turn == player_humain)
                        {
                            res = abminmax(depth - 1, other(turn), alpha, minv(beta, betap));
                            val.add(res);
                            betap = minv(betap, res);

                            if (cut && alpha > betap) // alpha cut
                            {
                                simu[x][y] = 0;
                                minL(val);
                                //System.out.println("\nalphacut\n");
                                return betap;
                            }
                        }
                        else
                        {
                            res = abminmax(depth - 1, other(turn), maxv(alpha, alphap), beta);
                            val.add(res);
                            alphap = maxv(alphap, res);

                        if (cut && alphap > beta) // beta cut
                        {
                            simu[x][y] = 0;
                            maxL(val);
                            //System.out.println("\nbetacut\n");
                            return alphap;
                        }

                        }
                        simu[x][y] = 0;
                    }
                }
            }

            if (depth == max_depth)
                print_eval(val, turn);

            if (turn == player_ai)
                //return alphap;
                return maxL(val);
            else
                //return betap;
                return minL(val);
    }

    public int abminmax2(int depth, int turn, int alpha, int beta)
    {
        int res;
        int alphap;
        int betap;
        int end;
        //int [][] mysimu = new int [3][3];

        //System.out.printf("\tdepth %d\n", depth);

        
        //load_mysimu(psimu, mysimu);

        ArrayList<Integer> val = new ArrayList<Integer>();

        end = ended();
        
        if (end != -1)
        {
            nb_pos++;
            //print_stat(end);
            return end;
        }


        if (depth == 0)
        {
            res = eval();
            nb_pos++;
            //print_stat(res);
            return res;
        }
        else
        {
            //print_simu();
        }

        alphap = -5000;
        betap = 5000;

            for (int x = 0 ; x < 3 ; x++)
            {
                for (int y = 0 ; y < 3 ; y++)
                {
                    if (simu[x][y] == 0)
                    {
                        simu[x][y] = turn;

                        if (turn == player_ai)
                        {
                            res = abminmax(depth - 1, other(turn), alpha, beta);
                            alphap = Math.max(alphap, res);
                            alpha = Math.max(alpha, res);
                            val.add(res);
                            simu[x][y] = 0;


                            if (cut && beta <= alpha) // beta cut
                            {
                                //simu[x][y] = 0;
                                break;
                                //System.out.println("\nalphacut\n");
                            }
                            return alphap;
                        }
                        else
                        {
                            res = abminmax(depth - 1, other(turn), alpha, beta);
                            betap = Math.min(betap, res);
                            beta = Math.min(beta, res);
                            val.add(res);
                            simu[x][y] = 0;


                            if (cut && beta <= alpha) // beta cut
                            {
                                //simu[x][y] = 0;
                                break;
                                //System.out.println("\nbetacut\n");
                            }
                            return betap;
                        }
                    }
                }
            }

            if (depth == max_depth)
                print_eval(val, turn);

            if (turn == player_ai)
                //return alphap;
                return maxL(val);
            else
                //return betap;
                return minL(val);
    }

    public int minmax(int depth, int turn)
    {
        int res;
        ArrayList<Integer> val = new ArrayList<Integer>();

        if (depth == 0)
        {
            res = eval();
            nb_pos++;
            //print_stat(res);
            return res;
        }

        for (int x = 0 ; x < 3 ; x++)
        {
            for (int y = 0 ; y < 3 ; y++)
                {
                    if (simu[x][y] == 0)
                    {
                        simu[x][y] = turn;
                        if (threeraw(simu, x, y, false))
                            {
                                val.add(value_victory(turn));
                                nb_pos++;
                                //print_stat(value_victory(turn));
                            }
                        else
                            {
                                val.add(minmax(depth - 1, other(turn)));
                            }
                        simu[x][y] = 0;
                    }
                }
            }

        if (depth == max_depth)
            print_eval(val, turn);

        if (turn == player_ai)
            return maxL(val);
        else
            return minL(val);
    }

    public boolean getHumanmove(int player)
    {
        request_move = true;
        while (request_move)
        {
            System.out.println("please enter your move :");
            String line = sc.nextLine();

            if (line.length() != 2)
            {
                System.out.println("error coordinate too long");
                continue;
            }
            int x = line.charAt(0) - '0';
            int y = line.charAt(1) - '0';

            if (x > 3 || x < 1 || y > 3 || y < 1)
            {
                System.out.printf("error wrong coordinate %d%d\n", x, y);
                continue;
            }
            if (grid[x-1][y-1] != 0)
            {
                System.out.printf("error there %d%d is occuped !\n", x, y);
                continue;
            }
            request_move = false;
            grid[x-1][y-1] = player;
            nb_move++;
            if (nb_move == 9)
                return true;
            return threeraw(grid, x-1, y-1, true);
        }
        return false;
    }

    public void print_grid()
    {
        for (int i = 0 ; i < 3 ; i++)
        {
            for (int j = 0 ; j < 3 ; j++)
            {
                System.out.printf("|%c|", symb[grid[i][j]]);
            }
            System.out.println();
        }
        System.out.println();
    }

    public boolean threeraw(int [][] grid, int x, int y, boolean save)
    {
        int sy = grid[x][y];
        int compt = 0;

        while (compt < 3 && grid[x][compt] == sy)
                compt++;
        if (compt == 3)
        {
            if (save)
                victory = true;
            return true;
        }

        compt = 0;
        while (compt < 3 && grid[compt][y] == sy)
            compt++;

        if (compt == 3)
        {
            if (save)
                victory = true;
            return true;
        }

        compt = 0;

        while(compt < 3 && grid[compt][compt] == sy)
            compt++;
        if (compt == 3)
        {
            if (save)
                victory = true;
            return true;
        }

        if (grid[0][2] == sy && grid[1][1] == sy && grid[2][0] == sy)
        {
            if (save)
                victory = true;
            return true;
        }

        return false;

    }

    public boolean play_best(int player)
    {
        int compt = 0;
        for (int i = 0 ; i < 3 ; i++)
        {
            for (int j = 0 ; j < 3 ; j++)
            {
                if (grid[i][j] == 0)
                {
                    if (compt == best)
                        {
                            grid[i][j] = player;
                            System.out.printf("%d%d (%d) position evaluated\n", i+1, j+1, nb_pos);
                            return threeraw(grid, i, j, true);
                        }
                    compt ++;
                }
            }
        }
        return false;
    }

    public boolean do_ia_move(int player)
    {
        int best1;
        int best2;

        if (nb_move >= 9)
            return true;

        load_simu();

        if (mode == "minmax")
        {
            minmax(max_depth, player);
        }
        else if (mode == "abminmax")
        {
            abminmax(max_depth, player, -10000, +10000);
        }
        else if (mode == "both")
        {
            minmax(max_depth, player);
            best1 = best;
            load_simu();
            abminmax(max_depth, player, -10000, +10000);
            best2 = best;

            System.out.printf("minmax best : %d abminmax best : %d\n", best1, best2);
        }
        else 
        {
            System.out.println("No ai loaded");
            System.exit(0);
        }
        nb_move++;
        return play_best(player);

    }

    public void load_simu()
    {
        nb_pos = 0;
        for (int i = 0 ; i < 3 ; i++)
            for (int j = 0 ; j < 3 ; j++)
                {
                    simu[i][j] = grid[i][j];
                }
    }

    public void game()
    {
        if (player_humain == -1)
            return;
        if (player_ai == 1)
        {
            do_ia_move(player_ai);
            print_grid();
        }
        while (nb_move < 9)
        {
            if (getHumanmove(player_humain))
                break;
            print_grid();
            if (do_ia_move(player_ai))
                break;
            print_grid();
            //System.out.printf("nb_move %d\n", nb_move);
        }

        print_grid();

        if (victory)
        {
            //System.out.printf(" player ai %d player human %d nb_move %d\n", player_humain, player_ai, nb_move);

            if ((player_humain == 1 && (nb_move % 2) == 1 ) || (player_humain == 2 && (nb_move % 2) == 0))
                System.out.println("Congratulation you won against the Ai !");
            else
                System.out.println("Watchout, the Ai won !");
        }
        else
        {
            System.out.println("Well done, it is a draw");
        }
    }

    public void select_colors()
    {
        System.out.println("Do you want to play X or O ?");
        String line = sc.nextLine();
        if (line.length() != 1)
        {
            System.out.println("Error please choose X or O");
            System.exit(0);
        }

        if (line.charAt(0) == 'O')
        {
            player_ai = 1;
            player_humain = 2;
        }
        else if (line.charAt(0) == 'X')
        {
            player_ai = 2;
            player_humain = 1;
        }
        else 
        {
            System.out.println("Error please choose X or O");
            System.exit(0);
        }
    }

    public static void main(String args[])
    {
        TictacToe T = new TictacToe();

        System.out.println("Welcome in TicTacToe !");
        T.select_colors();
        T.game();
    }
}

