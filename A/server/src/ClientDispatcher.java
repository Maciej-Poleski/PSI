import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Set;

/**
 * User: Maciej Poleski
 * Date: 27.02.13
 * Time: 19:14
 */
public class ClientDispatcher implements Runnable {
    private final Server server;
    private String name;
    private BufferedReader inputStreamReader;
    private OutputStreamWriter outputStreamWriter;

    public ClientDispatcher(Server server, Socket socket) throws IOException {
        this.server = server;
        inputStreamReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
    }

    private synchronized void sendToSocket(String message) throws IOException {
        outputStreamWriter.write(message + "\n");
        outputStreamWriter.flush();
    }

    @Override
    public void run() {
        try {
            for (String line = inputStreamReader.readLine(); line != null; line = inputStreamReader.readLine()) {
                if (line.startsWith("NAME")) {
                    name = line.substring(4);
                    server.setNameOfClient(name, this);
                } else if (line.startsWith("GET CLIENTS")) {
                    Set<String> clientsNames = server.getClientsNames();
                    StringBuilder message = new StringBuilder("CLIENTS LIST");
                    for (String name : clientsNames) {
                        message.append("#").append(name);
                    }
                    sendToSocket(message.toString());
                } else if (line.startsWith("SEND MESSAGE")) {
                    String[] cnt = line.split("#");
                    server.sendMessage(name, cnt[1], cnt[2]);
                }
            }
        } catch (IOException ignored) {
        }
        server.forgetClient(this, name);
    }

    public void sendMessage(String from, String to, String message) throws IOException {
        if (name != null && from.equals(name) == false) {
            if (name.equals(to) || to.equals("ALL")) {
                sendToSocket("MESSAGE#" + from + "#" + message);
            }
        }
    }

    public void forgetPeerName(String name) throws IOException {
        sendToSocket("FORGET" + name);
    }

    public void addPeerName(String name) throws IOException {
        sendToSocket("ADD" + name);
    }
}
