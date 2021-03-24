import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;
import java.util.ArrayList;
import java.util.Arrays;

public class ServerRMI extends UnicastRemoteObject implements InterfaceServerRMI {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	ArrayList<Pessoa> Estudantes = new ArrayList<Pessoa>(); 
	ArrayList<Pessoa> Docentes = new ArrayList<Pessoa>(); 
	ArrayList<Pessoa> Funcionarios = new ArrayList<Pessoa>(); 
	ArrayList<Eleicao> eleicoes = new ArrayList<Eleicao>();
	ArrayList<InterfaceClientRMI> clients = new ArrayList<InterfaceClientRMI>();

	public ServerRMI() throws RemoteException {
		super();
	}
	public ArrayList<Pessoa> getEstudantes() throws RemoteException{
		return this.Estudantes;
	}
	public ArrayList<Pessoa> getDocentes() throws RemoteException{
		return this.Docentes;
	}
	public ArrayList<Pessoa> getFuncionarios() throws RemoteException{
		return this.Funcionarios;
	}
	public ArrayList<Eleicao> getEleicoes() throws RemoteException{
		return this.eleicoes;
	}
	public ArrayList<InterfaceClientRMI> getClients() throws RemoteException{
		return this.clients;
	}
	public void SaveRegistry(Pessoa pessoa) throws RemoteException {

		File arquivo = new File("teste.txt");
		try {
			if (!arquivo.exists()) {
				arquivo.createNewFile();
			}
			FileOutputStream fw = new FileOutputStream(arquivo);
			ObjectOutputStream bw = new ObjectOutputStream(fw);

			// type | register ; username | pierre ; password | omidyar ; job | estudante ; tele | 913613099 ; adress | Seia ; CCNumber | 123 ; CCVal | 12/05/2023 ; Depart | UC ;

			if(pessoa.getTrabalho().toUpperCase().equals("ESTUDANTE")){
				Estudantes.add(pessoa);
			
			}
			else if(pessoa.getTrabalho().toUpperCase().equals("DOCENTE")){
				Docentes.add(pessoa);
			
			}
			else if(pessoa.getTrabalho().toUpperCase().equals("FUNCIONARIO")){
				Funcionarios.add(pessoa);
			}

			bw.writeObject(pessoa);
			bw.close();
			fw.close();
			System.out.println("SUCCESSFULLY REGISTERED !");

		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return;
	}
	public void criarEleicao(Eleicao eleicao) throws RemoteException {

		File arquivo = new File("teste.txt");
		try {
			if (!arquivo.exists()) {
				arquivo.createNewFile();
			}

			FileOutputStream fw = new FileOutputStream(arquivo, true);
			ObjectOutputStream bw = new ObjectOutputStream(fw);

			// type | eleicao ; nome | Lista A ; dataInicio | 12/04/2021 13:30 ; dataFim | 12/04/2021 18:30 ; publicoAlvo | estudantes ;
			eleicoes.add(eleicao);
			bw.writeObject(eleicao);
			bw.close();
			fw.close();
			System.out.println("MESA DE VOTO CRIADA COM SUCESSO !");
		
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	public void saveClients(String name, InterfaceClientRMI a) throws RemoteException{
		System.out.println("CONNECTED WITH " + name);
		clients.add(a);
	}	
	public void print_on_server(String s) throws RemoteException {
		System.out.println("> " + s);
	}

	public static void main(String args[]) throws IOException {
		String a;

		InputStreamReader input = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(input);
		
		try {
			ServerRMI h = new ServerRMI();
			Registry r = LocateRegistry.createRegistry(7000);
			r.rebind("RMI Server", h);
			System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
			System.out.println("======================RMI SERVER READY!======================");
			
			while(true){
				System.out.print("> ");
				a = reader.readLine();
				h.clients.get(0).print_on_client(a);
			}
		} catch (RemoteException re) {
			System.out.println("Exception in HelloImpl.main: " + re);
		}
	}
}