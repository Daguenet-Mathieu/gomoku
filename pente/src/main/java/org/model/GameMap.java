package org.model;
import java.util.*;
import javafx.scene.paint.*;

import javafx.scene.shape.Circle;

public class GameMap {
    public int color;
    public Circle node;

    public GameMap()
    {
        color = 0;
        node = null;
    }
    public int getColor()
    {
        return color;
    }
    public int getNode()
    {
        return color;
    }
    public void setColor(int new_color)
    {
        color = new_color;
    }
    public void setNode(Circle new_node)
    {
        node = new_node;
    }
}