package org.ast;

public class StringValue extends Union {
    private String value;

    public StringValue(String command) {
        super(command);
    }

    public StringValue(String command, CommandType type) {
        super(command, type);
    }

    public String getVal(){
        return this.value;
    }

    @Override
    public void setValue(Object value){
        this.value = (String)value;
    }
}