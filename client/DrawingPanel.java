package client;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public class DrawingPanel extends JPanel
{
    private List<DrawPoint> shapes = new ArrayList<>();

    private  int bSize;

    private  Color color;

    public DrawingPanel()
    {
        bSize = 3;
        color = Color.BLACK;

        setPreferredSize(new Dimension(200, 150));
        setBackground(Color.WHITE);

        addMouseListener(new MouseAdapter() 
        {
            @Override
            public void mousePressed(MouseEvent e) 
            {
                shapes.add(new DrawPoint(bSize, color));
                shapes.get(shapes.size() - 1).points.add(e.getPoint());
                repaint();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() 
        {

            @Override
            public void mouseDragged(MouseEvent e)
            {
                shapes.get(shapes.size() - 1).points.add(e.getPoint());
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        

        for(int i = 0; i < shapes.size(); i++)
        {
            g2d.setColor(shapes.get(i).color);
            g2d.setStroke(new BasicStroke(shapes.get(i).bSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            for(int n = 1; n < shapes.get(i).points.size(); n++)
            {
                Point p1 = shapes.get(i).points.get(n - 1);
                Point p2 = shapes.get(i).points.get(n);
                g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
        }

        g2d.dispose();
    }

    public void setBrushSize(int size)
    {
        this.bSize = size;
    }

    public int getBrushSize()
    {
        return this.bSize;
    }

    public void setColor(Color color)
    {
        this.color = color;
    }

    public Color getColor()
    {
        return this.color;
    }

    public void clear()
    {
        shapes.clear();
        repaint();
    }
}
