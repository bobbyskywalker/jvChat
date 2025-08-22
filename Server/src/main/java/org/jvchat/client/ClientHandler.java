package org.jvchat.client;

import org.jvchat.util.CommandHandler;
import org.jvchat.server.Server;

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
            BufferedReader inBuf = new BufferedReader(new InputStreamReader(client.getSocket().getInputStream()));

            client.sendMsg(
                    Server.SIGNATURE +
                            "\n\n\u001B[36mWelcome to jvChat " + client.getUsername() + "!\u001B[0m\n" +
                            "* To change your username send \"username <new username>\" \n" +
                            "* To send a message to a particular client, send \"privmsg <recipient_username> <message>\"\n" +
                            "* To send a message to all clients, send \"msg <message>\"\n"
            );

            String line;
            while ((line = inBuf.readLine()) != null) {
                CommandHandler.execCmd(line, client, server);
            }
            System.out.println("\u001B[31m" + client.getUsername() + " DISCONNECTED\u001B[0m");
            server.removeClient(client);
        } catch (IOException e) {
            server.removeClient(client);
            e.printStackTrace();
        }
    }
}
