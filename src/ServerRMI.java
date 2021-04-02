import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
	ArrayList<InterfaceClientRMI> clientsAdmin = new ArrayList<InterfaceClientRMI>();
	ArrayList<InterfaceClientRMI> clients = new ArrayList<InterfaceClientRMI>();
	String crashName, crashCC = "";


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

	public ArrayList<InterfaceClientRMI> getAdminClients() throws RemoteException {
		return this.clients;
	}

	public String getCrashName() throws RemoteException {
		return this.crashName;
	}

	public String getCrashCC() throws RemoteException {
		return this.crashCC;
	}

	public void setCrashName(String name) throws RemoteException{
		this.crashName = name;
	}

	public void setCrashCC(String CC) throws RemoteException{
		this.crashCC = CC;
	}

	public  void loadDataElection() throws RemoteException {
		System.out.println("Getting data....");
		try {
			FileInputStream fin = new FileInputStream("eleicao.txt");
			ObjectInput oin = new ObjectInputStream(fin);

			System.out.println("DeSerialization process has started, "
					+ "displaying employee objects...");

			eleicoes = (ArrayList<Eleicao>) oin.readObject();
			if(eleicoes.isEmpty()){
				System.out.println("vazio");
			}
			else{
				for(Eleicao e : eleicoes){
					System.out.println(e.getNome());
				}
			}

			oin.close();
			fin.close();

		} catch (EOFException e) {
			System.out.println("File ended");
		}  catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try {
			FileInputStream fin = new FileInputStream("Pessoas.txt");
			ObjectInput oin = new ObjectInputStream(fin);

			person = (ArrayList<Pessoa>) oin.readObject();
			if(person.isEmpty()){
				System.out.println("sem pesoas");
			}
			else{
				for(Pessoa e : person){
					System.out.println(e.getNome());
				}
			}

			oin.close();
			fin.close();

		} catch (EOFException e) {
			System.out.println("File ended");
		}  catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		for(Pessoa p : person){
			if (p.getTrabalho().toUpperCase().equals("ESTUDANTE")) {
				Estudantes.add(p);

			} else if (p.getTrabalho().toUpperCase().equals("DOCENTE")) {
				Docentes.add(p);

			} else if (p.getTrabalho().toUpperCase().equals("FUNCIONARIO")) {
				Funcionarios.add(p);
			}
		}
	}

	public void SaveRegistry(Pessoa pessoa) throws RemoteException {

		File arquivo = new File("Pessoas.txt");
		try {
			if (!arquivo.exists()) {
				arquivo.createNewFile();
			}
			FileOutputStream fw = new FileOutputStream(arquivo);
			ObjectOutputStream bw = new ObjectOutputStream(fw);

			// type | register ; username | pierre ; password | omidyar ; job | estudante ; tele | 913613099 ; adress | Seia ; CCNumber | 123 ; CCVal | 12/05/2023 ; Depart | UC ;
			person.add(pessoa);
			if (pessoa.getTrabalho().toUpperCase().equals("ESTUDANTE")) {
				Estudantes.add(pessoa);

			} else if (pessoa.getTrabalho().toUpperCase().equals("DOCENTE")) {
				Docentes.add(pessoa);

			} else if (pessoa.getTrabalho().toUpperCase().equals("FUNCIONARIO")) {
				Funcionarios.add(pessoa);
			}

			bw.writeObject(person);
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

	public void gerirEleicao(Lista l, int eleNum,int opt,int indexLi) throws IOException {
			if(opt == 2){
				eleicoes.get(eleNum).getListas().add(l);
				OutputStream fout = new FileOutputStream("eleicao.txt");
				ObjectOutput oout = new ObjectOutputStream(fout);
				oout.writeObject(eleicoes);
				oout.close();
			}
			else if(opt == 1){
				eleicoes.get(eleNum).getListas().remove(indexLi);
				eleicoes.get(eleNum).getListas().add(l);
				OutputStream fout = new FileOutputStream("eleicao.txt");
				ObjectOutput oout = new ObjectOutputStream(fout);
				oout.writeObject(eleicoes);
				oout.close();
			}

	}

	public void alteraEleicao(Eleicao e, int numEle) throws IOException {
		eleicoes.remove(numEle);
		eleicoes.add(e);
		OutputStream fout = new FileOutputStream("eleicao.txt");
		ObjectOutput oout = new ObjectOutputStream(fout);
		oout.writeObject(eleicoes);
		oout.close();
	}

	public void criarEleicao(Eleicao eleicao) throws RemoteException {

		try {
			OutputStream fout = new FileOutputStream("eleicao.txt");
			ObjectOutput oout = new ObjectOutputStream(fout);

			eleicoes.add(eleicao);
			oout.writeObject(eleicoes);
			oout.close();
			System.out.println("MESA DE VOTO CRIADA COM SUCESSO !");

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void saveClients(String name, InterfaceClientRMI a) throws RemoteException {
		System.out.println("CONNECTED WITH " + name);
		clients.add(a);
	}

	public void saveAdmin(String name, InterfaceClientRMI a) throws RemoteException {
		System.out.println("CONNECTED WITH " + name);
		clientsAdmin.add(a);
	}

	public void print_on_server(String s) throws RemoteException {
		System.out.println("> " + s);
	}

	public boolean stateOfElections(Eleicao eleicao, int option) throws RemoteException, ParseException {
		
			String dataInicial = eleicao.getDataInicio();
			String datafinal = eleicao.getDataFim();
			DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date inicial = sdf.parse(dataInicial);
			Date fina = sdf.parse(datafinal);

			Date now = new Date();
			String strDate = sdf.format(now);
			Date actual = sdf.parse(strDate);
			
			if(option == 0){
				// Se ainda nao comecou
				if(!inicial.before(actual)){
					return true;
				}
				else{
					return false;
				}
			}
			else if(option == 1){
				// Se ja acabou
				if(fina.before(actual)){
					return true;
				}
				else{
					return false;
				}
			}
			else if(option == 2){
				// Se esta a decorrer 
				if(inicial.before(actual) && !fina.before(actual)){
					return true;
				}
				else{
					return false;
				}
			}

			return false;
	}

	public void saveVotes(String eleicao, String lista) throws RemoteException{
		//eleicao
		//lista em que votou
		boolean flag = false;
		System.out.println("tamanho das eleicoes " + eleicoes.size());
		if(lista.equals("BRANCO")){
			for(int i = 0; i < eleicoes.size() ; i++){
				if(eleicoes.get(i).getNome().equals(eleicao)) {
					eleicoes.get(i).votosBranco +=1;
				}
			}
		}
		else if(lista.equals("NULL")){
			for(int i = 0; i < eleicoes.size() ; i++){
				if(eleicoes.get(i).getNome().equals(eleicao)) {
					eleicoes.get(i).votoNulo +=1;
					System.out.println("VOTOU NULO");
				}
			}
		}

		else{
			for(int i = 0; i < eleicoes.size() ; i++){
				if(eleicoes.get(i).getNome().equals(eleicao)){
					ArrayList<Lista> l = eleicoes.get(i).listas;
					System.out.println("Tamanho das lista "+l.size());
					for(int j = 0; j < l.size(); j++){
						if(l.get(j).getNomeLista().equals(lista)){
							l.get(j).setNumVotes((l.get(j).getNumVotes() + 1));
							flag = true;
						}
					}
				}
			}
			if(!flag){
				System.out.println("Em branco ou nulo");
			}
		}

		try {
			OutputStream fout = new FileOutputStream("eleicao.txt");
			ObjectOutput oout = new ObjectOutputStream(fout);

			oout.writeObject(eleicoes);
			oout.close();

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public boolean verifyUser(String nome, String ccNumber, String password) throws RemoteException{
		for(int i = 0; i< person.size(); i++){
			if(person.get(i).nome.equals(nome) && person.get(i).CCnumber.equals(ccNumber) && person.get(i).password.equals(password)){
				return true;
			}
		}
		return false;
	}

	public boolean verifyLogin(String nome, String ccNumber) throws RemoteException {
		for(int i = 0; i< person.size(); i++){
			if(person.get(i).nome.equals(nome) && person.get(i).CCnumber.equals(ccNumber)){
				return true;
			}
		}
		return false;
	}

	public void notifyClient(String name, String tag) throws RemoteException{

		for(int i = 0; i<clientsAdmin.size(); i++){
			clientsAdmin.get(i).print_on_client(name + tag);
		}
	}

	public void saveUserVote(String name, String ccNumber, String election) throws RemoteException{
		Pessoa aux = null;
		for(int i = 0; i<person.size(); i++){
			if(person.get(i).nome.equals(name) && person.get(i).CCnumber.equals(ccNumber)){
				aux = person.get(i);
			}
		}
		
		for(int i = 0; i< eleicoes.size(); i++){
			if(eleicoes.get(i).nome.equals(election)){
				eleicoes.get(i).getpeopleWhoVoted().add(aux);
			}
		}
		
	}

	public boolean verifyUserinArray(String name, String ccNUmber, Eleicao election) throws RemoteException{
		for(int i = 0; i < election.getpeopleWhoVoted().size(); i++){
			if(election.getpeopleWhoVoted().get(i).getNome().equals(name) && election.getpeopleWhoVoted().get(i).getCCnumber().equals(ccNUmber)){ 
				return true;
			}
		}
		
		return false;
	}

	public void saveVotedPlaceOnPeople(String name, String ccNumber, String table) throws RemoteException{
		Pessoa aux = null;
		for(int i = 0; i<person.size(); i++){
			if(person.get(i).nome.equals(name) && person.get(i).CCnumber.equals(ccNumber)){
				aux = person.get(i);
			}
		}
		aux.getTables().add(table);

		try {
			OutputStream fout = new FileOutputStream("eleicao.txt");
			ObjectOutput oout = new ObjectOutputStream(fout);

			oout.writeObject(eleicoes);
			oout.close();

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public String getUserproperties(String name, String ccNumber) throws RemoteException{
		for(int i = 0; i < person.size(); i++){
			if(person.get(i).nome.equals(name) && person.get(i).CCnumber.equals(ccNumber) ){
				return person.get(i).trabalho;
			}
		}
		return "";
	}

	public static void main(String args[]) throws IOException, InterruptedException {
		String a;
		DatagramSocket aSocket = null;
		InputStreamReader input = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(input);

		while(true){
			try{
				aSocket = new DatagramSocket();
				System.out.println("ping");
				InetAddress host = InetAddress.getByName("127.0.0.1");
				int serverPort = 6789;
				byte[] buffer = new byte[1000];
				DatagramPacket request = new DatagramPacket(buffer,buffer.length,host,serverPort);
				aSocket.send(request);

				aSocket.setSoTimeout(2000);

				byte[] buff = new byte[1000];
				DatagramPacket reply = new DatagramPacket(buff,buff.length,host,serverPort);
				aSocket.receive(reply);
				System.out.println("recebido");
				Thread.sleep(2000);


			} catch (InterruptedException e){
				e.printStackTrace();
			} catch (UnknownHostException e){
				e.printStackTrace();
			} catch (SocketException e){
				e.printStackTrace();
			} catch (IOException e){
				System.out.println("Não encontrou o server");
				break;
			}
		}


		NewThread t = new NewThread();
		t.start();

		try {

			System.getProperties().put("java.security.policy","policy.all");
        	System.setSecurityManager(new SecurityManager());

			ServerRMI h = new ServerRMI();

			System.getProperties().put("java.security.policy","policy.all");
        	System.setSecurityManager(new SecurityManager());

			Registry r = LocateRegistry.createRegistry(7000);
			r.rebind("RMI Server", h);

			h.loadDataElection();

            System.out.println("___________________________< RMI SERVER READY ! >_____________________________________");

			while (true) {
				System.out.print("> ");
				a = reader.readLine();
				for(int i = 0; i< h.clientsAdmin.size(); i++){
					h.clientsAdmin.get(i).print_on_client(a);
				}
			}
		} catch (RemoteException re) {
			System.out.println("Exception in HelloImpl.main: " + re);
		}
	}
}
class NewThread extends Thread {

	public void run() {      // entry point

		DatagramSocket aSocket = null;
		int serverPort = 6789;
		InetAddress host = null;
		try {
			aSocket = new DatagramSocket(6789);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		while (true) {

			byte[] buffer = new byte[1000];
			DatagramPacket request = new DatagramPacket(buffer, buffer.length);
			try {
				aSocket.receive(request);
			} catch (IOException e) {
				e.printStackTrace();
			}

			byte[] buff = new byte[1000];
			DatagramPacket reply = new DatagramPacket(request.getData(), request.getLength(), request.getAddress(), request.getPort());
			try {
				aSocket.send(reply);
			} catch (IOException e) {
				e.printStackTrace();
			}
	}}
}