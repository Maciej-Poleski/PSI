import java.io.IOException;
import java.net.ServerSocket;
import java.util.*;

/**
 * User: Maciej Poleski
 * Date: 27.02.13
 * Time: 19:14
 */
public class Server implements Runnable {
    private final ServerSocket server;
    private List<ClientDispatcher> clientDispatcherList = new ArrayList<ClientDispatcher>();
    private Map<String, ClientDispatcher> nameToClientDispatcherHashMap = new HashMap<String, ClientDispatcher>();

    public Server(int port) throws IOException {
        server = new ServerSocket(port);
    }

    private synchronized void addClientDispatcher(ClientDispatcher clientDispatcher) {
        clientDispatcherList.add(clientDispatcher);
    }

    public synchronized void setNameOfClient(String name, ClientDispatcher clientDispatcher) {
        nameToClientDispatcherHashMap.put(name, clientDispatcher);
        namedClientAdded(name);
    }

    public synchronized void sendMessage(String to, String message) {
        for (ClientDispatcher clientDispatcher : clientDispatcherList) {
            try {
                clientDispatcher.sendMessage(to, message);
            } catch (IOException ignored) {
            }
        }
    }

    public synchronized void forgetClient(ClientDispatcher clientDispatcher, String name) {
        clientDispatcherList.remove(clientDispatcher);
        if (name != null) {
            nameToClientDispatcherHashMap.remove(name);
            namedClientRemoved(name);
        }
    }

    public synchronized Set<String> getClientsNames() {
        return nameToClientDispatcherHashMap.keySet();
    }

    private synchronized void namedClientRemoved(String name) {
        for (ClientDispatcher clientDispatcher : clientDispatcherList)
            try {
                clientDispatcher.forgetPeerName(name);
            } catch (IOException ignored) {
            }
    }

    private synchronized void namedClientAdded(String name) {
        for (ClientDispatcher clientDispatcher : clientDispatcherList) {
            try {
                clientDispatcher.addPeerName(name);
            } catch (IOException ignored) {
            }
        }
    }

    @Override
    public void run() {
        for (; ; ) {
            try {
                ClientDispatcher clientDispatcher = new ClientDispatcher(this, server.accept());
                addClientDispatcher(clientDispatcher);
                new Thread(clientDispatcher).start();
            } catch (IOException ignored) {
            }
        }
    }
}
