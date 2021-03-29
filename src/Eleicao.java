import jdk.swing.interop.SwingInterOpUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Scanner;

public class Eleicao implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public String nome, DataInicio, DataFim, publicoAlvo;
    boolean estado;
    public ArrayList<Lista> listas = new ArrayList<Lista>();
    
    public Eleicao(String nome, String DataInicio, String DataFim, String publicoAlvo, boolean estado,ArrayList<Lista> listas){
        this.nome = nome;
        this.DataInicio = DataInicio;
        this.DataFim = DataFim;
        this.publicoAlvo = publicoAlvo;
        this.estado = estado;
        this.listas = listas;
    }
    public  Eleicao(){

    }
    public String getNome() {
        return this.nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getDataInicio() {
        return this.DataInicio;
    }
    public void setDataInicio(String DataInicio) {
        this.DataInicio = DataInicio;
    }
    public boolean getEstado(){
        return this.estado;
    }
    public String getDataFim() {
        return this.DataFim;
    }
    public void setDataFim(String DataFim) {
        this.DataFim = DataFim;
    }
    public String getPublicoAlvo() {
        return this.publicoAlvo;
    }
    public void setPublicoAlvo(String publicoAlvo) {
        this.publicoAlvo = publicoAlvo;
    }
    public ArrayList<Lista> getListas() {
        return listas;
    }
    public void setListas(ArrayList<Lista> listas) {
        this.listas = listas;
    }
    @Override
    public String toString() {
        return "Eleicao{" +
                "nome='" + nome + '\'' +
                ", DataInicio='" + DataInicio + '\'' +
                ", DataFim='" + DataFim + '\'' +
                ", publicoAlvo='" + publicoAlvo + '\'' +
                ", estado=" + estado +
                ", listas=" + listas +
                '}';
    }

    public void createEleicao(ArrayList<Pessoa> pessoa) throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);
        String in;
        int numberOfLists;
        int numCandidate;
        Scanner scan = new Scanner(System.in);

        System.out.print("NOME ELEICAO: ");
        in = reader.readLine();
        this.nome = in;

        System.out.print("DATA INICIO (dd/mm/aaaa): ");
        in = reader.readLine();
        this.DataInicio = in;

        System.out.print("DATA FIM (dd/mm/aaaa): ");
        in = reader.readLine();
        this.DataFim = in;
        
        System.out.print("PUBLICO ALVO: ");
        in = reader.readLine();
        this.publicoAlvo = in;

        System.out.println("NUMERO DE LISTAS: ");
        numberOfLists = scan.nextInt();

        Lista l = new Lista();
        for(int i = 0; i < numberOfLists; i++){
            System.out.println("LISTA "+(i+1)+":");
            System.out.print("NOME DA LISTA: ");
            in = reader.readLine();
            l.setNomeLista(in);
            System.out.println("CANDIDATO PRINCIPAL: ");
            for(int k = 0; k < pessoa.size(); k++){
                System.out.println(k+" "+pessoa.get(k).getNome());
            }
            numCandidate = scan.nextInt();
            l.setCandidatoPrincipal(pessoa.get(numCandidate));
            while(true) {
                for (int k = 0; k < pessoa.size(); k++) {
                    if(!(l.getPessoas().contains(pessoa.get(k))))
                        System.out.println((k+1) + " " + pessoa.get(k).getNome());
                }
                numCandidate = scan.nextInt();
                if(numCandidate != 0) {
                    l.getPessoas().add(pessoa.get(numCandidate - 1));
                }else{
                    break;
                }
            }
        }

        this.listas.add(l);
        this.estado = true;

    }
    
}
