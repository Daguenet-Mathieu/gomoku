package org.ast;
import org.utils.*;
import java.util.ArrayList;


public class ArrayValue extends Union {
    ArrayList<Point> value;

    public ArrayValue(String command) {
        super(command);
    }

    public ArrayValue(String command, CommandType type) {
        super(command, type);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setValue(Object value){
        this.value = ((ArrayList<Point>)value);
    }

    public ArrayList<Point> getVal(){
        return this.value;
    }
}