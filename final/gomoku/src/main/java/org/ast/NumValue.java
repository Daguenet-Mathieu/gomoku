package org.ast;
import org.utils.*;

class NumValue extends Union {
    private final double value;
    
    public NumValue(String command, double value) {
        super(command);
        this.value = value;
    }

    double getValue(){
        return (this.value);
    }
}