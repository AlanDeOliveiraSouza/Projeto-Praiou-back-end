package br.gov.sp.fatec.pg.praiou.model;

import java.util.Date;

public class Amizade {

    private Integer id;
    private Usuario usuarioSolicitante;
    private Usuario usuarioAmigo;
    private Date dataAmizade;
    private String status; 

    // Construtor vazio
    public Amizade() {}

    // Construtor básico para criar solicitação
    public Amizade(Usuario usuarioSolicitante, Usuario usuarioAmigo, String status) {
        this.usuarioSolicitante = usuarioSolicitante;
        this.usuarioAmigo = usuarioAmigo;
        this.status = status;
        this.dataAmizade = new Date(); 
    }

    // Construtor completo
    public Amizade(Integer id, Usuario usuarioSolicitante, Usuario usuarioAmigo, Date dataAmizade, String status) {
        this.id = id;
        this.usuarioSolicitante = usuarioSolicitante;
        this.usuarioAmigo = usuarioAmigo;
        this.dataAmizade = dataAmizade;
        this.status = status;
    }

    // Getters e Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Usuario getUsuarioSolicitante() {
        return usuarioSolicitante;
    }

    public void setUsuarioSolicitante(Usuario usuarioSolicitante) {
        this.usuarioSolicitante = usuarioSolicitante;
    }

    public Usuario getUsuarioAmigo() {
        return usuarioAmigo;
    }

    public void setUsuarioAmigo(Usuario usuarioAmigo) {
        this.usuarioAmigo = usuarioAmigo;
    }

    public Date getDataAmizade() {
        return dataAmizade;
    }

    public void setDataAmizade(Date dataAmizade) {
        this.dataAmizade = dataAmizade;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
