import java.net.MulticastSocket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


public class MulticastServer extends Thread {

    private String MULTICAST_ADDRESS = "224.0.224.0";
    private int PORT = 7000;
    private static MulticastServer server;
    private static MulticastUser user;
    Scanner keyboardScanner = new Scanner(System.in);
    String dataReceived;
    int number = 0;
    int id = 0;
    HashMap<String, String> info = new HashMap<String, String>();
    InterfaceServerRMI h;

    public MulticastServer(InterfaceServerRMI h) {
        super("SERVER " + (long) (Math.random() * 1000));
        this.h = h;
    }

    public String receiveData(MulticastSocket socket) throws IOException{
        byte[] buffer = new byte[256];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);
        String message = new String(packet.getData(), 0, packet.getLength());
//        System.out.println(message);
        return message;
    }
    
    public void analyseData(MulticastSocket socket, String data) throws IOException{

        String aux[] = data.split("[;]");
        for(String a : aux){
            String types[] = a.split("\\|");
            info.put(types[0].trim(), types[1].trim());
        }

        if(info.get("type").equals("login")){ //"type | login ; username | Miguel ; ccNumber | 1234
            user.sendData(socket, "type | request ; username | " + info.get("username") + " ; ccNumber | " + info.get("ccNumber") + " ; NumberRequest | " + number );
        }
        else if(info.get("type").equals("requestAnswer") && number == Integer.parseInt(info.get("NumberRequest"))){
            user.sendData(socket, "type | reserve ; username | " + info.get("username") + " ; ccNumber | " + info.get("ccNumber") + " ; NumberRequest | " + number + " ; terminalID | " + info.get("IDclient"));
            number++;
        }
        else if(info.get("type").equals("reserved")){
            System.out.println("GO TO TERMINAL " + info.get("IDclient") + " !");
        }
        else if(info.get("type").equals("authentication")){
            if(h.verifyUser(info.get("username"), info.get("ccNumber"), info.get("PASSWORD")) == true){
                System.out.println("BEM VINDO, " + info.get("username") + " !");
                user.sendData(socket, "type | vote ; username | " + info.get("username") + " ; ccNumber | " + info.get("ccNumber") + " ; NumberRequest | " + number + " ; terminalID | " + info.get("IDclient"));
            }
            else{
                System.out.println("UTILIZADOR NAO ESTA REGISTADO, POR FAVOR REGISTE SE NA NOSSA PLATAFORMA !");
            }
        }
        else if(info.get("type").equals("item_listRequire")){
            String lista = "type | item_list ; item_count | " + h.getEstudantes().size() + " ; ";
            for(int i = 0; i< h.getEstudantes().size(); i++){
                System.out.println(h.getEstudantes().get(i).nome);
                lista += "item_" + i + "_name | " + h.getEstudantes().get(i).nome + " ; ";
            }
            user.sendData(socket, lista + "username | " + info.get("username") + " ; ccNumber | " + info.get("ccNumber"));
        }
    }


    public void run() {
        MulticastSocket socket = null;
        try {
            System.out.println("================================< " + this.getName() + " >=========================================");
            
            ClientRMI client = new ClientRMI(this.getName());
            h.saveClients(this.getName(), (InterfaceClientRMI) client);
            
            socket = new MulticastSocket(PORT);  
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);

            while (true) {            
                dataReceived = receiveData(socket);
                analyseData(socket, dataReceived);
            } 
        } catch (IOException e) { e.printStackTrace();}
    }

    public static void main(String[] args) throws IOException, NotBoundException {
        InterfaceServerRMI h = (InterfaceServerRMI) LocateRegistry.getRegistry(7000).lookup("RMI Server");
        server = new MulticastServer(h);  //THREAD RECEBE
        server.start();
        user = new MulticastUser(h);  //THREAD ENVIA
        user.start();
    }
}

class MulticastUser extends Thread {
    private String MULTICAST_ADDRESS = "224.0.224.1";
    private int PORT = 7000;
    MulticastServer server;
    boolean verify = false;
    InterfaceServerRMI h;

    InputStreamReader input = new InputStreamReader(System.in);
    BufferedReader reader = new BufferedReader(input);
    Scanner keyboardScanner = new Scanner(System.in);

    public MulticastUser(InterfaceServerRMI h) {
        super("SERVER " + (long) (Math.random() * 1000));
        this.h = h;
    }

    public void sendData(MulticastSocket socket, String data) throws IOException{
        String aux = data + " " ;
        byte[] buffer = aux.getBytes();
        InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);
        socket.send(packet);
    }
    
    public void sendToServer(MulticastSocket socket, String data) throws IOException{
        String aux = data + " " ;
        //System.out.println(aux);
        byte[] buffer = aux.getBytes();
        InetAddress group = InetAddress.getByName("224.0.224.0");
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);
        socket.send(packet);
    }
  
    public void run() {
        MulticastSocket socket = null;
        try {
            socket = new MulticastSocket();  
            while (true) {
                if(verify == false){
                    System.out.println("IDENTIFICA O DEPARTEMENTO DA MESA :");
                    System.out.print(">");
                    String dep = reader.readLine();
                    verify = true;
                    System.out.println("=======================================< " + dep + " >=========================================");
                    System.out.println("<1> LOGIN");
                }
                else{
                    String teste = reader.readLine();
                    if(teste.equals("1")){
                        System.out.print("NAME: ");
                        String aux = reader.readLine();
                        System.out.print("CC NUMBER: ");
                        String cc = reader.readLine();
                        h.verifyLogin(aux, cc);
                        sendToServer(socket, "type | login ; username | " + aux + " ; ccNumber | " + cc );  
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }  finally {
            socket.close();
        }
    }   
}
