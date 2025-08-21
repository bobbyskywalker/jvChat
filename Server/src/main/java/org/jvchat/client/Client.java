package org.jvchat.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.UUID;

public class Client {
    private String username;
    private final String ipAddress;
    private Socket socket = null;
    public final PrintWriter out;

    public Client(String ipAddress, Socket socket) throws IOException {
        this.username = makeDefaultUsername();
        this.ipAddress = ipAddress;
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

    public String getIpAddress() {
        return ipAddress;
    }

    public Socket getSocket() {
        return socket;
    }
}
