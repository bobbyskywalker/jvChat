import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ClientHandler extends Thread {

    private final Client client;
    private final Server server;

    public ClientHandler(Client client, Server server) {
        this.client = client;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            BufferedReader in_buf = new BufferedReader(new InputStreamReader(client.getSocket().getInputStream()));

            client.sendMsg(server.signature + "\n\nWelcome to jvChat " + client.getUsername() +
                    "!\nTo change your username send \"username <new username>\"");

            String line;
            while ((line = in_buf.readLine()) != null) {
                server.broadcastToAll(client, client.getUsername() + ": " + line);
            }
            System.out.println("\u001B[31m" + client.getUsername() + " DISCONNECTED\u001B[0m");
            server.removeClient(client);
        } catch (IOException e) {
            server.removeClient(client);
            e.printStackTrace();
        }
    }
}
