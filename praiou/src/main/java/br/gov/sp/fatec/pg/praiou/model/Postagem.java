package br.gov.sp.fatec.pg.praiou.model;

import java.sql.Timestamp;

public class Postagem{
    //Atributos da classe POSTAGEM
    private Integer id;
    private Integer idUsuario;
    private String imagem; // Armazena o caminho da imagem no servidor
    private String texto;
    private Timestamp data;

    //Construtores
    public Postagem() {}

    public Postagem(Integer id, Integer idUsuario, String imagem, String texto, Timestamp data){
        this.id = id;
        this.idUsuario = idUsuario;
        this.imagem = imagem;
        this.texto = texto;
        this.data = data;
    }

    public Postagem(Integer id, Integer idUsuario, String texto, Timestamp data){
        this.id = id;
        this.idUsuario = idUsuario;
        this.imagem = null;
        this.texto = texto;
        this.data = data;
    }

    //Getters e Setters
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getIdUsuario() {
        return idUsuario;
    }
    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }
    public String getImagem() {
        return imagem;
    }
    public void setImagem(String imagem) {
        this.imagem = imagem;
    }
    public String getTexto() {
        return texto;
    }
    public void setTexto(String texto) {
        this.texto = texto;
    }
    public Timestamp getData() {
        return data;
    }
    public void setData(Timestamp data) {
        this.data = data;
    }

}