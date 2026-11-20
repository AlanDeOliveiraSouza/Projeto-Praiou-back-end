import java.util.ArrayList;
import java.util.List;

public class Cidade {
    private String sigla;
    private String nome;
    private String siglaEstado;
    private List<Praia> praias = new ArrayList<>();

    public Cidade() {
    }

    public Cidade(String sigla, String nome, String siglaEstado) {
        this.sigla = sigla;
        this.nome = nome;
        this.siglaEstado = siglaEstado;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSiglaEstado() {
        return siglaEstado;
    }

    public void setSiglaEstado(String siglaEstado) {
        this.siglaEstado = siglaEstado;
    }

    public List<Praia> getPraias() {
        return praias;
    }

    public void setPraias(List<Praia> praias) {
        this.praias = praias;
    }
}
