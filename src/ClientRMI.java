import java.io.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLOutput;
import java.util.ArrayList;
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
		System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");


		try {

			InterfaceServerRMI h = (InterfaceServerRMI) LocateRegistry.getRegistry(7000).lookup("RMI Server");
			ClientRMI client = new ClientRMI(args[0]);
			h.saveClients(args[0], (InterfaceClientRMI) client);

			while (true) {
				System.out.println("======================ADMIN CONSOLE!======================");
				System.out.println("<1> REGISTAR PESSOA");
				System.out.println("<2> CRIAR ELEICAO");
				System.out.println("<3> GERIR CANDIDATOS A UMA ELEICAO ");
				System.out.println("<4> GERIR MESAS DE VOTOS");
				System.out.print(">");
				menuOption = scan.nextInt();
				switch(menuOption){
					case 1:
						Pessoa pessoa = new Pessoa();
						pessoa.RegisterPerson();
						try{
							h.SaveRegistry(pessoa);
						}catch(Exception e){
							h = (InterfaceServerRMI) LocateRegistry.getRegistry(7000).lookup("RMI Server");
							h.SaveRegistry(pessoa);
						}
						break;
					case 2:
						Eleicao eleicao  = new Eleicao();
						try{
							eleicao.createEleicao(h.getEstudantes());
							h.criarEleicao(eleicao);
						}catch (Exception e){
							h = (InterfaceServerRMI) LocateRegistry.getRegistry(7000).lookup("RMI Server");
							eleicao.createEleicao(h.getEstudantes());
							h.criarEleicao(eleicao);
						}
						break;
					case 3:
						ArrayList<Eleicao> election;
						try{
							election = h.getEleicoes();
						}
						catch (Exception e){
							h = (InterfaceServerRMI) LocateRegistry.getRegistry(7000).lookup("RMI Server");
							election = h.getEleicoes();
						}

						System.out.println("SELECIONE UMA ELEICAO\n> ");
						for(int i = 0; i < h.getEleicoes().size(); i++){
							System.out.println("<" + i +"> " + h.getEleicoes().get(i).getNome());
						}
						int eleNum = scan.nextInt();


						System.out.println("SELECIONE UMA OPCAO: ");
						System.out.println("<1> MODIFICAR LISTA: ");
						System.out.println("<2> ADICIONAR LISTA: ");
						int opt = scan.nextInt();
						if(opt == 1){
							System.out.println("LISTAS PRESENTES NA ELEIÇÂO");
							for(int i = 0; i < h.getEleicoes().get(eleNum).getListas().size(); i++){
								System.out.println("<" + i +"> " + h.getEleicoes().get(eleNum).getListas().get(i).getNomeLista());
							}
							int listnum = scan.nextInt();
							h.getEleicoes().get(eleNum).getListas().get(listnum).manageCandidateList();
						}
						else if(opt == 2){
							Lista l = new Lista();
							l.createList(h.getEstudantes());
							//funçao no server RMI para adicionar uma LISTA.
							// exemplo --- > h.addLISTA
							h.getEleicoes().get(eleNum).getListas().add(l);

						}

						break;

				}
			}

		} catch (Exception e) {
			System.out.println("Exception in main: " + e);
			e.printStackTrace();
		}

	}
}
