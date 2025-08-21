import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Server {
    private ServerSocket servSocket;
    private List<Client> clients = new ArrayList<>();
    private volatile boolean running = true;
    public static final String SIGNATURE =
        """
            +---------------------------------------------+
            |       _          ______ __            _     |
            |      (_)       .' ___  [  |          / |_   |
            |      __ _   __/ .'   \\_|| |--.  ,--.`| |-'  |
            |     [  [ \\ [  | |       | .-. |`'_\\ :| |    |
            |   _  | |\\ \\/ /\\ `.___.'\\| | | |// | || |,   |
            |  [ \\_| | \\__/  `.____ .[___]|__\\'-;__\\__/   |
            |   \\____/                                    |
            +---------------------------------------------+
        """;

    public Server(int port) {
        try {
            servSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace(); // logging tbd
        }
    }

    private Client registerClient(Socket clientSocket) throws IOException {
        Client client = new Client(clientSocket.getInetAddress().getHostAddress(), clientSocket);
        clients.add(client);
        return client;
    }

    public void removeClient(Client client) {
        clients.remove(client);
    }

    public Client getClientByUsername(String username) {
        for (Client cur : clients) {
            if (Objects.equals(cur.getUsername(), username)) {
                return cur;
            }
        }
        return null;
    }

    public void broadcastToAll(Client excluded, String message) {
        for (Client current : clients) {
            if (current == excluded)
                continue;
            current.sendMsg(message);
        }
    }

    private void closeServer() throws IOException {
        running = false;
        servSocket.close();
    }

    public void runServer() throws IOException {
        while (running) {
            try {
                Socket clientSocket = servSocket.accept();
                Client client = registerClient(clientSocket);
                ClientHandler handler = new ClientHandler(client, this);
                handler.start();
            } catch (IOException e) {
                if (running)
                    System.err.println("Error: " + e.getMessage());
            }
        }
    }
}
