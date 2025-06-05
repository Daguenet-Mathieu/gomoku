package org.ast;
import org.utils.*;

public class NumValue extends Union {
    private Number value;
    
    public NumValue(String command) {
        super(command);
    }

    public Number getValue(){
        return (this.value);
    }

    @Override
    public void setValue(Object value){
        this.value = ((Number) value).doubleValue();
    }

}