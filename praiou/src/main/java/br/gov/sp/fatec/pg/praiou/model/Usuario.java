package br.gov.sp.fatec.pg.praiou.model;

import java.util.Date;

import br.gov.sp.fatec.pg.praiou.enums.TipoUsuario;

public class Usuario {
    // Atributos da classe USUARIO
    private Integer id;
    private String nome;
    private String sobrenome;
    private Date dataNascimento;
    private String email;
    private String senha;
    private String token;
    private String telefone;
    private TipoUsuario tipoUsuario;

    // Construtores de USUARIO:
    
    // Construtor vazio para ser usado na coleta dos dados do banco
    public Usuario() {}

    public Usuario(Integer id, String nome, String sobrenome, Date dataNascimento, String email, String senha, String token, String telefone, TipoUsuario tipoUsuario) {
        this.id = id;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.dataNascimento = dataNascimento;
        this.email = email;
        this.senha = senha;
        this.token = token;
        this.telefone = telefone;
        this.tipoUsuario = tipoUsuario;
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

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
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

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }
}
