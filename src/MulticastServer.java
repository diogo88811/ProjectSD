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
    

    
    public MulticastServer() {
        super("User " + (long) (Math.random() * 1000));
    }

    public void sendData() throws IOException{
        MulticastSocket socket = new MulticastSocket(); 
        Scanner keyboardScanner = new Scanner(System.in);
        String readKeyboard = keyboardScanner.nextLine();
        byte[] buffer = readKeyboard.getBytes();
        InetAddress group = InetAddress.getByName("224.0.224.1");
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);
        socket.send(packet);
    }

    public void receiveData() throws IOException{
        byte[] buffer = new byte[256];
        MulticastSocket socket = new MulticastSocket(PORT);  // create socket and bind it
        InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
        socket.joinGroup(group);
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);
        String message = new String(packet.getData(), 0, packet.getLength());
        System.out.println(message);
    }

    public void run() {
        try {
            String aux ;
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                
                // type | login ; username | pierre ; password | omidyar 
                aux = br.readLine();
                if(aux.equals("login")){
                    System.out.println("LOGIN");
                    System.out.print("Nome: ");
                    sendData();
                }
                aux = null;
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

    public MulticastUser() {
        super("SERVER " + (long) (Math.random() * 1000));
    }

    public void run() {
        MulticastSocket socket = null;
        System.out.println("================================< " + this.getName() + " >=========================================");
        try {
            socket = new MulticastSocket();  // create socket without binding it (only for sending)
            Scanner keyboardScanner = new Scanner(System.in);
            while (true) {
                String readKeyboard = keyboardScanner.nextLine();
                byte[] buffer = readKeyboard.getBytes();

                InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);
                socket.send(packet);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }
}
