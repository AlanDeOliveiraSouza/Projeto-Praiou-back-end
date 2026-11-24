package br.gov.sp.fatec.pg.praiou.model;

public class Praia {
    private Integer id;
    private String nome;
    private String siglaCidade;

    public Praia() {
    }

    public Praia(Integer id, String nome, String siglaCidade) {
        this.id = id;
        this.nome = nome;
        this.siglaCidade = siglaCidade;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSiglaCidade() {
        return siglaCidade;
    }

    public void setSiglaCidade(String siglaCidade) {
        this.siglaCidade = siglaCidade;
    }

}
