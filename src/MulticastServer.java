import java.net.MulticastSocket;
import java.net.UnknownHostException;
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

    public MulticastServer() {
        super("User " + (long) (Math.random() * 1000));
    }

    public void sendData(MulticastSocket socket, InetAddress group) throws IOException{
        String readKeyboard = keyboardScanner.nextLine();
        byte[] buffer = readKeyboard.getBytes();

        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);
        socket.send(packet);
    }

    public String receiveData(MulticastSocket socket, InetAddress group) throws IOException{
        byte[] buffer = new byte[256];

        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);

        String message = new String(packet.getData(), 0, packet.getLength());

        return message;
    }

    public void run() {
        MulticastSocket socket = null;
        try {
            socket = new MulticastSocket();
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);
            while (true) {

                // type | login ; username | pierre ; password | omidyar
                receiveData(socket, group);

            }
        } catch (IOException e) { e.printStackTrace();}
    }
    public static void main(String[] args) throws IOException {
        Scanner keyboardScanner = new Scanner(System.in);

        MulticastServer server = new MulticastServer();
        server.start();
        MulticastUser user = new MulticastUser();
        user.start();

    }
}


class MulticastUser extends Thread {
    private String MULTICAST_ADDRESS = "224.0.224.1";
    private int PORT = 7000;
    Scanner keyboardScanner = new Scanner(System.in);


    public MulticastUser() {
        super("SERVER " + (long) (Math.random() * 1000));
    }

    void sendData(MulticastSocket socket) throws IOException{
        String readKeyboard = keyboardScanner.nextLine();
        byte[] buffer = readKeyboard.getBytes();
        InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);

        socket.send(packet);
    }

    public void run() {
        MulticastSocket socket = null;
        System.out.println("================================< " + this.getName() + " >=========================================");
        try {
            socket = new MulticastSocket();  // create socket without binding it (only for sending)
            while (true) {
                sendData(socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }
}
