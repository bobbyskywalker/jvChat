package org.jvchat.server;

import org.jvchat.client.ClientHandler;
import org.jvchat.client.Client;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.tinylog.Logger;


public class Server {
    private ServerSocket servSocket;
    private final List<Client> clients = new ArrayList<>();
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
        } catch (IOException _) {
            Logger.error("System: failed to create the server socket.");
        }
    }

    private Client registerClient(Socket clientSocket) throws IOException {
        var address = clientSocket.getInetAddress().getHostAddress();
        Client client = new Client(address, clientSocket);
        clients.add(client);

        Logger.info("Client [{}] connected from {}", client.getUsername(), address);

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

    public void runServer() {
        while (running) {
            try {
                Socket clientSocket = servSocket.accept();
                Client client = registerClient(clientSocket);
                ClientHandler handler = new ClientHandler(client, this);
                handler.start();
            } catch (IOException e) {
                if (running)
                    Logger.error("System fail, exception: {}", (Object) e.getStackTrace());
            }
        }
    }
}
