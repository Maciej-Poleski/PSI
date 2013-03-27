import java.rmi.Remote;

public interface IClient extends Remote {

    /**
     * Klient nie może zmienić swojej nazwy. Liczy się pierwsza wersja.
     * @return Pseudonim klienta
     */
    String getName();

    void receiveMessage(String from, String message);

    void receiveClientsList(String[] clients);

    void receiveNewClient(String name);

    void forgetOldClient(String name);
}
