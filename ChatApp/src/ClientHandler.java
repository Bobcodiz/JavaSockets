import java.io.*;
import java.net.Socket;
import java.util.Set;

public class ClientHandler extends Thread{
    private String clientName;
    private Socket socket;
    private final Set<ClientHandler> clientHandlers;
    private PrintWriter out;
    private BufferedReader in;
    private InputStreamReader reader;
    private OutputStreamWriter writer;

    // Constructor to initialize the socket and shared client handlers set
    public ClientHandler(Socket socket, Set<ClientHandler> clientHandlers) {
        this.socket = socket;
        this.clientHandlers = clientHandlers;
    }

    @Override
    public void run() {

        try {
            // Initialize input and output streams for communication with the client
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(),true);

            // Prompt the client to enter their name
            out.println("Enter your name: ");
            clientName = in.readLine();

            // Notify all clients that a new client has joined the chat
            synchronized (clientHandlers) {
                for (ClientHandler clientHandler : clientHandlers) {
                    clientHandler.setName(clientName);
                    clientHandler.out.println(clientName +" joined the chat");
                }
            }

            String message;

            // Continuously read messages from the client
            while ((message = in.readLine()) != null) {
                if (message.startsWith("@")){

                    // Handle private messages
                    int splitIndex = message.indexOf(' ');
                    String target = message.substring(1,splitIndex);
                    String privateChat = message.substring(splitIndex + 1);
                    sendPrivateMessage(target,privateChat);
                }
                else {
                    // Broadcast public messages to all clients
                    broadcastMessage(message);
                }
            }
        } catch (IOException e) {
            // Handle IO exceptions that may occur during client communication
            throw new RuntimeException(e);
        }
        finally {
            // Cleanup and notify other clients when a client disconnects
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            synchronized (clientHandlers) {
                clientHandlers.remove(this);
                for (ClientHandler clientHandler : clientHandlers) {
                    clientHandler.setName(clientName);
                    clientHandler.out.println(clientName + " left the chat");
                }
            }
        }
    }
    // Method to send a private message to a specific client
    private void sendPrivateMessage(String targetName, String message) throws IOException {
        synchronized (clientHandlers) {
            for (ClientHandler client : clientHandlers) {
                if (client.clientName.equals(targetName)) {
                    client.out.println("(Private) " + clientName + ":" + message);
                    break;
                }
            }
        }
    }
    // Method to broadcast a message to all connected clients
    private void broadcastMessage(String message) {
        synchronized (clientHandlers) {
            for (ClientHandler client : clientHandlers) {
                client.out.println(clientName + ":" + message);
            }
        }
    }
}
