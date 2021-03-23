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
    

    void receiveData(MulticastSocket socket) throws IOException{
        byte[] buffer = new byte[256];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);

        System.out.println("Received packet from " + packet.getAddress().getHostAddress() + ":" + packet.getPort() + " with message:");
        String message = new String(packet.getData(), 0, packet.getLength());
        System.out.println(message);
    }

    public static void main(String[] args) {
        MulticastServer server = new MulticastServer();
        server.start();
        MulticastUser user = new MulticastUser();
        user.start();
    }

    public void run() {
        MulticastSocket socket = null;
        try {
            socket = new MulticastSocket(PORT);  // create socket and bind it
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);
            while (true) {
                receiveData(socket);
            }
        } catch (IOException e) { e.printStackTrace();} finally { socket.close();}
    }
}

class MulticastUser extends Thread {
    private String MULTICAST_ADDRESS = "224.0.224.1";
    private int PORT = 7000;

    public MulticastUser() {
        super("User " + (long) (Math.random() * 1000));
    }

    public void sendData(MulticastSocket socket, String readKeyboard) throws IOException{
        byte[] buffer = readKeyboard.getBytes();
        InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);
        socket.send(packet);
    }

    public void run() {
        MulticastSocket socket = null;
        System.out.println(this.getName() + " ready...");
        try {
            socket = new MulticastSocket();  
            Scanner keyboardScanner = new Scanner(System.in);
            InputStreamReader input = new InputStreamReader(System.in);
            BufferedReader reader = new BufferedReader(input);
            while (true) {
                //type | login ; username | pierre ; password | omidyar
                String[] array = reader.readLine().split("\\|");
                String[] aux = array[1].split(";");
                String[] temp = aux[0].split(" ");
                if(temp[1].equals("login")){
                    System.out.println("============================LOGIN===============================");
                    System.out.print("Nome: ");
                    String readKeyboard = keyboardScanner.nextLine();
                    sendData(socket, readKeyboard);
                }
                
                String readKeyboard = keyboardScanner.nextLine();
                sendData(socket, readKeyboard);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }
}
