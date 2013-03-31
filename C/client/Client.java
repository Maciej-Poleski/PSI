import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Client extends UnicastRemoteObject implements IClient {
    String nick;
    List<String> clients = new ArrayList<>();

    Client(String arg) throws RemoteException {
        nick = arg;
    }

    public static void main(String args[]) {

        try {
            String host = args[0];
            String nazwaSerwera = "rmi://" + host + "/Server";
            //pozyskanie namiastki serwer obiektu zdalnego  przez klienta
            IServer server = (IServer) Naming.lookup(nazwaSerwera);
            Client client = new Client(args[1]);
            server.registerClient(client);
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Jest połączenie\n");
            for (; ; ) {
                String line;
                line = input.readLine();
                if (line.startsWith("send ")) {
                    line = line.substring("send ".length());
                    int sp = line.indexOf(" ");
                    String nick = line.substring(0, sp);
                    String message = line.substring(sp + 1);
                    server.sendMessage(client.getName(), nick, message);
                }
                if (line.startsWith("list")) {
                    for (String c : client.clients) {
                        System.out.println(c);
                    }
                }
                if (line.startsWith("sendall ")) {
                    line = line.substring("sendall ".length());
                    server.sendToAll(client.getName(), line);
                }
                if (line.startsWith("quit")) {
                    break;
                }
            }
            server.unregisterClient(client);
        } catch (Exception e) {
            System.err.println("Wystąpił wyjątek:\n" + e.toString() + "\n" + e.getMessage() + "\n\n");
            e.printStackTrace();
        }
        System.out.println("Koniec programu :)");
    }

    @Override
    public String getName() {
        return nick;
    }

    @Override
    public void receiveMessage(String from, String message) {
        System.out.println("Wiadomość od " + from + ":\n" + message + "\n");
    }

    @Override
    public void receiveClientsList(String[] clients) {
        this.clients.clear();
        Collections.addAll(this.clients, clients);
    }

    @Override
    public void receiveNewClient(String name) {
        this.clients.add(name);
    }

    @Override
    public void forgetOldClient(String name) {
        this.clients.remove(name);
    }
}
