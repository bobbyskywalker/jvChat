import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Server {
    private ServerSocket servSocket;
    private static int port;
    private List<Client> clients = new ArrayList<>();
    public String signature = "+---------------------------------------------+\n" +
            "|       _          ______ __            _     |\n" +
            "|      (_)       .' ___  [  |          / |_   |\n" +
            "|      __ _   __/ .'   \\_|| |--.  ,--.`| |-'  |\n" +
            "|     [  [ \\ [  | |       | .-. |`'_\\ :| |    |\n" +
            "|   _  | |\\ \\/ /\\ `.___.'\\| | | |// | || |,   |\n" +
            "|  [ \\_| | \\__/  `.____ .[___]|__\\'-;__\\__/   |\n" +
            "|   \\____/                                    |\n" +
            "+---------------------------------------------+";

    public Server(int port) {
        Server.port = port;
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

    public void runServer() throws IOException {
        while (true) {
            Socket clientSocket = servSocket.accept();
            Client client = registerClient(clientSocket);
            ClientHandler handler = new ClientHandler(client, this);
            handler.start();
        }
    }
}
