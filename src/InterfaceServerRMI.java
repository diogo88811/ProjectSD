import java.rmi.RemoteException;
import java.util.ArrayList;
import java.rmi.Remote;

public interface InterfaceServerRMI extends Remote{
    public void SaveRegistry(Pessoa string) throws RemoteException;
    public void criarEleicao(Eleicao eleicao) throws RemoteException;
	public void saveClients(String name, InterfaceClientRMI a) throws RemoteException;
    public void print_on_server(String s) throws RemoteException;
    public ArrayList<Pessoa> getEstudantes() throws RemoteException;
	public ArrayList<Pessoa> getDocentes() throws RemoteException;
	public ArrayList<Pessoa> getFuncionarios() throws RemoteException;
	public ArrayList<Eleicao> getEleicoes() throws RemoteException;
	public ArrayList<InterfaceClientRMI> getClients() throws RemoteException;
}