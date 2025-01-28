package org.utils;

public class Score {

    public int one;
    public int two;

    public Score()
    {
        this.one = 0;
        this.two = 0;
    }

    public int victory(int num)
    {
        if (num == 1)
            this.one = 1000;
        else
            this.two = 1000;
        return num;
    }

    public void loss(int num, int score)
    {
        if (num < 0)
        {
            this.two -= Math.abs(num);
        }
        else
        {
            this.one -= Math.abs(num);
        }
    }

    public void change_case(int old, int nw, int nb)
    {
        if (nb == 1)
        {
            this.one = this.one - old + nw;
        }
        else
        {
            this.two = this.two - old + nw;
        }
    }


}