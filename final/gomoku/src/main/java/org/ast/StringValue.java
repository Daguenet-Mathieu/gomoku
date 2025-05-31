package org.ast;

public class StringValue extends Union {
    private String value;

    public StringValue(String command) {
        super(command);
    }

    public String getValue(){
        return this.value;
    }
    public void setValue(String value){
        this.value = value;
    }
}