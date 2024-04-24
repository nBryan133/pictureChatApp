package server;

public class ChatServer {
    private static final int PORT1 = 1234;
    private static final int PORT2 = 4321;

    public static void main(String[] args) throws InterruptedException
    {
        Thread room1 = new Thread(new RoomHandler(PORT1));
        Thread room2 = new Thread(new RoomHandler(PORT2));

        room1.start();
        room2.start();

        while(true)
        {
            if(!room1.isAlive() || !room2.isAlive())
            {
                System.exit(0);
            }
        }
    }

    
}
