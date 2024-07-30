import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Server {

     static int SERVER_PORT = 8080;
    private static final Set<ClientHandler> clientHandlers = Collections.synchronizedSet(new HashSet<>());


    }
