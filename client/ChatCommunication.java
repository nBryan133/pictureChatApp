package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class ChatCommunication implements Runnable
{

    private InetAddress host;
    private final int PORT;

    ChatInterface cInt;

    ChatCommunication(ChatInterface cInt, int PORT)
    {
        this.PORT = PORT;
        this.cInt = cInt;
    }

    private void chatFunction()
    {

        Socket link;

        try 
        {

            host = InetAddress.getLocalHost();

            link = new Socket(host, PORT);

            String serverMsg;

            try (BufferedReader input = new BufferedReader(new InputStreamReader(link.getInputStream(), StandardCharsets.UTF_8))) 
            {
                PrintWriter output = new PrintWriter(link.getOutputStream(), true, StandardCharsets.UTF_8);
                
                output.println(cInt.uName + " has joined the chat.");
                
                while (link.isConnected() && cInt.getRunning())
                {
                    
                    if(input.ready())
                    {
                        serverMsg = input.readLine();
                        
                        if(serverMsg != null)
                        {
                            if(!serverMsg.toLowerCase(Locale.ENGLISH).equals("/online"))
                            {
                                ChatInterface.appendToEditorPane(cInt.msgPanel, serverMsg);
                            }
                            else
                            {
                                output.println(cInt.uName);
                            }
                        }
                        
                    }

                    if(!cInt.message.isBlank())
                    {
                        if(!cInt.message.toLowerCase(Locale.ENGLISH).equals("/online"))
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
                    
                    if(!cInt.getRunning())
                    {
                        output.println(cInt.uName + " has left the chat.");
                        link.close();
                    }

                }
            }
        }
        catch(IOException ioEx)
        {
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

            try 
            {
                System.exit(1);
            } 
            catch (RuntimeException e) 
            {
                System.out.println(e);
            }
            
        }
        
        chatFunction();
    }
    
}
