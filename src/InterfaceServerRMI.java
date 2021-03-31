import java.io.IOException;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.ArrayList;
import java.rmi.Remote;

public interface InterfaceServerRMI extends Remote{
    public void SaveRegistry(Pessoa string) throws RemoteException;
    public void criarEleicao(Eleicao eleicao) throws RemoteException;
	public void saveClients(String name, InterfaceClientRMI a) throws RemoteException;
	public void saveAdmin(String name, InterfaceClientRMI a) throws RemoteException ;
    public void print_on_server(String s) throws RemoteException;
    public ArrayList<Pessoa> getEstudantes() throws RemoteException;
	public ArrayList<Pessoa> getDocentes() throws RemoteException;
	public ArrayList<Pessoa> getFuncionarios() throws RemoteException;
	public ArrayList<Eleicao> getEleicoes() throws RemoteException;
	public ArrayList<InterfaceClientRMI> getClients() throws RemoteException;
	public ArrayList<InterfaceClientRMI> getAdminClients() throws RemoteException;
	public boolean verifyUser(String nome, String ccNumber, String password)throws RemoteException;
	public boolean verifyLogin(String nome, String ccNumber) throws RemoteException ;
	public void saveVotes(String eleicao, String lista) throws RemoteException;
	public void loadDataElection() throws RemoteException;
	public void notifyClient(String name, String tag) throws RemoteException;
	public void gerirEleicao(Lista l, int eleNum,int opt,int indexLi)throws RemoteException, IOException;
	public void alteraEleicao(Eleicao e, int numEle) throws RemoteException, IOException;
	public void stateOfElections() throws RemoteException, ParseException;
}