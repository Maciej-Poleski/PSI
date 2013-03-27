/*
* Klasa interpretujaca interface
* sluzacy realizacji polaczenia
*/

import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Musimy rozszerzac UnicastRemoteObject
public class Server extends UnicastRemoteObject implements IServer {

    Map<String, IClient> clientsByNames = new HashMap<String, IClient>();

    //Inplementacja konstruktora by rzucal
    //odpowiedni wyjatek
    Server() throws RemoteException {
        super(); //Mozna choc nie jest to konieczne :)
    }

    public static void main(String[] args) {
        //domyslnie Java nie instaluje menadzera bezpieczenstwa,
        // RMI Class Loader nie pozwala sciagnac klasy jezeli nie jest zainstalowany SecurityManager
        //w tej aplikacji serwer bedzie sciagal klase Dane, Manager bezpieczenstwa jest wiec wymagany
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new RMISecurityManager());
        }
        //Gdy nie podamy portu - bedzie to 1099
        String nazwaSerwisu = "//127.0.0.1/Server";
        try {
            IServer serwer = new Server();
            Naming.rebind(nazwaSerwisu, serwer);
            //wyeksportowanie referencji zdalnego obiektu do rejestru
            //rejestr musi sciagnac klase ServerInt.class
            System.out.println("Server ruszyl");
        } catch (Exception e) {
            System.err.println("Wyjatek serwera: " +
                    e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void registerClient(IClient client) throws RemoteException {
        String name = client.getName();
        clientsByNames.put(name, client);
        client.receiveClientsList((String[]) clientsByNames.keySet().toArray());
    }

    @Override
    public synchronized void sendMessage(String from, String to, String message) throws RemoteException {
        clientsByNames.get(to).receiveMessage(from, message);
    }

    @Override
    public synchronized void sendToAll(String from, String message) throws RemoteException {
        List<String> clients = new ArrayList<>(clientsByNames.keySet());
        for (String client : clients) {
            sendMessage(from, client, message);
        }
    }
}
