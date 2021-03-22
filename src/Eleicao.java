import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.Scanner;

public class Eleicao implements Serializable {

    public String nome, DataInicio, DataFim, publicoAlvo;
    
    public Eleicao(String nome, String DataInicio, String DataFim, String publicoAlvo){
        this.nome = nome;
        this.DataInicio = DataInicio;
        this.DataFim = DataFim;
        this.publicoAlvo = publicoAlvo;
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


    public String toString(){//overriding the toString() method  
        return "NOME: "              + nome         + 
               "\nDATA INICIO: "     + DataInicio   + 
               "\nDATA FIM: "        + DataFim      + 
               "\nPUBLICO ALVO: "    + publicoAlvo  ;
       }

    public void createEleicao(){
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);
        Scanner scan = new Scanner(System.in);

        System.out.println("hello here");
    }
    
}
