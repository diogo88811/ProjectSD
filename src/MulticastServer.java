import java.net.MulticastSocket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Scanner;


public class MulticastServer extends Thread {

    private String MULTICAST_ADDRESS = "224.0.224.0";
    private int PORT = 7000;
    private static MulticastServer server;
    private static MulticastUser user;
    Scanner keyboardScanner = new Scanner(System.in);
    String dataReceived;
    int number = 0;
    boolean state = true;
    InterfaceServerRMI h;
    TimeCount time;

    
    public MulticastServer(InterfaceServerRMI h, TimeCount time) {
        super("SERVER " + (long) (Math.random() * 1000));
        this.h = h;
        this.time = time;
    }

    public String receiveData(MulticastSocket socket) throws IOException{
        byte[] buffer = new byte[256];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);
        String message = new String(packet.getData(), 0, packet.getLength());
        return message;
    }

    public void analyseData(MulticastSocket socket, String data, HashMap<String, String> info) throws IOException{

        String aux[] = data.split("[;]");
        for(String a : aux){
            String types[] = a.split("\\|");
            info.put(types[0].trim(), types[1].trim());
        }
        
        if(info.get("type").equals("login")){ 
            if(user.getName().equals(info.get("serverName"))){
                user.sendData(socket, "type | request ; username | " + info.get("username") + " ; ccNumber | " + info.get("ccNumber") + " ; NumberRequest | " + number + " ; eleicao | " + info.get("eleicao") + " ; tamanhoLista | " + info.get("tamanhoLista") + " ; serverName | " + user.getName());
            }
        }
        else if(info.get("type").equals("requestAnswer") && number == Integer.parseInt(info.get("NumberRequest"))){
            if(user.getName().equals(info.get("serverName"))){
                user.sendData(socket, "type | reserve ; username | " + info.get("username") + " ; ccNumber | " + info.get("ccNumber") + " ; NumberRequest | " + number + " ; terminalID | " + info.get("IDclient") + " ; eleicao | " + info.get("eleicao") + " ; tamanhoLista | " + info.get("tamanhoLista") + " ; serverName | " + user.getName());
                number++;
            }
        }
        else if(info.get("type").equals("reserved")){
            if(user.getName().equals(info.get("serverName"))){
                System.out.println("GO TO TERMINAL " + info.get("IDclient") + " !");
                System.out.println("________________________________________________________________________");
            }

        }
        else if(info.get("type").equals("authentication")){
            if(user.getName().equals(info.get("serverName"))){
                if(h.verifyUser(info.get("username"), info.get("ccNumber"), info.get("PASSWORD")) == true){
                    user.sendData(socket, "type | vote ; username | " + info.get("username") + " ; ccNumber | " + info.get("ccNumber") + " ; NumberRequest | " + number + " ; terminalID | " + info.get("IDclient") + " ; userData | valid" + " ; eleicao | " + info.get("eleicao") + " ; tamanhoLista | " + info.get("tamanhoLista") + " ; serverName | " + user.getName());
                }
                else{
                    System.out.println("UTILIZADOR NAO ESTA REGISTADO, POR FAVOR REGISTE-SE NA NOSSA PLATAFORMA !");
                    user.sendData(socket, "type | restart ; state | true" + " ; terminalID | " + info.get("IDclient") + " ; serverName | " + user.getName());
                }
            }
        }
        else if(info.get("type").equals("item_listRequire")){
            if(user.getName().equals(info.get("serverName"))){
                ArrayList <Lista> listaEleicao = null ;

                String lista = "type | item_list ; item_count | " + info.get("tamanhoLista") + " ; ";

                ArrayList<Eleicao> eleicoes = h.getEleicoes();

                for (int i = 0; i< eleicoes.size(); i++){
                    if(eleicoes.get(i).getNome().equals(info.get("eleicao"))){
                        Eleicao eleicao = eleicoes.get(i);
                        listaEleicao = eleicao.getListas();
                    }
                }
                for(int i = 0; i< Integer.parseInt(info.get("tamanhoLista")); i++){
                    lista += "item_" + i + "_name | " + listaEleicao.get(i).getNomeLista() + " ; ";
                }
                user.sendData(socket, lista + "username | " + info.get("username") + " ; ccNumber | " + info.get("ccNumber") + " ; terminalID | " + info.get("IDclient") + " ; eleicao | " + info.get("eleicao") + " ; tamanhoLista | " + info.get("tamanho") + " ; serverName | " + user.getName());
            }
        }
        else if(info.get("type").equals("done")){
            if(user.getName().equals(info.get("serverName"))){
                h.saveVotes(info.get("eleicao"),info.get("voto"));
                h.print_on_server(info.get("username") + " VOTO " + info.get("voto"));
                state = true;
            }
        }
    }

    public void run() {
        MulticastSocket socket = null;
        try {
            System.out.println("================================< " + this.getName() + " >=========================================");

            ClientRMI client = new ClientRMI(this.getName());
            h.saveClients(this.getName(), (InterfaceClientRMI) client);
            h.notifyClient("MESA " + this.getName(), " -> on");

            socket = new MulticastSocket(PORT);
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);
            HashMap<String, String> info = new HashMap<String, String>();

            while (true) {
                dataReceived = receiveData(socket);
                analyseData(socket, dataReceived, info);
            }
        } catch (IOException e) { e.printStackTrace();}
    }

    public static void main(String[] args) throws IOException, NotBoundException {
        InterfaceServerRMI h = (InterfaceServerRMI) LocateRegistry.getRegistry(7000).lookup("RMI Server");
        TimeCount time = new TimeCount();
        time.start();
        server = new MulticastServer(h, time);  //THREAD RECEBE
        server.start();
        user = new MulticastUser(h, server, time);  //THREAD ENVIA
        user.start();
        Runtime.getRuntime().addShutdownHook(new RuntimeDemo(h, server));
    }
}

class MulticastUser extends Thread {
    private String MULTICAST_ADDRESS = "224.0.224.1";
    private int PORT = 7000;
    MulticastServer server;
    boolean verify = false;
    InterfaceServerRMI h;
    TimeCount time;

    InputStreamReader input = new InputStreamReader(System.in);
    BufferedReader reader = new BufferedReader(input);
    Scanner keyboardScanner = new Scanner(System.in);

    public MulticastUser(InterfaceServerRMI h, MulticastServer server, TimeCount time) {
        super("SERVER " + (long) (Math.random() * 1000));
        this.h = h;
        this.server = server;
        this.time = time;
    }

    public void sendData(MulticastSocket socket, String data) throws IOException{
        String aux = data + " " ;
        byte[] buffer = aux.getBytes();
        InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);
        socket.send(packet);
    }

    public void sendToServer(MulticastSocket socket, String data) throws IOException{
        String aux = data + " " ;
        byte[] buffer = aux.getBytes();
        InetAddress group = InetAddress.getByName("224.0.224.0");
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);
        socket.send(packet);
    }

    public void run() {
        MulticastSocket socket = null;
        try {
            socket = new MulticastSocket();
            System.out.println("<1> - LOGIN NA MESA DE VOTO");
            while (true) {
                System.out.print(">");
                String teste = reader.readLine();
                if(teste.equals("1")){
                    server.state = false;
                    System.out.print("NAME: ");
                    String aux = reader.readLine();
                    System.out.print("CC NUMBER: ");
                    String cc = reader.readLine();
                    h.verifyLogin(aux, cc);
                    System.out.println("SELECIONA A ELEICAO EM QUE QUER VOTAR:");

                    for(int i = 0; i< h.getEleicoes().size(); i++){
                        System.out.println("\t<" + i + "> " + h.getEleicoes().get(i).getNome());
                    }
                    int numEleicoes = keyboardScanner.nextInt();
                    String eleicao =  h.getEleicoes().get(numEleicoes).getNome();
                    int tamanho = h.getEleicoes().get(numEleicoes).getListas().size();

                    sendToServer(socket, "type | login ; username | " + aux + " ; ccNumber | " + cc + " ; eleicao | " + eleicao + " ; tamanhoLista | " + tamanho + " ; serverName | " + getName());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } 
        finally {
            socket.close();
        }
    }
}

class RuntimeDemo extends Thread{

    InterfaceServerRMI h ;
    MulticastServer server;

    public RuntimeDemo(InterfaceServerRMI h, MulticastServer server){
        this.h = h;
        this.server = server;
    }
    public void run() {
        try {
            h.notifyClient("MESA " + server.getName(), " -> off");
        } catch (RemoteException e) {
            System.out.println("ERRO!");
        }
    }
}

class TimeCount extends Thread{
    String hour, minute, second;
  
    public void run(){
        while(true){
            LocalDateTime agora = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            String agoraFormatado = agora.format(formatter);

            String []todayTime = agoraFormatado.split(":");
            hour = todayTime[0];
            minute = todayTime[1];
            second = todayTime[2];
        }
    }
}