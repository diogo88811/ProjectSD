import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.Vector;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.io.IOException;

public class MulticastClient extends Thread {
    private String MULTICAST_ADDRESS = "224.0.224.1";
    
    private static MulticastUserClient user;
    private static MulticastClient client;

    private int PORT = 7000;
    
    public MulticastClient() {
        super("CLIENT " + (long) (Math.random() * 1000));
    }
    
    public String receiveData(MulticastSocket socket) throws IOException{
        byte[] buffer = new byte[256];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);
        String message = new String(packet.getData(), 0, packet.getLength());
        
        return message;
    }

    
    public String sendData(MulticastSocket socket, String msg) throws IOException{
        Scanner keyboardScanner = new Scanner(System.in);

        String a = keyboardScanner.nextLine();
        String aux = msg + a;

        byte[] buffer = aux.getBytes();

        InetAddress group = InetAddress.getByName("224.0.224.1");
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);
        socket.send(packet);

        return aux;
    }
    public void analyzeData(MulticastSocket socket, String data) throws IOException, InterruptedException{
        Scanner keyboardScanner = new Scanner(System.in);

        if(data.equals("Miguel")){
            System.out.print("PASSWORD: ");
            user.sendData(socket, "type | login ; username | " + data + " ; password | ");           
        }
    }

    public void run() {
        MulticastSocket socket = null;
        try {
            socket = new MulticastSocket(PORT);  // create socket and bind it
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);
            String data;
            while (true) {
                data = receiveData(socket);
                analyzeData(socket, data);
            } 
        }catch (IOException e) { e.printStackTrace();} catch (InterruptedException e) { e.printStackTrace();}
    }

    public static void main(String[] args) throws IOException {
        client = new MulticastClient();
        client.start();
        user = new MulticastUserClient();
        user.start();
    }
}

class MulticastUserClient extends Thread {
    private String MULTICAST_ADDRESS = "224.0.224.0";
    private int PORT = 7000;
    boolean stateTerminal = true;


    public MulticastUserClient() {
        super("CLIENT " + (long) (Math.random() * 1000));
    }

    public String sendData(MulticastSocket socket, String msg) throws IOException{
        Scanner keyboardScanner = new Scanner(System.in);

        String a = keyboardScanner.nextLine();
        String aux = msg + a;
        byte[] buffer = aux.getBytes();

        InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);
        socket.send(packet);

        return aux;
    }

    public boolean getStateTerminal() {
        return this.stateTerminal;
    }

    public void setStateTerminal(boolean stateTerminal) {
        this.stateTerminal = stateTerminal;
    }

    public String getNome(){
        return this.getName();
    }

    public void run() {
        MulticastSocket socket = null;
        System.out.println("================================< " + this.getName() + " >=========================================");
        try {
            socket = new MulticastSocket(); 
            Scanner keyboardScanner = new Scanner(System.in);
            while (true) {
                //fefe
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }
}
