import java.net.MulticastSocket;
import java.util.Scanner;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.io.IOException;

public class MulticastClient extends Thread {
    private String MULTICAST_ADDRESS = "224.0.224.1";
    private int PORT = 7000;

    public static void main(String[] args) throws IOException {
        MulticastClient client = new MulticastClient();
        client.start();
        MulticastClientUser user = new MulticastClientUser();
        user.start();
    }

    public void run() {
        MulticastSocket socket = null;
        try {
            socket = new MulticastSocket(PORT);  // create socket and bind it
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);
            while (true) {
                byte[] buffer = new byte[256];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                System.out.println("Received packet from " + packet.getAddress().getHostAddress() + ":" + packet.getPort() + " with message:");
                String message = new String(packet.getData(), 0, packet.getLength());
                System.out.println(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }
}

class MulticastClientUser extends Thread {
    private String MULTICAST_ADDRESS = "224.0.224.0";
    private int PORT = 7000;
    static boolean state = true;
    int flag = 0;

    public MulticastClientUser() {
        super("User " + (long) (Math.random() * 1000));
    }

    void verify() throws IOException{
        if(state == true){
            MulticastSocket socket = new MulticastSocket(PORT);  
            InetAddress group = InetAddress.getByName("224.0.224.0");
            socket.joinGroup(group);
            byte[] buffer = "FREE".getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);
            socket.send(packet);
        }
    }

    public void run() {
        MulticastSocket socket = null;
        System.out.println(this.getName() + " ready...");
        try {
            socket = new MulticastSocket();  
            Scanner keyboardScanner = new Scanner(System.in);
            while (true) {
                if(flag == 0){
                    verify();
                    flag = 1;
                }

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