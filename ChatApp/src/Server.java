import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Server {

    static int SERVER_PORT = 8080;
    private static final Set<ClientHandler> clientHandlers = Collections.synchronizedSet(new HashSet<>());

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
            System.out.println("Waiting for clients..");


            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected..");
                ClientHandler clientHandler = new ClientHandler(socket, clientHandlers);

                for (ClientHandler client : clientHandlers){
                    if(client.getName().equals(clientHandler.getClientName())){
                        clientHandler.getOut().println("Username already exist.\nExiting the session.");
                        return;
                    }
                }

                clientHandlers.add(clientHandler);
                new Thread(clientHandler).start();

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}