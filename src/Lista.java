import java.io.Serializable;
import java.util.ArrayList;

public class Lista implements Serializable  {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    ArrayList<Pessoa> pessoas = new ArrayList<Pessoa>();
    Pessoa candidatoPrincipal;
    String nomeLista;
    int numVotes;

    public Lista() {
    }

    public Lista(ArrayList<Pessoa> pessoas, Pessoa candidatoPrincipal, String nomeLista) {

        this.pessoas = pessoas;
        this.candidatoPrincipal = candidatoPrincipal;
        this.nomeLista = nomeLista;
    }

    public void setPessoas(ArrayList<Pessoa> pessoas) {
        this.pessoas = pessoas;
    }

    public void setCandidatoPrincipal(Pessoa candidatoPrincipal) {
        this.candidatoPrincipal = candidatoPrincipal;
    }

    public void setNomeLista(String nomeLista) {
        this.nomeLista = nomeLista;
    }

    public ArrayList<Pessoa> getPessoas() {
        return pessoas;
    }

    public Pessoa getCandidatoPrincipal() {
        return candidatoPrincipal;
    }

    public String getNomeLista() {
        return nomeLista;
    }

    public int getNumVotes() {
        return numVotes;
    }

    public void setNumVotes(int numVotes) {
        this.numVotes = numVotes;
    }

    @Override
    public String toString() {
        return "Lista{" +
                "pessoas=" + pessoas +
                ", candidatoPrincipal=" + candidatoPrincipal +
                ", nomeLista='" + nomeLista + '\'' +
                '}';
    }


    public void manageCandidateList(){
        if(this == null){
            System.out.println("NAO EXISTEM LISTAS DISPONIVEIS !");
        }
        else{
            System.out.println("CANDIDATO PRINCIPAL: ");
            System.out.println(this.getCandidatoPrincipal().nome);
            System.out.println("NUMERO DE VOTOS: ");
            System.out.println(this.getNumVotes());
            System.out.println("OUTRAS PESSOAS DA LISTA: ");
            for(Pessoa p : this.getPessoas()){
                System.out.println(p.getNome());
            }
        }
    }
}
