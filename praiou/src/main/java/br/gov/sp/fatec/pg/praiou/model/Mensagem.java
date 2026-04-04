package br.gov.sp.fatec.pg.praiou.model;

import java.util.Date;
import java.time.LocalDate;

public class Mensagem {

    private Integer id;
    private Usuario remetente;
    private Evento evento; 
    private String conteudo; 
    private LocalDate dataEnvio;
    private LocalDate dataEdicao;    

    // Construtor vazio
    public Mensagem() {}

    // Construtor básico de envio
    public Mensagem(Usuario remetente, Evento evento, String conteudo) {
        this.remetente = remetente;
        this.evento = evento;
        this.conteudo = conteudo;
        this.dataEnvio = LocalDate.now();
    }

    // Construtor completo
    public Mensagem(Integer id, Usuario remetente, Evento evento, String conteudo, LocalDate dataEnvio, LocalDate dataEdicao) {
        this.id = id;
        this.remetente = remetente;
        this.evento = evento;
        this.conteudo = conteudo;
        this.dataEnvio = dataEnvio;
        this.dataEdicao = dataEdicao;
    }

    // Getters e Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Usuario getRemetente() {
        return remetente;
    }

    public void setRemetente(Usuario remetente) {
        this.remetente = remetente;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public LocalDate getDataEnvio() {
        return dataEnvio;
    }

    public void setDataEnvio(LocalDate dataEnvio) {
        this.dataEnvio = dataEnvio;
    }

    public LocalDate getDataEdicao() {
        return dataEdicao;
    }

    public void setDataEdicao(LocalDate dataEdicao) {
        this.dataEdicao = dataEdicao;
    }
}
