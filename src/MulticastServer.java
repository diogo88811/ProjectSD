import java.net.MulticastSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;



public class MulticastServer extends Thread {
    private String MULTICAST_ADDRESS = "224.0.224.0";
    private int PORT = 7000;
    Scanner keyboardScanner = new Scanner(System.in);
    String a;

    public MulticastServer() {
        super("SERVER " + (long) (Math.random() * 1000));
    }

    public String receiveData(MulticastSocket socket) throws IOException{

        byte[] buffer = new byte[256];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            String message = new String(packet.getData(), 0, packet.getLength());
            System.out.println(message);
            return message;
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
            } 
        } catch (IOException e) { e.printStackTrace();}
    }

    public static void main(String[] args) throws IOException {
        MulticastServer server = new MulticastServer();
        server.start();
        MulticastUser user = new MulticastUser();
        user.start();
    }
}

class MulticastUser extends Thread {
    private String MULTICAST_ADDRESS = "224.0.224.1";
    private int PORT = 7000;
    InputStreamReader input = new InputStreamReader(System.in);
    BufferedReader reader = new BufferedReader(input);
    Scanner keyboardScanner = new Scanner(System.in);

    public MulticastUser() {
        super("SERVER " + (long) (Math.random() * 1000));
    }

    public void sendData(MulticastSocket socket, String data) throws IOException{
        byte[] buffer = data.getBytes();
        InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);
        socket.send(packet);
    }

    public void run() {
        MulticastSocket socket = null;
        try {
            socket = new MulticastSocket();  
            while (true) {
                String data = reader.readLine();
                if(data.equals("login")){
                    System.out.println("\n\n");
                    System.out.print("Introduz o nome:");
                }
                sendData(socket, data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }   
}
