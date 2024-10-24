package client;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.text.html.HTMLDocument;

public class DrawInterface extends JFrame implements ActionListener
{
    DrawingPanel drawPanel = new DrawingPanel();

    JPanel buttonPanel = new JPanel();
    JPanel dropDownPanel = new JPanel();
    JPanel rightPanel = new JPanel();
    JPanel sendPanel = new JPanel();

    String htmlContent;

    String[] colors = { "Black", "Red", "Blue", "Green", "Magenta", "Orange"};
    JComboBox<String> colorSelect = new JComboBox<>(colors);

    JButton brushSize = new JButton("Brush Size");
    JButton clear = new JButton("Clear Screen");
    JButton send =  new JButton("Send");
    
    JCheckBox eraser = new JCheckBox("Toggle Eraser");

    Color buf = null;

    Graphics2D g2d;

    BufferedImage image;

    ChatInterface cInt;
    
    DrawInterface(ChatInterface cInterface)
    {

        cInt = cInterface;

        setUpMainFrame();
    }

    private void setUpMainFrame()
    {

        colorSelect.setPreferredSize(new Dimension(100, 20));
        colorSelect.setMaximumSize(new Dimension(100, 20));
        colorSelect.setActionCommand("COL");
        colorSelect.addActionListener(this);

        brushSize.setActionCommand("BSIZ");
        brushSize.addActionListener(this);

        clear.setActionCommand("CLEAR");
        clear.addActionListener(this);

        eraser.setActionCommand("ERASE");
        eraser.addActionListener(this);

        send.setActionCommand("SEND");
        send.addActionListener(this);

        dropDownPanel.setLayout(new BoxLayout(dropDownPanel, BoxLayout.X_AXIS));
        dropDownPanel.add(colorSelect);

        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.add(brushSize);
        buttonPanel.add(eraser);
        buttonPanel.add(clear);

        sendPanel.setLayout(new CardLayout());
        sendPanel.add(send);

        drawPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        add(dropDownPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.WEST);
        add(drawPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
        add(sendPanel, BorderLayout.SOUTH);
        
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension d = (tk.getScreenSize());

        setSize(d.width / 3, d.height / 3);
        setLocation((d.width / 2) - (this.getWidth() / 2), (d.height / 2) - (this.getHeight() / 2));

        setTitle("DrawWindow ver 0.01");

        setResizable(false);

        setVisible(true);
    }

    static Color getColor(String col)
    {
        Color color;

        switch(col.toLowerCase(Locale.ENGLISH))
        {
            default -> color = Color.BLACK;
            case "black" -> color = Color.BLACK;
            case "blue" -> color = Color.BLUE;
            case "cyan" -> color = Color.CYAN;
            case "darkgray" -> color = Color.DARK_GRAY;
            case "gray" -> color = Color.GRAY;
            case "green" -> color = Color.GREEN;
            case "yellow" -> color = Color.YELLOW;
            case "lightgray" -> color = Color.LIGHT_GRAY;
            case "magenta" -> color = Color.MAGENTA;
            case "orange" -> color = Color.ORANGE;
            case "pink" -> color = Color.PINK;
            case "red" -> color = Color.RED;
            case "white" -> color = Color.WHITE;
        }

        return color;
    }
    

    // Convert BufferedImage to Base64 encoded string
    private static String bufferedImageToBase64(BufferedImage image)
    {
        String base64String = null;
        try(ByteArrayOutputStream baos = new ByteArrayOutputStream()) 
        {
            ImageIO.write(image, "png", baos);

            base64String = Base64.getEncoder().encodeToString(baos.toByteArray());

            baos.close();
        } 
        catch(IOException e)
        {
            e.printStackTrace();
        }

        return base64String;
    }

    static void appendToEditorPane(JEditorPane editorPane, String textContent) 
    {
        HTMLDocument doc = (HTMLDocument) editorPane.getDocument();

        try 
        {
            doc.insertBeforeEnd(doc.getDefaultRootElement(),  ("<div style='text-align: left;'>" + textContent + "</div>")); 
        } 
        catch (IOException | javax.swing.text.BadLocationException e) 
        {
            e.printStackTrace();
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if(e.getActionCommand().equals("CLEAR"))
        {
            drawPanel.clear();
        }

        if(e.getActionCommand().equals("BSIZ"))
        {
            if(drawPanel.getBrushSize() < 5)
            {
                drawPanel.setBrushSize(1 + drawPanel.getBrushSize());
            }
            else
            {
                drawPanel.setBrushSize(1);
            }
        }

        if(e.getActionCommand().equals("ERASE"))
        {

            if(eraser.isSelected())
            {
                buf = drawPanel.getColor();
                drawPanel.setColor(drawPanel.getBackground());
            }
            else
            {
                drawPanel.setColor(buf);
            }
        }

        if(e.getActionCommand().equals("COL"))
        {
            drawPanel.setColor(getColor(colorSelect.getSelectedItem().toString()));

            if(eraser.isSelected())
            {
                eraser.setSelected(false);
            }
        }

        if(e.getActionCommand().equals("SEND"))
        {
            image = new BufferedImage((int)drawPanel.getSize().getWidth(), (int)drawPanel.getSize().getHeight(), BufferedImage.TYPE_INT_RGB);
            g2d = image.createGraphics();
            drawPanel.paint(g2d);

            String iString = bufferedImageToBase64(image);

            htmlContent = "<img src='data:image/png;base64," + iString + "'/>";

            cInt.message = htmlContent;

            appendToEditorPane(cInt.msgPanel, ("<FONT COLOR = 'BLUE'>" + cInt.uName + ">" + htmlContent + "</FONT>"));

            this.dispose();
        }
    }
}
