package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatCommunication implements Runnable
{

    private static InetAddress host;
    private static int PORT;

    static ChatInterface cInt;

    public static String msg;

    ChatCommunication(ChatInterface cInt, int PORT)
    {
        this.PORT = PORT;
        this.cInt = cInt;
    }

    private static void chatFunction()
    {

        Socket link = null;

        msg = "";

        try 
        {

            host = InetAddress.getLocalHost();

            link = new Socket(host, PORT);
            
            BufferedReader input = new BufferedReader(new InputStreamReader(link.getInputStream()));
            
            PrintWriter output = new PrintWriter(link.getOutputStream(),true); 

            output.println(cInt.uName + " has joined the chat.");

            while (link.isConnected() && cInt.running)
            {

                if(input.ready())
                {

                    String serverMsg = input.readLine();
                    if(!serverMsg.toLowerCase().equals("/online"))
                    {
                        ChatInterface.appendToEditorPane(cInt.msgPanel, serverMsg);
                    }
                    else
                    {
                        output.println(cInt.uName);
                    }
                    
                }

                if(!cInt.message.isBlank())
                {
                    if(!cInt.message.toLowerCase().equals("/online"))
                    {
                        output.println((cInt.uName + ">" + cInt.message));
                    }
                    else
                    {
                        ChatInterface.appendToEditorPane(cInt.msgPanel, "Online:");
                        ChatInterface.appendToEditorPane(cInt.msgPanel, cInt.uName);
                        output.println(cInt.message);
                    }

                    cInt.message = "";
                }

                if(cInt.running == false)
                {
                    output.println(cInt.uName + " has left the chat.");
                    link.close();
                }
                
            }

            input.close();
        }
        catch(IOException ioEx)
        {
            ioEx.printStackTrace();
        } 
    }


    @Override
    public void run() 
    {

        try
        {
            this.host = InetAddress.getLocalHost();

        }
        catch(UnknownHostException uhEx)
        {
            System.out.println("Host ID not found!");
            System.exit(1);
        }
        
        chatFunction();
    }
    
}
