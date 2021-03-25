import java.io.*;
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
	ArrayList<Pessoa> person = new ArrayList<Pessoa>();
	ArrayList<Eleicao> eleicoes = new ArrayList<Eleicao>();
	ArrayList<InterfaceClientRMI> clients = new ArrayList<InterfaceClientRMI>();

	public ServerRMI() throws RemoteException {
		super();
	}

	public ArrayList<Pessoa> getEstudantes() throws RemoteException {
		return this.Estudantes;
	}

	public ArrayList<Pessoa> getPerson() throws RemoteException {
		return this.person;
	}

	public ArrayList<Pessoa> getDocentes() throws RemoteException {
		return this.Docentes;
	}

	public ArrayList<Pessoa> getFuncionarios() throws RemoteException {
		return this.Funcionarios;
	}

	public ArrayList<Eleicao> getEleicoes() throws RemoteException {
		return this.eleicoes;
	}

	public ArrayList<InterfaceClientRMI> getClients() throws RemoteException {
		return this.clients;
	}

	private static void loadData(ServerRMI h) {

		try {
			FileInputStream fis = new FileInputStream("eleicao.dat");
			ObjectInputStream ois = new ObjectInputStream(fis);

			// read object from file7
			Eleicao ele = new Eleicao();
			while((ele = (Eleicao) ois.readObject()) != null){
				h.eleicoes.add(ele);
			}


		} catch (IOException | ClassNotFoundException ex) {
			ex.printStackTrace();
		}

		try {
			FileInputStream fis = new FileInputStream("Pessoas.dat");
			 ObjectInputStream ois = new ObjectInputStream(fis) ;

			// read object from file7
			Pessoa ele = new Pessoa();
			while((ele = (Pessoa) ois.readObject()) != null){
				h.person.add(ele);
			}

		} catch (IOException | ClassNotFoundException ex) {
			ex.printStackTrace();
		}

		for(Pessoa p : h.person){
			if (p.getTrabalho().toUpperCase().equals("ESTUDANTE")) {
				h.Estudantes.add(p);

			} else if (p.getTrabalho().toUpperCase().equals("DOCENTE")) {
				h.Docentes.add(p);

			} else if (p.getTrabalho().toUpperCase().equals("FUNCIONARIO")) {
				h.Funcionarios.add(p);
			}
		}
	}

	public void SaveRegistry(Pessoa pessoa) throws RemoteException {

		File arquivo = new File("Pessoas.dat");
		try {
			if (!arquivo.exists()) {
				arquivo.createNewFile();
			}
			FileOutputStream fw = new FileOutputStream(arquivo);
			ObjectOutputStream bw = new ObjectOutputStream(fw);

			// type | register ; username | pierre ; password | omidyar ; job | estudante ; tele | 913613099 ; adress | Seia ; CCNumber | 123 ; CCVal | 12/05/2023 ; Depart | UC ;

			if (pessoa.getTrabalho().toUpperCase().equals("ESTUDANTE")) {
				Estudantes.add(pessoa);

			} else if (pessoa.getTrabalho().toUpperCase().equals("DOCENTE")) {
				Docentes.add(pessoa);

			} else if (pessoa.getTrabalho().toUpperCase().equals("FUNCIONARIO")) {
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

		File arquivo = new File("eleicao.dat");
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

	public void saveClients(String name, InterfaceClientRMI a) throws RemoteException {
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

			loadData(h);

			System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
			System.out.println("======================RMI SERVER READY!======================");

			while (true) {
				System.out.print("> ");
				a = reader.readLine();
				h.clients.get(0).print_on_client(a);
			}
		} catch (RemoteException re) {
			System.out.println("Exception in HelloImpl.main: " + re);
		}
	}
}