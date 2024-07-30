import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Server {

     // The port number on which the server will listen for client connections
     static int SERVER_PORT = 8080;

     // A synchronized set to keep track of all connected client handlers
     private static final Set<ClientHandler> clientHandlers = Collections.synchronizedSet(new HashSet<>());

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
            System.out.println("Waiting for clients..");

            // Continuously listen for new client connections
            while (true) {
                // Accept a new client connection
                Socket socket = serverSocket.accept();
                System.out.println("Client connected..");

                // Create a new ClientHandler for the connected client
                ClientHandler clientHandler = new ClientHandler(socket, clientHandlers);

                // Add the new client handler to the set of client handlers
                clientHandlers.add(clientHandler);

                // Start a new thread to handle the client's communication
                new Thread(clientHandler).start();

            }

        } catch (IOException e) {
            // Handle IO exceptions that may occur during server operation
            throw new RuntimeException(e);
        }
    }
}
