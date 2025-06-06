package org.ast;
import org.utils.*;

public abstract class Union {
    private String command;

    protected Union(String command) {
        this.command = command;
    }

    public String getCommand(){
        return this.command;
    }
    
    public abstract void setValue(Object value);

    public String getValue(){
        return this.command;
    }
}