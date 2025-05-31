package org.ast;
import org.utils.*;
import java.util.ArrayList;


public class ArrayValue extends Union {
    ArrayList<Point> value;

    public ArrayValue(String command) {
        super(command);
        this.value = new ArrayList();
    }

    public void AddMove(Point p){
        this.value.add(p);
    }

    public ArrayList<Point> getValue(){
        return this.value;
    }
}