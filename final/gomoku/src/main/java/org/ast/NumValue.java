package org.ast;
import org.utils.*;

public class NumValue extends Union {
    private double value;
    
    public NumValue(String command) {
        super(command);
    }

    public double getValue(){
        return (this.value);
    }

    public void setValue(double value){
        this.value = value;
    }

}