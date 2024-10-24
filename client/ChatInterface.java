package client;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.text.html.HTMLDocument;


public class ChatInterface extends JFrame implements ActionListener
{

    DrawInterface dInterface;

    Thread cThread;

    JButton sendButton = new JButton("Send");
    JButton drawButton = new JButton("Draw");
    JButton logoutButton = new JButton("Logout");

    JTextField textbox = new JTextField(20);

    JLabel name;

    JPanel inputPanel = new JPanel();
    JPanel topPanel = new JPanel(new BorderLayout());

    JEditorPane msgPanel;
    JScrollPane scrollPane;

    String message = "";
    //String response;
    String uName;

    //BufferedImage image;

    //private InetAddress host;

    private boolean running;

    ChatInterface(String userName, int PORT)
    {        
        running = true;
        
        this.uName = userName;

        setUpMainFrame(PORT);

        appendToEditorPane(msgPanel, uName + " has entered the chat.");

        message = "/online";

    }


    private void setUpMainFrame(int PORT)
    {
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension d = (tk.getScreenSize());

        cThread = new Thread(new ChatCommunication(this, PORT));

        cThread.start();

        this.getRootPane().setDefaultButton(sendButton);

        name = new JLabel(uName);

        msgPanel = new JEditorPane();

        msgPanel.setContentType("text/html");
        msgPanel.setEditorKit(new BASE64HTMLEditorKit());
        msgPanel.setEditable(false);

        scrollPane = new JScrollPane(msgPanel);

        sendButton.setActionCommand("SEND");
        sendButton.addActionListener(this);

        drawButton.setActionCommand("DRAW");
        drawButton.addActionListener(this);

        logoutButton.setActionCommand("LOUT");
        logoutButton.addActionListener(this);

        inputPanel.add(drawButton);
        inputPanel.add(textbox);
        inputPanel.add(sendButton);
        
        topPanel.add(logoutButton, BorderLayout.WEST);
        topPanel.add(name, BorderLayout.EAST);

        add(inputPanel, BorderLayout.SOUTH);
        add(scrollPane, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        setSize(d.width / 2, d.height / 2);
        setLocation(d.width / 4, d.height / 4);

        setTitle("ChatRoom ver 0.01");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setVisible(true);

        setResizable(false);
    }

    static void appendToEditorPane(JEditorPane editorPane, String textContent) 
    {
        HTMLDocument doc = (HTMLDocument) editorPane.getDocument();

        try 
        {
            doc.insertBeforeEnd(doc.getDefaultRootElement(), "<div style='text-align: :left;'>" + textContent + "</div>");
        } 
        catch (IOException | javax.swing.text.BadLocationException e) 
        {
        }
    }

    public boolean getRunning()
    {
        return this.running;
    }

    @Override
    public void actionPerformed(ActionEvent e) 
    {
        if("DRAW".equals(e.getActionCommand()))
        {
            dInterface = new DrawInterface(this);
        }
        
        if("LOUT".equals(e.getActionCommand()))
        {
            LoginInterface.logFrame.setVisible(true);
            running = false;
            msgPanel.setText("");
            this.dispose();

            if(dInterface != null)
            {
                dInterface.dispose();
            }
        }

        if(e.getActionCommand().equals("SEND"))
        {
            message = textbox.getText();
            String msg = textbox.getText() ;
            textbox.setText("");
    
            if(!msg.isBlank() && !msg.toLowerCase(Locale.ENGLISH).equals("/online"))
            {
                appendToEditorPane(msgPanel ,("<FONT COLOR = 'BLUE'>" + uName + ">" + msg + "</FONT>"));
            }
        }
    }
}
