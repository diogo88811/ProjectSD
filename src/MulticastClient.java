import java.net.MulticastSocket;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MulticastClient extends Thread {
    private String MULTICAST_ADDRESS = "224.0.224.1";

    private static MulticastUserClient user;
    private static MulticastClient client;
    boolean state = true;
    private int PORT = 7000;
    HashMap<String, String> info = new HashMap<String, String>();
    InputStreamReader input = new InputStreamReader(System.in);
    BufferedReader reader = new BufferedReader(input);
    Scanner keyboardScanner = new Scanner(System.in);


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

        String aux[] = data.split("[;]");

        for(String a : aux){
            String types[] = a.split("\\|");
            info.put(types[0].trim(), types[1].trim());
        }

        if(info.get("type").equals("request") && state == true){
            user.sendData(socket, "type | requestAnswer ; NumberRequest | " + info.get("NumberRequest") + " ; IDclient | " + user.getName() + " ; msg | FREE ; eleicao | " + info.get("eleicao") + " ; tamanhoLista | " + info.get("tamanhoLista"), false);
        }

        else if(info.get("type").equals("reserve")){
            if(user.getName().equals(info.get("terminalID"))){
                user.sendData(socket, "type | reserved ; IDclient | " + user.getName() + " ; eleicao | " + info.get("eleicao") + " ; IDclient | " + user.getName() + " ; tamanhoLista | " + info.get("tamanhoLista"), false);
                state = false;
                System.out.print("NOME : " + info.get("username"));
                System.out.print("\nNUMERO CC : " + info.get("ccNumber"));
                System.out.print("\nPASSWORD: ");
                String password = reader.readLine();
                user.sendData(socket, "type | authentication ; username | " + info.get("username") + " ; IDclient | " + user.getName() + " ; ccNumber | " + info.get("ccNumber") + " ; PASSWORD | " + password + " ; eleicao | " + info.get("eleicao") + " ; tamanhoLista | " + info.get("tamanhoLista"), false);
            }
        }

        else if(info.get("type").equals("vote") && info.get("userData").equals("valid")){
            if(user.getName().equals(info.get("terminalID"))){
                System.out.println("\nBEM VINDO, " + info.get("username") + " !\n");
                user.sendData(socket, "type | item_listRequire ; username | " + info.get("username") +  " ; IDclient | " + user.getName() + " ; eleicao | " + info.get("eleicao") + " ; tamanhoLista | " + info.get("tamanhoLista"), false);
            }
        }

        else if(info.get("type").equals("item_list")){
            if(user.getName().equals(info.get("terminalID"))){
                System.out.println("LISTA DE CANDIDATOS");
                for(int i = 0 ; i< Integer.parseInt(info.get("item_count")) ; i++){
                    System.out.println("<" + i + "> " + info.get("item_" + i + "_name"));
                }

                int voto = keyboardScanner.nextInt();
                user.sendData(socket, "type | done ; username | " + info.get("username") + " ; IDclient | " + user.getName() + " ; voto | " + info.get("item_" + voto + "_name"), false);
                state = true;
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
