
import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * User: Maciej Poleski
 * Date: 28.02.13
 * Time: 20:50
 */
class SocketService implements Runnable {
    private final String name;
    private final DefaultListModel<String> usersListModel = new DefaultListModel<>();
    private Socket socket;
    private OutputStreamWriter outputStreamWriter;
    private UserInterface ui;

    public SocketService(String name) {
        this.name = name;
    }

    public void setUi(UserInterface ui) {
        this.ui = ui;
    }

    private synchronized void sendToSocket(String message) throws IOException {
        outputStreamWriter.write(message + "\n");
        outputStreamWriter.flush();
    }

    private synchronized void addPeerName(String name) {
        if (!usersListModel.contains(name))
            usersListModel.addElement(name);
    }

    private synchronized void forgetPeerName(String name) {
        usersListModel.removeElement(name);
    }

    {
        usersListModel.addElement("ALL");
    }

    public synchronized ListModel<String> getUsersListModel() {
        return usersListModel;
    }

    public synchronized void shutdown() {
        try {
            if (socket != null)
                socket.close();
        } catch (IOException ignored) {
        }
    }

    public synchronized void sendMessage(String to, String message) throws IOException {
        if (to == null || message == null || message.isEmpty())
            throw new IllegalArgumentException();
        sendToSocket("SEND MESSAGE#" + to + "#" + message);
    }

    @Override
    public void run() {
        try {
            socket = new Socket((String) null, 9000);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
            sendToSocket("NAME" + name);
            sendToSocket("GET CLIENTS");
            for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
                if (line.startsWith("CLIENTS LIST")) {
                    String[] clients = line.split("#");
                    for (int i = 1; i < clients.length; ++i)
                        addPeerName(clients[i]);
                } else if (line.startsWith("ADD")) {
                    addPeerName(line.substring(3));
                } else if (line.startsWith("FORGET")) {
                    forgetPeerName(line.substring(6));
                } else if (line.startsWith("MESSAGE")) {
                    final String[] cnt = line.split("#");
                    if (ui != null)
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                ui.displayMessage(cnt[1], cnt[2]);
                            }
                        });
                } else {
                    System.out.println("Unknown command: " + line);
                }
            }
        } catch (IOException ignored) {
        }
    }

    public String getName() {
        return name;
    }
}
