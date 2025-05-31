package org.ast;
import org.utils.*;

public class Command extends Union{
    public CommandType value;//necessaire? mettre dans unioon?
    public Union       DataType;
    public Node        next;

    public Command(){
        super("command");
    }
}
