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

    public ClientHandler(Socket socket, Set<ClientHandler> clientHandlers) throws IOException {
        this.socket = socket;
        this.clientHandlers = clientHandlers;

        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(),true);

        out.println("Enter your name: ");
        clientName = in.readLine();

    }

    @Override
    public void run() {

        try {
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


    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public Set<ClientHandler> getClientHandlers() {
        return clientHandlers;
    }

    public PrintWriter getOut() {
        return out;
    }

    public void setOut(PrintWriter out) {
        this.out = out;
    }

    public BufferedReader getIn() {
        return in;
    }

    public void setIn(BufferedReader in) {
        this.in = in;
    }

    public InputStreamReader getReader() {
        return reader;
    }

    public void setReader(InputStreamReader reader) {
        this.reader = reader;
    }

    public OutputStreamWriter getWriter() {
        return writer;
    }

    public void setWriter(OutputStreamWriter writer) {
        this.writer = writer;
    }
}
