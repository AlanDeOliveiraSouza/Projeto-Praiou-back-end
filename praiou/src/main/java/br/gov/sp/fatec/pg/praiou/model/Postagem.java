package br.gov.sp.fatec.pg.praiou.model;

//import java.awt.image.BufferedImage;
import java.sql.Timestamp;

public class Postagem{
    //Atributos da classe POSTAGEM
    private Integer idPostagem;
    private Integer idUsuario;
    private String imagem;
    private String texto;
    private Timestamp data;

    //Construtores
    public Postagem(){}
    public Postagem(Integer idPostagem, Integer idUsuario, String imagem, String texto, Timestamp data){
        this.idPostagem = idPostagem;
        this.idUsuario = idUsuario;
        this.imagem = imagem;
        this.texto = texto;
        this.data = data;
    }

    public Postagem(Integer idPostagem, Integer idUsuario, String texto, Timestamp data){
        this.idPostagem = idPostagem;
        this.idUsuario = idUsuario;
        this.imagem = null;
        this.texto = texto;
        this.data = data;
    }

    //Getters e Setters
    public Integer getIdPostagem() {
        return idPostagem;
    }
    public void setIdPostagem(Integer idPostagem) {
        this.idPostagem = idPostagem;
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