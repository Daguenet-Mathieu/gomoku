package org.ast;
import org.utils.*;

public class Node extends Union{
    public CommandType value;//necessaire? mettre dans unioon?
    public Union       DataType;
    public Node        next;

    public Node(){
        super("branch");
    }
    
    @Override
    public void setValue(Object value){
        this.DataType = (Union)value;
    }
}
