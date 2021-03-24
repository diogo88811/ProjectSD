import com.sun.security.jgss.GSSUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.Scanner;

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

    public Pessoa() {

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
        return    "USERNAME: "       +this.nome         +
                "\nPASSWORD: "     + this.password     +
                "\nJOB: "          + this.trabalho     +
                "\nDEPARTAMENTO: " + this.departamento +
                "\nTELE: "         + this.telemovel    +
                "\nADRESS: "       + this.morada       +
                "\nCCNUMBER: "     + this.CCnumber     +
                "\nCCVAL: "        + this.CCVal        ;
    }

    public void RegisterPerson() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);
        Scanner scan = new Scanner(System.in);
        Pessoa pessoa = new Pessoa();
        String in;
        int num;

        System.out.print("Insira o nome:");
        in = reader.readLine();
        this.nome = in;

        System.out.print("Insira o contacto telefónico:");
        in = reader.readLine();
        this.telemovel = in;

        System.out.println("Insira a morada:");
        in = reader.readLine();

        this.morada = in;

        System.out.println("Insira número do CC:");
        in = reader.readLine();

        this.CCnumber = in;

        System.out.println("Insira validade dd/mm/aaaa do CC:");
        in = reader.readLine();

        this.CCVal = in;

        System.out.println("Cargo:");
        System.out.println("1 - Estudante");
        System.out.println("2 - Docente");
        System.out.println("3 - Funcionario");
        num = scan.nextInt();
        switch(num){
            case 1:
                this.trabalho = "Estudante";
                break;
            case 2:
                this.trabalho = "Docente";
            case 3:
                this.trabalho = "Funcionario";
        }

        System.out.println("Insira o departamento que frequenta:");
        in = reader.readLine();

        this.departamento = in;

        System.out.println("Insira uma password:");
        in = reader.readLine();

        this.password = in;

    }
}
