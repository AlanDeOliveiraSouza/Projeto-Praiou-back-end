package br.gov.sp.fatec.pg.praiou.model;

import java.time.LocalDate;

public class Amizade {

    private Integer id;
    private Usuario usuarioSolicitante;
    private Usuario usuarioAmigo;
    private LocalDate dataAmizade;
    private String status; 

    // Construtor vazio
    public Amizade() {}

    // Construtor básico para criar solicitação
    public Amizade(Usuario usuarioSolicitante, Usuario usuarioAmigo, String status) {
        this.usuarioSolicitante = usuarioSolicitante;
        this.usuarioAmigo = usuarioAmigo;
        this.status = status;
        this.dataAmizade = LocalDate.now(); 
    }

    // Construtor completo
    public Amizade(Integer id, Usuario usuarioSolicitante, Usuario usuarioAmigo, LocalDate dataAmizade, String status) {
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

    public LocalDate getDataAmizade() {
        return dataAmizade;
    }

    public void setDataAmizade(LocalDate dataAmizade) {
        this.dataAmizade = dataAmizade;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
