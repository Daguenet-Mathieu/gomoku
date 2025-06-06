package org.ast;
import org.utils.*;

public class CoordValue extends Union {
    private Point value;

    public CoordValue(String command) {
        super(command);
    }

    // public Point getValue(){
        // return this.value;
    // }

    @Override
    public void setValue(Object value){
        this.value = (Point)value;
    }

}