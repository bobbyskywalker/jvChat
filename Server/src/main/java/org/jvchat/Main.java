package org.jvchat;

import org.jvchat.server.Server;
import org.tinylog.Logger;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            Logger.warn("You need to pass the port number as the first argument.");
            System.exit(0);
        }
        try {
            int port = Integer.parseInt(args[0]);
            Server serv = new Server(port);
            serv.runServer();
        } catch (Exception e) {
            Logger.error("System failure, exception: {}", (Object) e.getStackTrace());
        }
    }
}
