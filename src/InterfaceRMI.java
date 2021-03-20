import java.rmi.RemoteException;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceRMI extends Remote{
    void SaveRegistry(String string) throws RemoteException;
    void criarEleicao(String string) throws RemoteException;
    
}