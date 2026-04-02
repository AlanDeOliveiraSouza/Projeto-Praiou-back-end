package br.gov.sp.fatec.pg.praiou.model;

import java.util.Date;

public class Mensagem {

    private Integer id;
    private Usuario remetente;
    private Evento evento; 
    private String conteudo; 
    private Date dataEnvio;

    // Construtor vazio
    public Mensagem() {}

    // Construtor básico de envio
    public Mensagem(Usuario remetente, Evento evento, String conteudo) {
        this.remetente = remetente;
        this.evento = evento;
        this.conteudo = conteudo;
        this.dataEnvio = new Date();
    }

    // Construtor completo
    public Mensagem(Integer id, Usuario remetente, Evento evento, String conteudo, Date dataEnvio) {
        this.id = id;
        this.remetente = remetente;
        this.evento = evento;
        this.conteudo = conteudo;
        this.dataEnvio = dataEnvio;
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

    public Date getDataEnvio() {
        return dataEnvio;
    }

    public void setDataEnvio(Date dataEnvio) {
        this.dataEnvio = dataEnvio;
    }
}
