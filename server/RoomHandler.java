package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class RoomHandler implements Runnable
{
    private final int PORT;
    private final Set<PrintWriter> clients = new HashSet<>();

    Thread clientThreads[];

    RoomHandler(int PORT)
    {
        this.PORT = PORT;
    }

    @Override
    public void run() 
    {
        ServerSocket serverSocket;

        try
        {
            serverSocket = new ServerSocket(PORT); 

            while(true)
            {
                Socket link = serverSocket.accept();

                System.out.println("New client connected: " + PORT);

                PrintWriter writer = new PrintWriter(link.getOutputStream(), true);
                clients.add(writer);

                Thread clientThread = new Thread(new ClientHandler(link, clients, writer));
                clientThread.start();

            }
        }
        catch(IOException ioEx)
        {
            System.out.println( "Unable to attach to port!");

            System.exit(1);
        }        
    }
    
}
