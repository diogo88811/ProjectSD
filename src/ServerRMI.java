import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;
import java.util.ArrayList;
import java.util.Arrays;

public class ServerRMI extends UnicastRemoteObject implements InterfaceRMI {

	public ServerRMI() throws RemoteException {
		super();
	}

	public void SaveRegistry(String string) throws RemoteException {

		File arquivo = new File("teste.txt");
		try {
			if (!arquivo.exists()) {
				arquivo.createNewFile();
			}
			FileOutputStream fw = new FileOutputStream(arquivo);
			ObjectOutputStream bw = new ObjectOutputStream(fw);

			// type | register ; username | pierre ; password | omidyar ; job | estudante ; tele | 913613099 ; adress | Seia ; CCNumber | 123 ; CCVal | 12/05/2023 ; Depart | UC ;
			String[] arrOfStr = string.split("\\|");
			String[] Strings = Arrays.copyOfRange(arrOfStr, 2, arrOfStr.length);
			ArrayList<String> person = new ArrayList<String>();
			for (String aux : Strings) {
				String[] temp = aux.split(";");
				person.add(temp[0]);
			}
			Pessoa pessoa = new Pessoa(person.get(0), person.get(1), person.get(2), person.get(3), person.get(4),
					person.get(5), person.get(6), person.get(7));
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

	public void Login(String string) throws RemoteException {
		// Verificar ficheiro
		System.out.println("type | status ; logged | on ; msg | Welcome to eVoting !");

	}

	public void criarEleicao(String string) throws RemoteException {

		File arquivo = new File("teste.txt");
		try {
			if (!arquivo.exists()) {
				arquivo.createNewFile();
			}

			FileOutputStream fw = new FileOutputStream(arquivo, true);
			ObjectOutputStream bw = new ObjectOutputStream(fw);

			// type | eleicao ; nome | Lista A ; dataInicio | 12/04/2021 13:30 ; dataFim | 12/04/2021 18:30 ; publicoAlvo | estudantes ;

			String[] arrOfStr = string.split("\\|");
			String[] Strings = Arrays.copyOfRange(arrOfStr, 2, arrOfStr.length);
			ArrayList<String> arrayEleicao = new ArrayList<String>();
			for (String aux : Strings) {
				String[] temp = aux.split(";");
				arrayEleicao.add(temp[0]);
			}
			Eleicao eleicao = new Eleicao(arrayEleicao.get(0), arrayEleicao.get(1), arrayEleicao.get(2), arrayEleicao.get(3));
			bw.writeObject(eleicao);
			bw.close();
			fw.close();
			System.out.println("MESA DE VOTO CRIADA COM SUCESSO !");
		
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	// =========================================================
	public static void main(String args[]) {

		try {
			// create the remote object that provides the service.
			ServerRMI h = new ServerRMI();

			// Cria um registo na porta 7000 que vai aceitar pedidos nessa porta
			Registry r = LocateRegistry.createRegistry(7000);

			// Faz o bind entre o objeto e o "RMI Server"
			r.rebind("RMI Server", h);

			System.out.println("======================RMI SERVER READY!======================");

		} catch (RemoteException re) {
			System.out.println("Exception in HelloImpl.main: " + re);
		}
	}
}