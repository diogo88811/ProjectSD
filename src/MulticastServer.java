import java.net.MulticastSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

class Data{
    private volatile String id;
    private volatile boolean state;

    public synchronized void setID(String id){
        this.id = id;
    }
    public synchronized void setState(boolean state){
        this.state = state;
    }
    public synchronized boolean getState(){
        return this.state;
    }
    public synchronized String getID(){
        return this.id;
    }
}

public class MulticastServer extends Thread {

    private String MULTICAST_ADDRESS = "224.0.224.0";
    private int PORT = 7000;
    private static MulticastServer server;
    private static MulticastUser user;
    Scanner keyboardScanner = new Scanner(System.in);
    String a;
    int number = 0;
    int id = 0;
    HashMap<String, String> info = new HashMap<String, String>();


    public MulticastServer(ArrayList<Data> data) {
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
    
    public void analyseData(String data){

        String aux[] = data.split("[;]");
        for(String a : aux){
            String types[] = a.split("\\|");
            info.put(types[0].trim(), types[1].trim());
        }
    }


    public void run() {
        MulticastSocket socket = null;
        try {
            System.out.println("================================< " + this.getName() + " >=========================================");
            
            socket = new MulticastSocket(PORT);  
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);

            while (true) {                
                a = receiveData(socket);
                analyseData(a);

                if(info.get("type").equals("login")){
                    user.sendData(socket, "type | request ; username | " + info.get("username") + " ; NumberRequest | " + number );
                }
                else if(info.get("type").equals("requestAnswer") && number == Integer.parseInt(info.get("NumberRequest"))){
                    user.sendData(socket, "type | reserve ; terminalID | " + info.get("IDclient") + " ; NumberRequest | " + number + " ; username | " + info.get("username"));
                    number++;
                }
                else if(info.get("type").equals("reserved")){
                    System.out.println("GO TO TERMINAL " + info.get("IDclient") + " !");
                }
                else if(info.get("type").equals("authentication")){
                    //VERIFICA DADOS NO RMI
                }
            } 
        } catch (IOException e) { e.printStackTrace();}
    }

    public static void main(String[] args) throws IOException {
        ArrayList<Data> data = new ArrayList<Data>();
        server = new MulticastServer(data);
        server.start();
        user = new MulticastUser(data);
        user.start();
    }
}

class MulticastUser extends Thread {
    private String MULTICAST_ADDRESS = "224.0.224.1";
    private int PORT = 7000;
    ArrayList<Data> data;
    MulticastServer server;

    InputStreamReader input = new InputStreamReader(System.in);
    BufferedReader reader = new BufferedReader(input);
    Scanner keyboardScanner = new Scanner(System.in);

    public MulticastUser( ArrayList<Data> data) {
        super("SERVER " + (long) (Math.random() * 1000));
        this.data = data;
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
            System.out.println("1.LOGIN");
            while (true) {
                String teste = reader.readLine();
                if(teste.equals("1")){
                    System.out.print("NAME: ");
                    String aux = reader.readLine();
                    sendToServer(socket, "type | login ; username | " + aux );  
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }  finally {
            socket.close();
        }
        
    }   
}
