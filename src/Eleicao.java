import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;

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

        Lista l = new Lista();
        l.setCandidatoPrincipal(pessoa.get(0));
        this.listas.add(l);
        this.estado = true;

    }
    
}
