/*
* Interfejs okreslajacy jakie funkcje sa
* dostepne po stronie serwera. Takie metody
* musza móc rzucac RemoteException.
*/
import java.rmi.Remote;
import java.rmi.RemoteException;
public interface SerwerInt extends Remote 
{
	// Wszystkie metody przez domniemanie sa public
	void rozwiaz(DaneInt d) throws RemoteException;
	int ilosc() throws RemoteException;
	double[] wynik() throws RemoteException;
}

