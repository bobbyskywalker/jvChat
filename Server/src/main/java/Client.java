import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.UUID;

public class Client {
    private String username;
    private final String ip_address;
    private Socket socket = null;
    public PrintWriter out;

    public Client(String ip_address, Socket socket) throws IOException {
        this.username = makeDefaultUsername();
        this.ip_address = ip_address;
        this.socket = socket;
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    private static String makeDefaultUsername() {
        return "User-" + UUID.randomUUID().toString().substring(0, 8);
    }

    public void sendMsg(String msg) {
        out.println(msg);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIp_address() {
        return ip_address;
    }

    public Socket getSocket() {
        return socket;
    }
}
