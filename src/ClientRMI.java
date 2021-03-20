import java.rmi.registry.LocateRegistry;
import java.util.Scanner;

public class ClientRMI {

	public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);
		try {
			System.out.println("======================ADMIN CONSOLE!======================");
			InterfaceRMI h = (InterfaceRMI) LocateRegistry.getRegistry(7000).lookup("RMI Server");
			while(true){
				String line = scanner.nextLine();
				String[] arrOfStr = line.split(" | "); 				

				// type | eleicao ; nome | Lista A ; dataInicio | 12/04/2021 13:30 ; dataFim | 12/04/2021 18:30 ; publico | estudantes ; 
				if(arrOfStr[2].equals("eleicao")){
					h.criarEleicao(line);
				}
				// type | register ; username | pierre ; password | omidyar ; job | estudante ; tele | 913613099 ; adress | Seia ; CCNumber | 123 ; CCVal | 12/05/2023 ; Depart | UC ;
				else if(arrOfStr[2].equals("register")){
					h.SaveRegistry(line);
				}
			}

		} catch (Exception e) {
			System.out.println("Exception in main: " + e);
			e.printStackTrace();
		}

	}
    
}
