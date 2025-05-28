package org.ast;
import org.utils.*;
import java.util.ArrayList;


class ArrayValue extends Union {
    ArrayList<Point> value;

    public ArrayValue(String command, ArrayList<Point> value) {
        super(command);
        this.value = value;
    }

    public void AddMove(Point p){
        this.value.add(p);
    }

    public ArrayList<Point> getValue(){
        return this.value;
    }

}