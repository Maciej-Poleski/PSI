import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IClient extends Remote {

    /**
     * Klient nie może zmienić swojej nazwy. Liczy się pierwsza wersja.
     * @return Pseudonim klienta
     */
    String getName() throws RemoteException;

    void receiveMessage(String from, String message) throws RemoteException;

    void receiveClientsList(String[] clients) throws RemoteException;

    void receiveNewClient(String name) throws RemoteException;

    void forgetOldClient(String name) throws RemoteException;
}
