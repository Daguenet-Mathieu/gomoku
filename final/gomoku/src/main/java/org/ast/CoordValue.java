package org.ast;
import org.utils.*;

class CoordValue extends Union {
    private final Point value;

    public CoordValue(String command, Point value) {
        super(command);
        this.value = value;
    }

    public Point getValue(){
        return this.value;
    }

}