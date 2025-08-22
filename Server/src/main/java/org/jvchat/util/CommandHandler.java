package org.jvchat.util;

import org.jvchat.client.Client;
import org.jvchat.server.Server;

public class CommandHandler {

    private CommandHandler() {}

    /* Utility function to strip N tokens from the beginning of a string
    * @param line_split: split string
    * @param start_from: the starting token for the final string
    * @return: tokens concatenated into a single string, space separated
    */
    private static String stripCmdFromMsg(String[] lineSplit, int startFrom) {
        StringBuilder res = new StringBuilder();
        for (int i = startFrom; i < lineSplit.length; i++) {
            res.append(lineSplit[i]);
            if (i != lineSplit.length - 1)
                res.append(" ");
        }
        return res.toString();
    }

    public static void execCmd(String line, Client requester, Server serv) {
        String[] res = line.split(" ");
        if (res.length < 2) {
            requester.sendMsg("\u001B[31m[SERVER] Request Error: Too few command arguments.\u001B[0m");
            return ;
        }

        String msg;
        switch (res[0]) {
            case "privmsg":
                Client recipient = serv.getClientByUsername(res[1]);
                if (recipient == null) {
                    requester.sendMsg("\u001B[31m[SERVER] Request Error: No such user " + res[1] + "\u001B[0m");
                } else {
                    msg = stripCmdFromMsg(res, 2);
                    recipient.sendMsg("\u001B[33m" + requester.getUsername() + "\u001B[0m: " + msg);
                }
                break;
            case "msg":
                msg = stripCmdFromMsg(res, 1);
                serv.broadcastToAll(requester, "\u001B[33m" + requester.getUsername() + "\u001B[0m: " + msg);
                break;
            case "username":
                String newUsername = res[1];
                requester.setUsername(newUsername);
                requester.sendMsg("\u001B[34m[SERVER] Username successfully changed to: " + newUsername + "\u001B[0m");
                break;
            default:
                requester.sendMsg("[\u001B[31mSERVER] Invalid command.\u001B[0m");
        }
    }
}
