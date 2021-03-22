import java.rmi.RemoteException;
import java.rmi.Remote;

public interface InterfaceServerRMI extends Remote{
    void SaveRegistry(String string) throws RemoteException;
    void criarEleicao(String string) throws RemoteException;
	public void saveClients(String name, InterfaceClientRMI a) throws RemoteException;
    public void print_on_server(String s) throws RemoteException;
    }