import java.io.Serializable;

public class Eleicao implements Serializable {

    public String nome, DataInicio, DataFim, publicoAlvo;
    
    public Eleicao(String nome, String DataInicio, String DataFim, String publicoAlvo){
        this.nome = nome;
        this.DataInicio = DataInicio;
        this.DataFim = DataFim;
        this.publicoAlvo = publicoAlvo;
    }

    public String toString(){//overriding the toString() method  
        return "NOME: "              + nome         + 
               "\nDATA INICIO: "     + DataInicio   + 
               "\nDATA FIM: "        + DataFim      + 
               "\nPUBLICO ALVO: "    + publicoAlvo  ;
       }
    
}
