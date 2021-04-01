import java.io.BufferedReader;
import java.io.InputStreamReader;
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
			h.saveAdmin(args[0], (InterfaceClientRMI) client);


			while (true) {
				System.out.println("_____________________________< ADMIN CONSOLE >_______________________________________");
				System.out.println("<1> REGISTAR PESSOA");
				System.out.println("<2> CRIAR ELEICAO");
				System.out.println("<3> GERIR CANDIDATOS A UMA ELEICAO ");
				System.out.println("<4> ALTERAR PROPRIEDADES DE UMA ELEICAO");
				System.out.println("<5> CONSULTAR DADOS DAS ANTIGAS ELEICOES");
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
							h.stateOfElections(); // teste
						}catch (Exception e){
							h = (InterfaceServerRMI) LocateRegistry.getRegistry(7000).lookup("RMI Server");
							eleicao.createEleicao(h.getEstudantes());
							h.criarEleicao(eleicao);
							h.stateOfElections(); //teste
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
						for(int i = 0; i < election.size(); i++){
							System.out.println("<" + i +"> " + election.get(i).getNome());
						}
						int eleNum = scan.nextInt();

						System.out.println("SELECIONE UMA OPCAO: ");
						System.out.println("<1> MODIFICAR LISTA: ");
						System.out.println("<2> ADICIONAR LISTA: ");
						int opt = scan.nextInt();
						if(opt == 1){
							System.out.println("SELECIONE A LISTA: ");
							for(int i = 0; i < election.get(eleNum).getListas().size(); i++){
								System.out.println(i + " "+ election.get(eleNum).getListas().get(i).getNomeLista());
							}

							int numList = scan.nextInt();
							Lista l = election.get(eleNum).getListas().get(numList);
							l.modifyList(h.getEstudantes());
							try{
								h.gerirEleicao(l,eleNum,opt,numList);
							}catch (Exception e){
								h = (InterfaceServerRMI) LocateRegistry.getRegistry(7000).lookup("RMI Server");
								h.gerirEleicao(l,eleNum,opt,numList);
							}


						}
						else if(opt == 2){
							Lista l = new Lista();
							l.createList(h.getEstudantes());
							try{
								h.gerirEleicao(l,eleNum,opt,0);
							}catch (Exception e){
								h = (InterfaceServerRMI) LocateRegistry.getRegistry(7000).lookup("RMI Server");
								h.gerirEleicao(l,eleNum,opt,0);
							}

						}

						break;
					case 4:
						//só mostrar eleicaoes que aindam não comecaram
						//so as que não começaram podem ser alteradas
						try{
							System.out.println("SELECIONE A ELEICAO: ");
							for(int i = 0; i < h.getEleicoes().size(); i++){
								//if para verificar se a eleição já acabou
								//adicionando flag de acabou na classe eleicao
								//fazer depois com o tempo
								System.out.println(i + " " + h.getEleicoes().get(i).getNome());
							}
							int numEle = scan.nextInt();
							Eleicao el = h.getEleicoes().get(numEle);
							el.changeEle();
							h.alteraEleicao(el,numEle);
						}catch (Exception e){
							h = (InterfaceServerRMI) LocateRegistry.getRegistry(7000).lookup("RMI Server");
							System.out.println("SELECIONE A ELEICAO: ");
							for(int i = 0; i < h.getEleicoes().size(); i++){
								//if para verificar se a eleição já acabou
								//adicionando flag de acabou na classe eleicao
								//fazer depois com o tempo
								System.out.println(i + " " + h.getEleicoes().get(i).getNome());
							}
							int numEle = scan.nextInt();
							Eleicao el = h.getEleicoes().get(numEle);
							el.changeEle();
							h.alteraEleicao(el,numEle);
						}

						break;

					case 5:
						//eleicoes que já tenham terminado
						System.out.println("SELECIONE UMA ELEICAO");
						for(int i = 0; i < h.getEleicoes().size(); i++){
							System.out.println(i + " "+ h.getEleicoes().get(i).getNome());
						}
						int numEle = scan.nextInt();
						/*
						System.out.println("DADOS:");
						for(int i = 0; i < h.getEleicoes().get(numEle).getListas().size(); i++){
							System.out.println(h.getEleicoes().get(numEle).getListas().get(i).getNomeLista()+ " NUMERO DE VOTO: "+h.getEleicoes().get(numEle).getListas().get(i).getNumVotes());
						}
						*/
						System.out.println(h.getEleicoes().get(numEle).toString());
						break;
				}
			}

		} catch (Exception e) {
			System.out.println("Exception in main: " + e);
			e.printStackTrace();
		}

	}
}
