package org.ast;
import org.utils.*;

public class CoordValue extends Union {
    private Point value;

    public CoordValue(String command) {
        super(command);
    }

    public CoordValue(String command, CommandType type) {
        super(command, type);
    }

    public Point getVal(){
        return this.value;
    }

    @Override
    public void setValue(Object value){
        this.value = (Point)value;
    }

}