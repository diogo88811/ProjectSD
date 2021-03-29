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

    @Override
    public String toString() {
        return "Lista{" +
                "pessoas=" + pessoas +
                ", candidatoPrincipal=" + candidatoPrincipal +
                ", nomeLista='" + nomeLista + '\'' +
                '}';
    }
}
