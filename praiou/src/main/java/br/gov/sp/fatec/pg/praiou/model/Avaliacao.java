package br.gov.sp.fatec.pg.praiou.model;

import java.time.LocalDate;

public class Avaliacao { 

    private Integer id;
    private Usuario avaliador; 
    private Evento evento;     // O EVENTO que está sendo avaliado
    private boolean curtida;   
    private LocalDate dataAvaliacao; 

    // Construtor Vazio
    public Avaliacao() {}

    // Construtor Básico 
    public Avaliacao(Usuario avaliador, Evento evento, boolean curtida) {
        this.avaliador = avaliador;
        this.evento = evento;
        this.curtida = curtida;
        this.dataAvaliacao = LocalDate.now();
    }

    // Construtor Completo 
    public Avaliacao(Integer id, Usuario avaliador, Evento evento, boolean curtida, LocalDate dataAvaliacao) {
        this.id = id;
        this.avaliador = avaliador;
        this.evento = evento;
        this.curtida = curtida;
        this.dataAvaliacao = dataAvaliacao;
    }

    // Getters e Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Usuario getAvaliador() { return avaliador; }
    public void setAvaliador(Usuario avaliador) { this.avaliador = avaliador; }

    public Evento getEvento() { return evento; }
    public void setEvento(Evento evento) { this.evento = evento; }

    public boolean isCurtida() { return curtida; }
    public void setCurtida(boolean curtida) { this.curtida = curtida; }

    public LocalDate getDataAvaliacao() { return dataAvaliacao; }
    public void setDataAvaliacao(LocalDate dataAvaliacao) { this.dataAvaliacao = dataAvaliacao; }
}
