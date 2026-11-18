package br.gov.sp.fatec.pg.praiou.model;

import java.time.LocalDate;

public class Avaliacao { 

    private Integer idUsuarioAvaliador;  
    private Integer idUsuarioAvaliado;   
    private Integer idEvento;      
    private boolean curtida;   
    private LocalDate dataAvaliacao; 

    public Avaliacao() {}

    public Avaliacao(Integer idUsuarioAvaliador, Integer idUsuarioAvaliado, Integer idEvento, boolean curtida) {
        this.idUsuarioAvaliador = idUsuarioAvaliador;
        this.idUsuarioAvaliado = idUsuarioAvaliado;
        this.idEvento = idEvento;
        this.curtida = curtida;
        this.dataAvaliacao = LocalDate.now();
    }

    
    public Avaliacao(Integer idUsuarioAvaliador, Integer idUsuarioAvaliado, Integer idEvento, boolean curtida, LocalDate dataAvaliacao) {
        this.idUsuarioAvaliador = idUsuarioAvaliador;
        this.idUsuarioAvaliado = idUsuarioAvaliado;
        this.idEvento = idEvento;
        this.curtida = curtida;
        this.dataAvaliacao = dataAvaliacao;
    }

    public Integer getIdUsuarioAvaliador() {
        return idUsuarioAvaliador;
    }

    public void setIdUsuarioAvaliador(Integer idUsuarioAvaliador) { 
        this.idUsuarioAvaliador = idUsuarioAvaliador; 
    }

    public Integer getIdUsuarioAvaliado() { 
        return idUsuarioAvaliado;
    }

    public void setIdUsuarioAvaliado(Integer idUsuarioAvaliado) { 
        this.idUsuarioAvaliado = idUsuarioAvaliado; 
    }

    public Integer getIdEvento() { 
        return idEvento; 
    }

    public void setIdEvento(Integer idEvento) { 
        this.idEvento = idEvento; 
    }

    public boolean isCurtida() { 
        return curtida; 
    }

    public void setCurtida(boolean curtida) { 
        this.curtida = curtida; 
    }

    public LocalDate getDataAvaliacao() { 
        return dataAvaliacao; 
    }

    public void setDataAvaliacao(LocalDate dataAvaliacao) { 
        this.dataAvaliacao = dataAvaliacao; 
    }
}
