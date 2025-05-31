package org.ast;
import org.utils.*;

public class CoordValue extends Union {
    private Point value;

    public CoordValue(String command) {
        super(command);
    }

    public Point getValue(){
        return this.value;
    }

    public void setValue(Point value){
        this.value = value;
    }

}