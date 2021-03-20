import java.io.Serializable;

public class Pessoa implements Serializable {
    public String nome, password, trabalho, departamento, telemovel, morada, CCnumber, CCVal;
    
    public Pessoa(String nome, String password, String job, String telemovel, String morada, String CCnumber, String CCVal, String departamento){
        this.nome = nome;
        this.password = password;
        this.trabalho = job;
        this.departamento = departamento;
        this.telemovel = telemovel;
        this.morada = morada;
        this.CCnumber = CCnumber;
        this.CCVal = CCVal;
    }

    public String toString(){//overriding the toString() method  
        return "USERNAME: "       + nome         + 
               "\nPASSWORD: "     + password     + 
               "\nJOB: "          + trabalho     + 
               "\nDEPARTAMENTO: " + departamento + 
               "\nTELE: "         + telemovel    + 
               "\nADRESS: "       + morada       + 
               "\nCCNUMBER: "     + CCnumber     + 
               "\nCCVAL: "        + CCVal        ;
       }
}
