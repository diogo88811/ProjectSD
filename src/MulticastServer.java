import java.net.MulticastSocket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


public class MulticastServer extends Thread {

    //"224.0.224.0"
    private String MULTICAST_ADDRESS;
    private int PORT = 7000;
    private static MulticastServer server;
    private static MulticastUser user;
    Scanner keyboardScanner = new Scanner(System.in);
    String dataReceived;
    int number = 0;
    boolean state = true;
    InterfaceServerRMI h;

    
    public MulticastServer(InterfaceServerRMI h, String MULTICAST_ADDRESS) {
        super("MESA " + (long) (Math.random() * 1000));
        this.h = h;
        this.MULTICAST_ADDRESS = MULTICAST_ADDRESS;
    }

    public String receiveData(MulticastSocket socket) throws IOException{
        byte[] buffer = new byte[256];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);
        String message = new String(packet.getData(), 0, packet.getLength());
        return message;
    }

    public void analyseData(MulticastSocket socket, String data, HashMap<String, String> info) throws IOException, NotBoundException {

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
                try {

                    if(h.verifyUser(info.get("username"), info.get("ccNumber"), info.get("PASSWORD"))){
                        user.sendData(socket, "type | vote ; username | " + info.get("username") + " ; ccNumber | " + info.get("ccNumber") + " ; NumberRequest | " + number + " ; terminalID | " + info.get("IDclient") + " ; userData | valid" + " ; eleicao | " + info.get("eleicao") + " ; tamanhoLista | " + info.get("tamanhoLista") + " ; serverName | " + user.getName());
                    }
                    else{
                        System.out.println("UTILIZADOR NAO ESTA REGISTADO, POR FAVOR REGISTE-SE NA NOSSA PLATAFORMA !");
                        user.sendData(socket, "type | restart ; state | true" + " ; terminalID | " + info.get("IDclient") + " ; serverName | " + user.getName());
                    }
                }catch (Exception e){

                    h = (InterfaceServerRMI) LocateRegistry.getRegistry(7000).lookup("RMI Server");
                    if(h.verifyUser(info.get("username"), info.get("ccNumber"), info.get("PASSWORD"))){
                        user.sendData(socket, "type | vote ; username | " + info.get("username") + " ; ccNumber | " + info.get("ccNumber") + " ; NumberRequest | " + number + " ; terminalID | " + info.get("IDclient") + " ; userData | valid" + " ; eleicao | " + info.get("eleicao") + " ; tamanhoLista | " + info.get("tamanhoLista") + " ; serverName | " + user.getName());
                    }
                    else{
                        System.out.println("UTILIZADOR NAO ESTA REGISTADO, POR FAVOR REGISTE-SE NA NOSSA PLATAFORMA !");
                        user.sendData(socket, "type | restart ; state | true" + " ; terminalID | " + info.get("IDclient") + " ; serverName | " + user.getName());
                    }
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
                try{
                    h.saveVotes(info.get("eleicao"),info.get("voto"));
                    //for(int i = 0; i< h.getAdminClients().size(); i++){
                        h.print_on_server(info.get("username") + " VOTO " + info.get("voto"));
                    //}
                    state = true;
                }catch (Exception e){
                    h = (InterfaceServerRMI) LocateRegistry.getRegistry(7000).lookup("RMI Server");
                    h.saveVotes(info.get("eleicao"),info.get("voto"));
                    //for(int i = 0; i< h.getAdminClients().size(); i++){
                        h.print_on_server(info.get("username") + " VOTO " + info.get("voto"));
                    //}
                    state = true;
                }
            }
        }
        else if(info.get("type").equals("voteDone")){
            if(user.getName().equals(info.get("serverName"))){
                try{
                    h.saveUserVote(info.get("username"), info.get("ccNumber"),info.get("eleicao"));
                    h.saveVotedPlaceOnPeople(info.get("username"), info.get("ccNumber"), this.getName());
                 
                }catch(Exception e){
                    h = (InterfaceServerRMI) LocateRegistry.getRegistry(7000).lookup("RMI Server");
                    h.saveUserVote(info.get("username"), info.get("ccNumber"), info.get("eleicao"));
                    h.saveVotedPlaceOnPeople(info.get("username"), info.get("ccNumber"), this.getName());
                }
            }
        }
    }

    public void run() {
        MulticastSocket socket = null;
        try {
            System.out.println("______________________________< " + this.getName() + " >________________________________________");
            ClientRMI client = new ClientRMI(this.getName());
            try{
                h.saveClients(this.getName(), (InterfaceClientRMI) client);
                h.notifyClient(this.getName(), " -> on");
            }catch (Exception e ){
                h = (InterfaceServerRMI) LocateRegistry.getRegistry(7000).lookup("RMI Server");
                h.saveClients(this.getName(), (InterfaceClientRMI) client);
                h.notifyClient(this.getName(), " -> on");
            }
            socket = new MulticastSocket(PORT);
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);
            HashMap<String, String> info = new HashMap<String, String>();

            while (true) {
                dataReceived = receiveData(socket);
                analyseData(socket, dataReceived, info);
            }
        } catch (IOException | NotBoundException e) { e.printStackTrace();}
    }

    public static void main(String[] args) throws IOException, NotBoundException {
        InterfaceServerRMI h = (InterfaceServerRMI) LocateRegistry.getRegistry(7000).lookup("RMI Server");
        server = new MulticastServer(h, args[0]);  //THREAD RECEBE
        server.start();
        user = new MulticastUser(h, server, args[0]);  //THREAD ENVIA
        user.start();
        Runtime.getRuntime().addShutdownHook(new RuntimeDemo(h, server));
    }
}

class MulticastUser extends Thread {
    // "224.0.224.0"
    private String MULTICAST_ADDRESS;
    private int PORT = 7000;
    MulticastServer server;
    boolean verify = false;
    InterfaceServerRMI h;

    InputStreamReader input = new InputStreamReader(System.in);
    BufferedReader reader = new BufferedReader(input);
    Scanner keyboardScanner = new Scanner(System.in);

    public MulticastUser(InterfaceServerRMI h, MulticastServer server, String MULTICAST_ADDRESS) {
        super("MESA " + (long) (Math.random() * 1000));
        this.h = h;
        this.server = server;
        this.MULTICAST_ADDRESS = MULTICAST_ADDRESS;
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
            sleep(1000);
            System.out.print("\nIDENTIFICA O DEPARTAMENTO DESTA MESA DE VOTO: ");
            String dep = reader.readLine();
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
                    try{
                        h.verifyLogin(aux, cc);
                    }catch (Exception e){
                        h = (InterfaceServerRMI) LocateRegistry.getRegistry(7000).lookup("RMI Server");
                        h.verifyLogin(aux, cc);
                    }
                    System.out.println("SELECIONA A ELEICAO EM QUE QUER VOTAR:");
                    String eleicao;
                    int tamanho;
                    try{
                        for(int i = 0; i< h.getEleicoes().size(); i++){
                           if(h.verifyUserinArray(aux, cc, h.getEleicoes().get(i)) == false){
                                System.out.println("\t<" + i + "> " + h.getEleicoes().get(i).getNome());
                            }
                        }
                        
                        int numEleicoes = keyboardScanner.nextInt();
                        eleicao =  h.getEleicoes().get(numEleicoes).getNome();
                        tamanho = h.getEleicoes().get(numEleicoes).getListas().size();
                        sendToServer(socket, "type | login ; username | " + aux + " ; ccNumber | " + cc + " ; eleicao | " + eleicao + " ; tamanhoLista | " + tamanho + " ; serverName | " + getName());
                        
                    }catch (Exception e){
                        h = (InterfaceServerRMI) LocateRegistry.getRegistry(7000).lookup("RMI Server");
                        for(int i = 0; i< h.getEleicoes().size(); i++){
                            if(h.verifyUserinArray(aux, cc, h.getEleicoes().get(i)) == false){
                                System.out.println("\t<" + i + "> " + h.getEleicoes().get(i).getNome());
                            }
                        }
                        int numEleicoes = keyboardScanner.nextInt();
                        eleicao =  h.getEleicoes().get(numEleicoes).getNome();
                        tamanho = h.getEleicoes().get(numEleicoes).getListas().size();
                        sendToServer(socket, "type | login ; username | " + aux + " ; ccNumber | " + cc + " ; eleicao | " + eleicao + " ; tamanhoLista | " + tamanho + " ; serverName | " + getName());
                    }
                }
            }
        } catch (IOException | NotBoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
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
