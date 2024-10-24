package client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;


public class LoginInterface extends JFrame implements ActionListener
{
    JPanel outer = new JPanel(new BorderLayout());
    JPanel uPanel = new JPanel();
    JPanel bPanel = new JPanel();

    JTextField uName = new JTextField(10);

    JRadioButton room1 = new JRadioButton("Room 1");
    JRadioButton room2 = new JRadioButton("Room 2");

    JLabel uLabel = new JLabel("Username");

    static JButton login = new JButton("Login");

    static LoginInterface logFrame;

    public static void main(String[] args)
    {
        logFrame = new LoginInterface();
    }

    LoginInterface()
    {
        setUpMainFrame();
    }

    private void setUpMainFrame()
    {

        this.getRootPane().setDefaultButton(login);

        login.setActionCommand("LOG");
        login.addActionListener(this);

        room1.setActionCommand("RONE");
        room1.addActionListener(this);
        room1.setSelected(true);
        room1.setEnabled(false);

        room2.setActionCommand("RTWO");
        room2.addActionListener(this);

        uPanel.setLayout(new GridBagLayout());

        uName.setPreferredSize(new Dimension(200, 20));
        uName.setMaximumSize(new Dimension(200, 20));

        outer.add(uPanel, BorderLayout.CENTER);
        outer.add(bPanel, BorderLayout.SOUTH);

        uPanel.add(uLabel);
        uPanel.add(uName);

        bPanel.add(login);
        bPanel.add(room1);
        bPanel.add(room2);
        
        add(outer, BorderLayout.CENTER);
        
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension d = (tk.getScreenSize());

        setSize(d.width / 4, d.height / 4);
        setLocation((d.width / 2) - (this.getWidth() / 2), (d.height / 2) - (this.getHeight() / 2));

        setTitle("Login ver 0.01");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setVisible(true);

        setResizable(false);
    }

    
    @SuppressWarnings("unused")
    @Override
    public void actionPerformed(ActionEvent e) 
    {
        if(e.getActionCommand().equals("LOG"))
        {
           
            if(uName.getText().length() > 0)
            {
                
                if(room1.isSelected() == true)
                {
                    ChatInterface cClient = new ChatInterface(uName.getText(), 1234);
                }
                else if(room2.isSelected() == true)
                {
                    ChatInterface cClient = new ChatInterface(uName.getText(), 4321);
                }

                uName.setText("");
                this.setVisible(false);
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Username Empty!");
            }
        }

        if(e.getActionCommand().equals("RONE"))
        {
            room1.setSelected(true);
            room1.setEnabled(false);

            room2.setSelected(false);
            room2.setEnabled(true);
        }

        if(e.getActionCommand().equals("RTWO"))
        {
            room2.setSelected(true);
            room2.setEnabled(false);

            room1.setSelected(false);
            room1.setEnabled(true);
        }

    }
}
