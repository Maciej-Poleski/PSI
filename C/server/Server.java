/*
* Klasa interpretujaca interface
* sluzacy realizacji polaczenia
*/

import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

//Musimy rozszerzac UnicastRemoteObject
public class Server extends UnicastRemoteObject implements IServer {

    Map<String, IClient> clientsByNames = new HashMap<>();

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
        newClientRegistered(name);
        clientsByNames.put(name, client);
        String[] clients = new String[clientsByNames.size()];
        Set<String> clientsFromMap = clientsByNames.keySet();
        int i = 0;
        for (String s : clientsFromMap) {
            clients[i++] = s;
        }
        client.receiveClientsList(clients);
    }

    @Override
    public void unregisterClient(IClient client) throws RemoteException {
        try {
            String name = client.getName();
            clientsByNames.remove(name);
            oldClientUnregistered(name);
        } catch (Exception ignored) {
            // Być może nie uda się uzyskać nazwy - w takim razie zostanie usunięty leniwie
        }
    }

    public void unregisterClient(String name) {
        clientsByNames.remove(name);
        oldClientUnregistered(name);
        // to zawsze się uda
    }

    private void oldClientUnregistered(String name) {
        for (String client : clientsByNames.keySet()) {
            try {
                clientsByNames.get(client).forgetOldClient(name);
            } catch (RemoteException e) {
                unregisterClient(client);
            }
        }
    }

    private void newClientRegistered(String name) {
        for (String client : clientsByNames.keySet())
            try {
                clientsByNames.get(client).receiveNewClient(name);
            } catch (Exception e) {
                unregisterClient(client);
            }

    }

    @Override
    public synchronized void sendMessage(String from, String to, String message) throws RemoteException {
        if (clientsByNames.get(to) == null) {
            sendMessage("SERVER", from, "Nie ma klienta o nicku " + to);
            return;
        }
        try {
            clientsByNames.get(to).receiveMessage(from, message);
        } catch (Exception e) {
            unregisterClient(to);
            sendMessage("SERVER", from, "Nie ma klienta o nicku " + to);
        }
    }

    @Override
    public synchronized void sendToAll(String from, String message) throws RemoteException {
        List<String> clients = new ArrayList<>(clientsByNames.keySet());
        for (String client : clients) {
            sendMessage(from, client, message);
        }
    }
}
