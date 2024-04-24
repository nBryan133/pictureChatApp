package client;
import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class DrawPoint extends Point
{
    public List<Point> points = new ArrayList<>();
    
    public int bSize;

    public Color color;
    
    public DrawPoint(int brushSize, Color col)
    {
        this.bSize = brushSize;
        this.color = col;
    }

}