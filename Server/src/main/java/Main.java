public class Main {
    public static void main(String[] Args) {
        if (Args.length != 1) {
            System.err.println("You need to pass the port number as the first argument.");
            System.exit(0);
        }
        try {
            int port = Integer.parseInt(Args[0]);
            Server serv = new Server(port);
            serv.runServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
