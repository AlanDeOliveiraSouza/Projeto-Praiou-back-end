package br.gov.sp.fatec.pg.praiou.model;

import java.time.LocalDate;

public class Avaliacao { 

    private Integer id;
    private Usuario avaliador;  
    private Usuario avaliado;   
    private Evento evento;      
    private boolean curtida;   
    private LocalDate dataAvaliacao; 

    public Avaliacao() {}

    public Avaliacao(Usuario avaliador, Usuario avaliado, Evento evento, boolean curtida) {
        this.avaliador = avaliador;
        this.avaliado = avaliado;
        this.evento = evento;
        this.curtida = curtida;
        this.dataAvaliacao = LocalDate.now();
    }

    
    public Avaliacao(Integer id, Usuario avaliador, Usuario avaliado, Evento evento, boolean curtida, LocalDate dataAvaliacao) {
        this.id = id;
        this.avaliador = avaliador;
        this.avaliado = avaliado;
        this.evento = evento;
        this.curtida = curtida;
        this.dataAvaliacao = dataAvaliacao;
    }


    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Usuario getAvaliador() { return avaliador; }
    public void setAvaliador(Usuario avaliador) { this.avaliador = avaliador; }

    public Usuario getAvaliado() { return avaliado; }
    public void setAvaliado(Usuario avaliado) { this.avaliado = avaliado; }

    public Evento getEvento() { return evento; }
    public void setEvento(Evento evento) { this.evento = evento; }

    public boolean isCurtida() { return curtida; }
    public void setCurtida(boolean curtida) { this.curtida = curtida; }

    public LocalDate getDataAvaliacao() { return dataAvaliacao; }
    public void setDataAvaliacao(LocalDate dataAvaliacao) { this.dataAvaliacao = dataAvaliacao; }
}
