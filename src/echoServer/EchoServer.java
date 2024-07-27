package echoServer;

import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {

    public static void main(String[] args) {

      try {
          System.out.println("Starting EchoServer");
          ServerSocket serverSocket = new ServerSocket(2000);
          System.out.println("Server started");
          Socket soc = serverSocket.accept();
          System.out.println("Client connected");
      }catch (Exception e){
          e.printStackTrace();
      }

    }
}
