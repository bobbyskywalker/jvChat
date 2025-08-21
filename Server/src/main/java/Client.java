import java.net.Socket;
import java.util.UUID;

public class Client {
    private String username;
    private String ip_address;
    private Socket socket = null;

    public Client(String ip_address, Socket socket) {
        this.username = makeDefaultUsername();
        this.ip_address = ip_address;
        this.socket = socket;
    }

    private static String makeDefaultUsername() {
        return "User-" + UUID.randomUUID().toString().substring(0, 8);
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

    public void setIp_address(String ip_address) {
        this.ip_address = ip_address;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
