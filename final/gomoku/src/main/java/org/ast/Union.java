package org.ast;
//import org.utils.*;

public abstract class Union {
    private String command;
    public CommandType type;//necessaire? mettre dans unioon?

    protected Union(String command) {
        this.command = command;
    }

    protected Union(String command, CommandType type) {
        this.command = command;
        this.type = type;
    }

    public String getCommand(){
        return this.command;
    }
    
    public abstract void setValue(Object value);

    public String getValue(){
        return this.command;
    }
    public CommandType getType()
    {
        return this.type;
    }

    public void setType(CommandType val)
    {
        this.type = val;
    }

}