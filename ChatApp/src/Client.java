import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    private static final int SERVER_PORT = 8080;

    public static void main(String[] args) {
        String SERVER_IP = null;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the server ip address: ");
        SERVER_IP = scanner.nextLine();

        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);


             BufferedReader userIn = new BufferedReader(new InputStreamReader(System.in));
             BufferedReader serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            Thread serverListener = new Thread(() -> {
                String serverMessage;
                try {
                    while ((serverMessage = serverIn.readLine()) != null) {
                        System.out.println(serverMessage);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            serverListener.start();

            String userMessage;
            while ((userMessage = userIn.readLine()) != null) {
                out.println(userMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
