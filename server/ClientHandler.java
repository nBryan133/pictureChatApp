package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Set;

public class ClientHandler implements Runnable
{
    private final Socket cSocket;
    private final BufferedReader reader;
    private final Set<PrintWriter> clients;
    private final PrintWriter sender;


    ClientHandler(Socket cSocket, Set<PrintWriter> clients, PrintWriter sender) throws IOException
    {
        this.cSocket = cSocket;
        this.clients = clients;
        this.sender = sender;

        reader = new BufferedReader(new InputStreamReader(cSocket.getInputStream()));
    }

    private void broadcast(String msg)
    {
        for(PrintWriter writer : clients)
        {
            if(writer != null && writer != sender)
            {
                writer.println(msg);
            }
        }
    }

    @Override
    public void run() 
    {
        
        try
        {
            String cMessage;

            while((cMessage = reader.readLine()) != null)
            {
                broadcast(cMessage);
            }

            reader.close();

        } catch (IOException e) {
            
            e.printStackTrace();
        }
    }
    
}
