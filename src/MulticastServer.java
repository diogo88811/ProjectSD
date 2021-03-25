import java.net.MulticastSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

class Terminal {
    private String id;
    private boolean state;
    private ArrayList<Terminal> terminals = new ArrayList<Terminal>();

    public Terminal(String id, boolean state) {
        this.id = id;
        this.state = state;
    }
    public Terminal(){

    }
    public String getId() {
        return this.id;
    }
    public boolean getState() {
        return this.state;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setState(boolean state) {
        this.state = state;
    }
    public boolean addTerminal( Terminal newTerminal ) {
        terminals.add( newTerminal );
        return true;
    }
    public ArrayList<Terminal> getTerminals(){
        return this.terminals;
    }
}

public class MulticastServer extends Thread {
    private String MULTICAST_ADDRESS = "224.0.224.0";
    private int PORT = 7000;
    private static MulticastServer server;
    private static MulticastUser user;
    Terminal terminal;
    Scanner keyboardScanner = new Scanner(System.in);
    String a;

    public MulticastServer(Terminal terminal) {
        super("SERVER " + (long) (Math.random() * 1000));
        this.terminal = terminal;

    }

    public String receiveData(MulticastSocket socket) throws IOException{

        byte[] buffer = new byte[256];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            String message = new String(packet.getData(), 0, packet.getLength());
            System.out.println(message);
            return message;
    }
    
    public void checkTerminal(String terminalID){
        String[] token = terminalID.split("[ ]");
        if(token[1] != null && token[2] != null){
            String terminalName = token[1] + " " + token[2];
            if(token[0]!= null && token[3] != null){
                if(token[0].equals("TERMINAL") && token[3].equals("AVAILABLE")){
                    terminal.addTerminal(new Terminal(terminalName, true));
                }
            }
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
                checkTerminal(a);
            } 
        } catch (IOException e) { e.printStackTrace();}
    }

    public static void main(String[] args) throws IOException {
        Terminal terminal = new Terminal();
        server = new MulticastServer(terminal);
        server.start();
        user = new MulticastUser(terminal);
        user.start();
    }
}

class MulticastUser extends Thread {
    private String MULTICAST_ADDRESS = "224.0.224.1";
    private int PORT = 7000;
    private static MulticastServer server;
    Terminal terminal;

    InputStreamReader input = new InputStreamReader(System.in);
    BufferedReader reader = new BufferedReader(input);
    Scanner keyboardScanner = new Scanner(System.in);

    public MulticastUser(Terminal terminal) {
        super("SERVER " + (long) (Math.random() * 1000));
        this.terminal = terminal;
    }

    public void sendData(MulticastSocket socket, String data) throws IOException{
        String name = choseTerminal();
        String aux = data + " " + name + " " ;
        System.out.println(aux);
        byte[] buffer = aux.getBytes();
        InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);
        socket.send(packet);
    }

    public String choseTerminal(){
        int k = 0;
        int f = 0;
        if(terminal.getTerminals().size() != 0){        
            while(k < terminal.getTerminals().size()){
                if(terminal.getTerminals().get(k).getState() == true){
                    terminal.getTerminals().get(k).setState(false);
                    f = k;
                    break;
                }
                k++;
            }
            return "GO TO TERMINAL " + terminal.getTerminals().get(f).getId() + " TO VOTE !" ;
        }
        else{
            return "";
        }
    }
    
    public void run() {
        MulticastSocket socket = null;
        try {
            socket = new MulticastSocket();  
            while (true) {
                String data = reader.readLine();
                if(data.equals("login")){
                    System.out.print("NAME: ");
                    String aux = reader.readLine();
                    sendData(socket, aux);  
                }
                
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }   
}
