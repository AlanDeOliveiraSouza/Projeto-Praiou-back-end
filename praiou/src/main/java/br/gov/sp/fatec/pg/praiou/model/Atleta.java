package br.gov.sp.fatec.pg.praiou.model;

import java.time.LocalDate;

public class Atleta extends Usuario {

    // Atributos exclusivos do ATLETA
    private String bio;
    private String fotoPerfil;
    private Integer pontuacao;

    // Construtor sem parâmetros
    public Atleta() {}

    // Construtor com parâmetros básicos para cadastro
    public Atleta(String nome, String email, String senha) {
        super(nome, email, senha);
    }

    // Construtor com parâmetros básicos para login de ATLETA
    public Atleta(String email, String senha) {
        super(email, senha, false);
    }

    // Construtor com todos os atributos
    public Atleta(Integer id, String nome, LocalDate dataNascimento, String email, String senha, String token, boolean statusAdministrador, String bio, String fotoPerfil, Integer pontuacao) {
        super(id, nome, dataNascimento, email, senha, token, statusAdministrador);
        this.bio = bio;
        this.fotoPerfil = fotoPerfil;
        this.pontuacao = pontuacao;
    }

    // Getters e Setters

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public Integer getPontuacao() {
        return pontuacao;
    }

    public void setPontuacao(Integer pontuacao) {
        this.pontuacao = pontuacao;
    }

}
