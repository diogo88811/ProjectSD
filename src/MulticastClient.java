import java.net.MulticastSocket;
import java.util.Scanner;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.io.IOException;

public class MulticastClient extends Thread {
    private String MULTICAST_ADDRESS = "224.0.224.1";
    private int PORT = 7000;

    
    public MulticastClient() {
        super("CLIENT " + (long) (Math.random() * 1000));
    }
    public static void main(String[] args) throws IOException {
        MulticastClient client = new MulticastClient();
        client.start();
        MulticastUserClient user = new MulticastUserClient();
        user.start();
    }

    public void sendData() throws IOException{
        MulticastSocket socket = new MulticastSocket(); 
        Scanner keyboardScanner = new Scanner(System.in);
        String readKeyboard = keyboardScanner.nextLine();
        byte[] buffer = readKeyboard.getBytes();
        InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
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
            while(true){
                receiveData();
            }
        } catch (IOException e) { e.printStackTrace();}
    }
}

class MulticastUserClient extends Thread {
    private String MULTICAST_ADDRESS = "224.0.224.0";
    private int PORT = 7000;

    public MulticastUserClient() {
        super("CLIENT " + (long) (Math.random() * 1000));
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
