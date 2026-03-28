package br.gov.sp.fatec.pg.praiou.model;

import java.util.Date;

public abstract class Usuario {

    // Atributos padrão para todos os USUARIOs
    private Integer id;
    private String nome;
    private Date dataNascimento;
    private String email;
    private String senha;
    private String token;
    private boolean statusAdministrador;

    // Construtores de USUARIO:
    // Construtor vazio para ser usado na coleta dos dados do banco
    public Usuario() {}

    // Construtor básico para cadastro de ATLETA
    public Usuario(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.statusAdministrador = false;
    }

    // Construtor com todos os atributos nos parâmetros
    public Usuario(Integer id, String nome, Date dataNascimento, String email, String senha, String token, boolean statusAdministrador) {
        this.id = id;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.email = email;
        this.senha = senha;
        this.token = token;
        this.statusAdministrador = statusAdministrador;
    }

    // Getters e Setters:

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean getStatusAdministrador() {
        return statusAdministrador;
    }

    public void setStatusAdministrador(boolean statusAdministrador) {
        this.statusAdministrador = statusAdministrador;
    }
}
