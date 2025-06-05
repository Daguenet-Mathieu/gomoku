package org.ast;
import org.utils.*;
import java.util.ArrayList;


public class ArrayValue extends Union {
    ArrayList<Point> value;

    public ArrayValue(String command) {
        super(command);
        this.value = new ArrayList();
    }

    @Override
    public void setValue(Object value){
        this.value.add((Point)value);
    }

    public ArrayList<Point> getValue(){
        return this.value;
    }
}