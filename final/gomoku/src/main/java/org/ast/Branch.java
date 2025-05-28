package org.ast;
import org.utils.*;

class Branch extends Union {
    Node node;

    public Branch(){
        super(null);
        this.node = new Node();
    }
}
