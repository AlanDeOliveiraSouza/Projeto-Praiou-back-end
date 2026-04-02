package br.gov.sp.fatec.pg.praiou.model;

import java.time.LocalTime;
import java.time.LocalDate;

import br.gov.sp.fatec.pg.praiou.enums.TipoEvento;

public class Evento{
    //Atributos da classe EVENTO
    private Integer idEvento;
    private Integer idUsuario;
    private String nome;
    private String descricao;
    private String endereco;
    private TipoEvento tipoEvento;
    private LocalDate data;
    private LocalDate dataPublicacao;
    private LocalTime horario;
    private Integer limiteParticipantes;

    //Construtores
    public Evento(){}
    
    public Evento(Integer idEvento, Integer idUsuario, String nome, String descricao, String endereco, TipoEvento tipoEvento, LocalDate data, LocalTime horario, Integer limiteParticipantes){
        this.idEvento = idEvento;
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.descricao = descricao;
        this.endereco = endereco;
        this.tipoEvento = tipoEvento;
        this.data = data;
        this.dataPublicacao = LocalDate.now();
        this.horario = horario;
        this.limiteParticipantes = limiteParticipantes;
    }

    //Getters e Setters
    public Integer getIdEvento() {
        return idEvento;
    }
    public void setIdEvento(Integer idEvento) {
        this.idEvento = idEvento;
    }
    public Integer getIdUsuario() {
        return idUsuario;
    }
    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public String getEndereco() {
        return endereco;
    }
    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
    public TipoEvento getTipoEvento() {
        return tipoEvento;
    }
    public void setTipoEvento(TipoEvento tipoEvento) {
        this.tipoEvento = tipoEvento;
    }
    public LocalDate getData() {
        return data;
    }
    public void setDataEvento(LocalDate data) {
        this.data = data;
    }
    public LocalDate getDataPublicacao() {
        return dataPublicacao;
    }
    public void setDataPublicacao(LocalDate dataPublicacao) {
        this.dataPublicacao = dataPublicacao;
    }
    public LocalTime getHorario() {
        return horario;
    }
    public void setHorario(LocalTime horario) {
        this.horario = horario;
    }
    public Integer getLimiteParticipantes() {
        return limiteParticipantes;
    }
    public void setLimiteParticipantes(Integer limiteParticipantes) {
        this.limiteParticipantes = limiteParticipantes;
    }

    // para testes
    /* 
    @Override
    public String toString(){
        return  idEvento + ", " +
                idUsuario + ", " +
                nome + ", " +
                descricao + ", " +
                endereco + ", " +
                tipoEvento + ", " +
                data.toString() + ", " +
                dataPublicacao.toString() + ", " +
                horario.toString() + ", " +
                limiteParticipantes;    
    }    
    
    public static void main(String[] args){
        Evento evento = new Evento(1, 1, "Nome do evento", "Descrição", "Endereço", TipoEvento.Surf, LocalDate.of(2026,05,15), LocalTime.of(16,0), 10);
        System.out.println(evento.toString());
    }
    */
}