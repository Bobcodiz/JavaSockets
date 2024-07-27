package echoServer;

import java.net.Socket;

public class EchoClient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 2000);

            System.out.println("client connected");


        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
