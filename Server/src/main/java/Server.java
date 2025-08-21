import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private ServerSocket servSocket;
    private DataInputStream instream;
    private static final int port = 3000;
    private List<Client> clients = new ArrayList<>();

    public Server() {
        try {
            servSocket = new ServerSocket(port);
            initConnections();
        } catch (IOException e) {
            e.printStackTrace(); // logging tbd
        }
    }

    private void initConnections() throws IOException {
        Socket clientSocket = servSocket.accept();
        instream = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
        Client client = new Client(clientSocket.getInetAddress().getHostAddress(), clientSocket);
        clients.add(client);
        readMsg();
        close();
    }

    private void close() throws IOException {
        instream.close();
        servSocket.close();
    }

    private void readMsg() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
    }

    public static void main(String[] Args) {
        new Server();
    }
}
