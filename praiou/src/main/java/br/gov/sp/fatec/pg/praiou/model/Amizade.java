package br.gov.sp.fatec.pg.praiou.model;

import java.time.LocalDate;

public class Amizade {

    private Integer id;
    private Integer idUsuarioSolicitante;
    private Integer idUsuarioRecebedor;
    private LocalDate dataAmizade;
    private String status; 
 
    // Construtor vazio
    public Amizade() {}

    // Construtor básico para criar solicitação
    public Amizade(Integer idUsuarioSolicitante, Integer idUsuarioRecebedor, String status) {
        this.idUsuarioSolicitante = idUsuarioSolicitante;
        this.idUsuarioRecebedor = idUsuarioRecebedor;
        this.status = status;
        this.dataAmizade = LocalDate.now(); 
    }

    // Construtor completo
    public Amizade(Integer id, Integer idUsuarioSolicitante, Integer idUsuarioRecebedor, LocalDate dataAmizade, String status) {
        this.id = id;
        this.idUsuarioSolicitante = idUsuarioSolicitante;
        this.idUsuarioRecebedor = idUsuarioRecebedor;
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

    public Integer getIdUsuarioSolicitante() {
        return idUsuarioSolicitante;
    }

    public void setIdUsuarioSolicitante(Integer idUsuarioSolicitante) {
        this.idUsuarioSolicitante = idUsuarioSolicitante;
    }

    public Integer getUsuarioRecebedor() {
        return idUsuarioRecebedor;
    }

    public void setIdUsuarioRecebedor(Integer idUsuarioRecebedor) {
        this.idUsuarioRecebedor = idUsuarioRecebedor;
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
