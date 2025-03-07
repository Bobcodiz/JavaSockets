import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    // Define the server port to connect to
    private static final int SERVER_PORT = 8080;

    public static void main(String[] args) {
        String SERVER_IP = null;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the server IP address: ");
        // Read the server IP address from the user input
        SERVER_IP = scanner.nextLine();

        // Try to establish a connection to the server
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);

             // Create a reader to read user input from the console
             BufferedReader userIn = new BufferedReader(new InputStreamReader(System.in));
             // Create a reader to read messages from the server
             BufferedReader serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             // Create a writer to send messages to the server
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            // Thread to listen for and print messages from the server
            Thread serverListener = new Thread(() -> {
                String serverMessage;
                try {
                    // Continuously read messages from the server
                    while ((serverMessage = serverIn.readLine()) != null) {
                        // Print each message received from the server
                        System.out.println(serverMessage);
                    }
                } catch (IOException e) {
                    // Handle any IO exceptions that occur while reading messages from the server
                    e.printStackTrace();
                }
            });
            // Start the server listener thread
            serverListener.start();

            // Read user input from the console and send it to the server
            String userMessage;
            while ((userMessage = userIn.readLine()) != null) {
                // Send each user input message to the server
                out.println(userMessage);
            }
        }catch (UnknownHostException e) {
            // Handling UnknownHostException
            System.err.println("Error: The IP address of the host could not be determined.");
            System.err.println("Please check the IP address and try again.");
        } catch (ConnectException e) {
            // Handling ConnectException
            System.err.println("Error: Unable to connect to the server. Connection refused.");
            System.err.println("Possible causes:");
            System.err.println("- The server is not running.");
            System.err.println("- The IP address is incorrect.");
            System.err.println("Please check the server status and the IP address then try again.");
        } catch (IOException e) {
            // Handle other IO exceptions
            System.err.println("Error: An I/O error occurred while trying to connect to the server.");
            e.printStackTrace();
        }
    }
}
