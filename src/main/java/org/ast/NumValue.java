package org.ast;

public class NumValue extends Union {
    private Number value;
    
    public NumValue(String command) {
        super(command);
    }

    public NumValue(String command, CommandType type) {
        super(command, type);
    }

    public Number getVal(){
        return (this.value);
    }

    @Override
    public void setValue(Object value){
        this.value = ((Number) value).doubleValue();
    }

}