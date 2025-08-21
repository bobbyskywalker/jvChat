public class CommandHandler {

    private static String stripCmdFromMsg(String[] line_split) {
        StringBuilder res = new StringBuilder();
        for (int i = 1; i < line_split.length; i++) {
            res.append(line_split[i]);
        }
        return res.toString();
    }

    public static void parseCmd(String line, Client requester, Server serv) {
        String[] res = line.split(" ");
        if (res.length < 2) {
            requester.sendMsg("[SERVER] Request Error: Too few command arguments.");
            return ;
        }
        String msg = stripCmdFromMsg(res);
        switch (res[0]) {
            case "privmsg":
                Client recipient = serv.getClientByUsername(res[1]);
                if (recipient == null) {
                    requester.sendMsg("[SERVER] Request Error: No such user " + res[1]);
                } else {
                    recipient.sendMsg(msg);
                }
                break;
            case "msg":
                serv.broadcastToAll(requester, requester.getUsername() + ": " + msg);
                break;
            case "username":
                String newUsername = res[1];
                requester.setUsername(newUsername);
                requester.sendMsg("[SERVER] Username successfully changed to: " + newUsername);
                break;
            default:
                requester.sendMsg("[SERVER] Invalid command.");
        }
    }
}
