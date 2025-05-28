package org.ast;

class StringValue extends Union {
    private final String value;

    public StringValue(String command, String value) {
        super(command);
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }

}