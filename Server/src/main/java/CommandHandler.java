public class CommandHandler {

    /* Utility function to strip N tokens from the beggining of a string
    * @param line_split: split string
    * @param start_from: the starting token for the final string
    * @return: tokens concatenated into a single string, space separated
    */
    private static String stripCmdFromMsg(String[] line_split, int start_from) {
        StringBuilder res = new StringBuilder();
        for (int i = start_from; i < line_split.length; i++) {
            res.append(line_split[i]);
            if (i != line_split.length - 1)
                res.append(" ");
        }
        return res.toString();
    }

    public static void parseCmd(String line, Client requester, Server serv) {
        String[] res = line.split(" ");
        if (res.length < 2) {
            requester.sendMsg("[SERVER] Request Error: Too few command arguments.");
            return ;
        }

        String msg;
        switch (res[0]) {
            case "privmsg":
                Client recipient = serv.getClientByUsername(res[1]);
                if (recipient == null) {
                    requester.sendMsg("[SERVER] Request Error: No such user " + res[1]);
                } else {
                    msg = stripCmdFromMsg(res, 2);
                    recipient.sendMsg(requester.getUsername() + ": " + msg);
                }
                break;
            case "msg":
                msg = stripCmdFromMsg(res, 0);
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
