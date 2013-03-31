/*
* Interfejs okreslajacy jakie funkcje sa
* dostepne po stronie serwera. Takie metody
* musza móc rzucac RemoteException.
*/
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IServer extends Remote
{
    void registerClient(IClient client) throws RemoteException;
    void unregisterClient(IClient client) throws RemoteException;
	void sendMessage(String from, String to, String message) throws RemoteException;
    void sendToAll(String from, String message) throws RemoteException;

}

