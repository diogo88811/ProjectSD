import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class ClientRMI extends UnicastRemoteObject implements InterfaceClientRMI{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	String nome;

	ClientRMI() throws RemoteException {
		super();
	}

	public ClientRMI(String nome) throws RemoteException{
		this.nome = nome;
	}

	public String getNome() {
		return this.nome;
	}

	public void print_on_client(String s) throws RemoteException {
		System.out.println("> " + s);
	}

	public static void main(String args[]) {
		String a;
		int menuOption;
		Scanner scan = new Scanner(System.in);
		InputStreamReader input = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(input);
		System.out.println("======================ADMIN CONSOLE!======================");
		System.out.println("1. Registar pessoas");
		System.out.println("2. Criar Eleicao");
		System.out.println("3. Gerir Candidatos a uma Eleicao ");
		System.out.println("4. Gerir Mesas de Votos");

		try {

			InterfaceServerRMI h = (InterfaceServerRMI) LocateRegistry.getRegistry(7000).lookup("RMI Server");
			ClientRMI client = new ClientRMI(args[0]);
			h.saveClients(args[0], (InterfaceClientRMI) client);

			while (true) {
				menuOption = scan.nextInt();
				switch(menuOption){
					case 1:
						Pessoa pessoa = new Pessoa();
						pessoa.RegisterPerson();
						//System.out.println(pessoa.toString());
						h.SaveRegistry(pessoa);
						break;
					case 2:
						Eleicao eleicao  = new Eleicao();
						eleicao.createEleicao();
						break;
				}


				System.out.print("> ");
				a = reader.readLine();
				h.print_on_server(a);

				/*
				scanf (nome);
				h.SaveRegistry(nome, idade);
				*/
				/*
				 * String line = scanner.nextLine(); String[] arrOfStr = line.split(" | ");
				 * 
				 * // type | eleicao ; nome | Lista A ; dataInicio | 12/04/2021 13:30 ; dataFim
				 * | 12/04/2021 18:30 ; publico | estudantes ;
				 * if(arrOfStr[2].equals("eleicao")){ h.criarEleicao(line); } // type | register
				 * ; username | pierre ; password | omidyar ; job | estudante ; tele | 913613099
				 * ; adress | Seia ; CCNumber | 123 ; CCVal | 12/05/2023 ; Depart | UC ; else
				 * if(arrOfStr[2].equals("register")){ h.SaveRegistry(line); } else
				 * if(line.equals("mesa")){
				 * 
				 * }
				 */
			}

		} catch (Exception e) {
			System.out.println("Exception in main: " + e);
			e.printStackTrace();
		}

	}
}
