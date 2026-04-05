package br.gov.sp.fatec.pg.praiou.model;

import java.time.LocalDate;

public class Usuario {

    // Atributos padrão para todos os USUARIOs
    private Integer id;
    private String nome;
    private LocalDate dataNascimento;
    private String email;
    private String senha;
    private String token;
    private boolean statusAdministrador;

    // Construtores de USUARIO:
    // Construtor vazio para ser usado na coleta dos dados do banco
    public Usuario() {}

    // Construtor básico para login
    public Usuario(String email, String senha, boolean statusAdministrador) {
        this.email = email;
        this.senha = senha;
        this.statusAdministrador = statusAdministrador;
    }

    // Construtor básico para cadastro de ATLETA
    public Usuario(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.statusAdministrador = false;
    }

    // Construtor com todos os atributos nos parâmetros
    public Usuario(Integer id, String nome, LocalDate dataNascimento, String email, String senha, String token, boolean statusAdministrador) {
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

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
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
