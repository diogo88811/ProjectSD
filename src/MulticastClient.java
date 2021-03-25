import java.net.MulticastSocket;
import java.util.Scanner;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.io.IOException;

public class MulticastClient extends Thread {
    private String MULTICAST_ADDRESS = "224.0.224.1";
    
    private static MulticastUserClient user;
    private static MulticastClient client;
    private static MulticastServer server;
    Terminal terminal;
    private boolean state = true;
    private int PORT = 7000;
    int flag = 0;
    
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
   
    public void analyzeData(MulticastSocket socket, String data) throws IOException, InterruptedException{
        if(state == true){
            String[] aux = data.split("[ ]");
            String terminal = aux[4] + " " + aux[5];
            if(aux[0].equals("Miguel")){
                if(user.getName().equals(terminal)){
                    state = false;
                    System.out.print("PASSWORD: ");
                    user.sendData(socket, "type | login ; username | " + aux[0] + " ; password | ", true);    
                }   
            }   
        }else{
            user.sendData(socket, "TERMINAL OCUPIED !", false);    
        }       
    }

    public boolean getClientState() {
        return this.state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    private void verifyClient(MulticastSocket socket) throws IOException {
        if(flag == 0){
            if(getClientState() == true){
                String aux = "TERMINAL " + user.getName() + " AVAILABLE !" ;
                user.sendData(socket, aux, false);
                flag = 1;
            }            
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
                verifyClient(socket);
                data = receiveData(socket);
                analyzeData(socket, data);
                //System.out.println(newTerminal.getTerminals().get(0).getId());
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


    public MulticastUserClient() {
        super("CLIENT " + (long) (Math.random() * 1000));
    }

    public String sendData(MulticastSocket socket, String msg, boolean premission) throws IOException{
        Scanner keyboardScanner = new Scanner(System.in);
        
        if(premission == true){
            String a = keyboardScanner.nextLine();
            String aux = msg + a;

            byte[] buffer = aux.getBytes();

            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);
            socket.send(packet);

            return aux;
        }
        else{
            String aux = msg ;

            byte[] buffer = aux.getBytes();

            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);
            socket.send(packet);

            return aux;
        }
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
