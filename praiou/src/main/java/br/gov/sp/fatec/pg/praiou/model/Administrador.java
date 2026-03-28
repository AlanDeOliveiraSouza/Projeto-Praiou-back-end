package br.gov.sp.fatec.pg.praiou.model;

import java.util.Date;

public class Administrador extends Usuario {

    // Construtor vazio
    public Administrador() {}

    // Construtor com todos os atributos
    public Administrador(Integer id, String nome, Date dataNascimento, String email, String senha, String token) {
        super(id, nome, dataNascimento, email, senha, token, true);
    }

}
