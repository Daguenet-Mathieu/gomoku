package org.model;

import java.util.*;

public class Eval
{
    static public class stone 
    {
        int number;
        int strength;
        boolean type;
        String dir;
        public stone(int nm, int str, boolean tp){this.number= nm;this.strength=str;this.type=tp;}
        public stone(int nm, int str, boolean tp, String d){this.number= nm;this.strength=str;this.type=tp; this.dir=d;} 
    }

    public int [] co;
    public int [] unco;

    public int free3 = 0;
    public int unfree3 = 0;

    public int free = 0;
    public int unfree = 0;
    public int free4 = 0;
    public int unfree4 = 0;
    

    public ArrayList<Eval.stone> stones;

    public void add_stones(int number, int strength, boolean type)
    {
        stones.add(new Eval.stone(number, strength, type));
    }
    public void add_stones(int number, int strength, boolean type, String direction)
    {
        stones.add(new Eval.stone(number, strength, type, direction));
    }

    public void clear_stones()
    {
        this.free=0;
        this.unfree4=0;
        this.stones.clear();
    }

    private float determine(Eval.stone st, int depth)
    {
        if (st.number >= 5)
        {
            if (st.type)
                return 10000;
            else
                return -9000;
        }

        if (st.number == 4)
        {
            if (st.strength == 2)
            {
                if (st.type)
                    return 4500;
                return -5000;
            }
            if (st.strength == 1)
            {
                if (st.type)
                {
                    this.free4 +=1;
                    if (this.free4 >= 2)
                        return 4500;
                    return 2000;
                }
                else
                {
                    this.unfree4+=1;
                    if (this.unfree4 >= 2)
                        return -5000;
                    return -2000;
                }
            }
        }

        if (st.number == 3)
        {
            if (st.strength == 2)
            {
                if (st.type)
                {
                    this.free +=1;
                    if (this.free >= 2)
                        return 500;
                    return 18;
                }
                else
                {
                    this.unfree+=1;
                    if (this.unfree >=2)
                        return -600;
                    return -18;
                }
            }
            if (st.strength == 1)
            {
                if (st.type)
                    return 6;
                return -6;
            }
        }

        if (st.number == 2)
        {
            if (st.strength == 2)
            {
                if (st.type)
                    return 5;
                return -5;
            }
            if (st.strength == 1)
                if (st.type)
                    return 1;
                return -1;
        }
        return 0;
    }

    public float evaluate(int depth)
    {
        Eval.stone st;
        float res = 0;
        //float cur;
        for (int i = 0 ; i < this.stones.size() ; i++)
        {
            st = this.stones.get(i);
            //cur = determine(st);
            if (st.strength != 0 && st.number != 1)
                res += determine(st, depth);
        }
        return res;
    }

    public void display()
    {
        Eval.stone st;
        for (int i = 0 ; i < this.stones.size() ; i++)
        {
            st = this.stones.get(i);
            System.out.printf("nm %d st %d type %d %s\n", st.number, st.strength, st.type?1:0, st.dir);            
        }
    }

    Eval()
    {
        this.co = new int[5];
        this.unco = new int[5];
        this.stones = new ArrayList<Eval.stone>();
    } 
}