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

    public MulticastServer() {
        super("SERVER " + (long) (Math.random() * 1000));
    }

    public String receiveData(MulticastSocket socket) throws IOException{
        byte[] buffer = new byte[256];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);
        String message = new String(packet.getData(), 0, packet.getLength());
//        System.out.println(message);
        return message;
    }
    
    public void analyseData(MulticastSocket socket, String data/*, InterfaceServerRMI h*/) throws IOException{

        String aux[] = data.split("[;]");
        for(String a : aux){
            String types[] = a.split("\\|");
            info.put(types[0].trim(), types[1].trim());
        }

        if(info.get("type").equals("login")){
            user.sendData(socket, "type | request ; username | " + info.get("username") + " ; NumberRequest | " + number );
        }
        else if(info.get("type").equals("requestAnswer") && number == Integer.parseInt(info.get("NumberRequest"))){
            user.sendData(socket, "type | reserve ; username | " + info.get("username") + " ; NumberRequest | " + number + " ; terminalID | " + info.get("IDclient"));
            number++;
        }
        else if(info.get("type").equals("reserved")){
            System.out.println("GO TO TERMINAL " + info.get("IDclient") + " !");
        }
        else if(info.get("type").equals("authentication")){
//            if(h.verifyUser(info.get("username"), info.get("CCNUMBER"), info.get("PASSWORD")) == true){
                user.sendData(socket, "type | reserve ; username | " + info.get("username") + " ; NumberRequest | " + number + " ; terminalID | " + info.get("IDclient") + " ; userData | valid");
//           }
        }
    }


    public void run() {
        MulticastSocket socket = null;
        try {
            System.out.println("================================< " + this.getName() + " >=========================================");
           // InterfaceServerRMI h = (InterfaceServerRMI) LocateRegistry.getRegistry(7000).lookup("RMI Server");
            //ClientRMI client = new ClientRMI(this.getName());
            //h.saveClients(this.getName(), (InterfaceClientRMI) client);
            
            socket = new MulticastSocket(PORT);  
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);

            while (true) {                
                dataReceived = receiveData(socket);
                analyseData(socket, dataReceived/*, h*/);
            } 
        } catch (IOException e) { e.printStackTrace();} //catch (NotBoundException e) {e.printStackTrace();}
    }

    public static void main(String[] args) throws IOException {
        server = new MulticastServer();  //THREAD RECEBE
        server.start();
        user = new MulticastUser();  //THREAD ENVIA
        user.start();
    }
}

class MulticastUser extends Thread {
    private String MULTICAST_ADDRESS = "224.0.224.1";
    private int PORT = 7000;
    MulticastServer server;
    boolean verify = false;

    InputStreamReader input = new InputStreamReader(System.in);
    BufferedReader reader = new BufferedReader(input);
    Scanner keyboardScanner = new Scanner(System.in);

    public MulticastUser() {
        super("SERVER " + (long) (Math.random() * 1000));
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
                    System.out.println("\n\n\n\n\n\n\n");
                }
                else{
                    String teste = reader.readLine();
                    if(teste.equals("1")){
                        System.out.print("NAME: ");
                        String aux = reader.readLine();
                        sendToServer(socket, "type | login ; username | " + aux );  
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
