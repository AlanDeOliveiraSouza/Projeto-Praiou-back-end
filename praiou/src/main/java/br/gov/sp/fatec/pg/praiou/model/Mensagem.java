package br.gov.sp.fatec.pg.praiou.model;

import java.time.LocalDate;

public class Mensagem {

    private Integer id;
    private Integer idRemetente;
    private Integer idEvento;
    private String conteudo; 
    private LocalDate data;
    private LocalDate dataEdicao;    

    // Construtor vazio
    public Mensagem() {}

    // Construtor básico de envio
    public Mensagem(Integer idRemetente, Integer idEvento, String conteudo) {
        this.idRemetente = idRemetente;
        this.idEvento = idEvento;
        this.conteudo = conteudo;
        this.data = LocalDate.now();
    }

    // Construtor completo
    public Mensagem(Integer id, Integer idRemetente, Integer idEvento, String conteudo, LocalDate dataEnvio, LocalDate dataEdicao) {
        this.id = id;
        this.idRemetente = idRemetente;      
        this.idEvento = idEvento;  
        this.conteudo = conteudo;
        this.data = dataEnvio;
        this.dataEdicao = dataEdicao;
    }

    // Getters e Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdRemetente() {
        return idRemetente;
    }

    public void setIdRemetente(Integer idRemetente) {
        this.idRemetente = idRemetente;
    }

    public Integer getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(Integer idEvento) {
        this.idEvento = idEvento;
    }


    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate dataEnvio) {
        this.data = dataEnvio;
    }

    public LocalDate getDataEdicao() {
        return dataEdicao;
    }

    public void setDataEdicao(LocalDate dataEdicao) {
        this.dataEdicao = dataEdicao;
    }
}
