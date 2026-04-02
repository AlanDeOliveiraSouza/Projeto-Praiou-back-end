package br.gov.sp.fatec.pg.praiou.model;

import java.awt.image.BufferedImage;
import java.sql.Timestamp;

public class Postagem{
    //Atributos da classe POSTAGEM
    private Integer idPostagem;
    private Integer idUsuario;
    private BufferedImage imagem;
    private String texto;
    private Timestamp data;

    //Construtores
    public Postagem(){}
    public Postagem(Integer idPostagem, Integer idUsuario, BufferedImage imagem, String texto){
        this.idPostagem = idPostagem;
        this.idUsuario = idUsuario;
        this.imagem = imagem;
        this.texto = texto;
        this.data = new Timestamp(System.currentTimeMillis());
    }

    public Postagem(Integer idPostagem, Integer idUsuario, String texto){
        this.idPostagem = idPostagem;
        this.idUsuario = idUsuario;
        this.imagem = null;
        this.texto = texto;
        this.data = new Timestamp(System.currentTimeMillis());
    }

    //Getters e Setters
    public Integer getidPostagem() {
        return idPostagem;
    }
    public void setidPostagem(Integer idPostagem) {
        this.idPostagem = idPostagem;
    }
    public Integer getIdUsuario() {
        return idUsuario;
    }
    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }
    public BufferedImage getImagem() {
        return imagem;
    }
    public void setImagem(BufferedImage imagem) {
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

    //para testes
    /*
    @Override
    public String toString(){
        return idPostagem + ", " + idUsuario + ", " + texto + ", " + data.toString();
    }

    public static void main(String[] args) {
        Postagem post = new Postagem(1, 1, "Texto da postagem");
        System.out.println(post.toString());
    }
    */
}