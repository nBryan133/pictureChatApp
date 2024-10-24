package client;
import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }

        if (!(o instanceof DrawPoint))
        {
            return false;
        }

        if(!super.equals(o))
        {
            return false;
        }

        DrawPoint that = (DrawPoint) o;

        return bSize == that.bSize 
        && Objects.equals(color, that.color) 
        && Objects.equals(points, that.points);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), bSize, color, points);
    }

}