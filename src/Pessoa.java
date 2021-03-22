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

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTrabalho() {
        return this.trabalho;
    }

    public void setTrabalho(String trabalho) {
        this.trabalho = trabalho;
    }

    public String getDepartamento() {
        return this.departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getTelemovel() {
        return this.telemovel;
    }

    public void setTelemovel(String telemovel) {
        this.telemovel = telemovel;
    }

    public String getMorada() {
        return this.morada;
    }

    public void setMorada(String morada) {
        this.morada = morada;
    }

    public String getCCnumber() {
        return this.CCnumber;
    }

    public void setCCnumber(String CCnumber) {
        this.CCnumber = CCnumber;
    }

    public String getCCVal() {
        return this.CCVal;
    }

    public void setCCVal(String CCVal) {
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
