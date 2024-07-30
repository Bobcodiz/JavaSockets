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
    public ClientHandler(Socket socket, Set<ClientHandler> clientHandlers) {
        this.socket = socket;
        this.clientHandlers = clientHandlers;
    }

    @Override
    public void run() {

        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(),true);

            out.println("Enter your name: ");
            clientName = in.readLine();


            synchronized (clientHandlers) {
                for (ClientHandler clientHandler : clientHandlers) {
                    clientHandler.setName(clientName);
                    clientHandler.out.println(clientName +" joined the chat");
                }
            }

            String message;
            while ((message = in.readLine()) != null) {
                if (message.startsWith("@")){
                    int splitIndex = message.indexOf(' ');
                    String target = message.substring(1,splitIndex);
                    String privateChat = message.substring(splitIndex + 1);
                    sendPrivateMessage(target,privateChat);
                }else {
                    broadcastMessage(message);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        finally {
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
    private void broadcastMessage(String message) {
        synchronized (clientHandlers) {
            for (ClientHandler client : clientHandlers) {
                client.out.println(clientName + ":" + message);
            }
        }
    }
}
