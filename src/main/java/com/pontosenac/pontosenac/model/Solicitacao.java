package com.pontosenac.pontosenac.model;

import com.pontosenac.pontosenac.componentes.SolicitacaoStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Solicitacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String dataAbertura, dataSolicita, horaEntrada, horaSaida, descricao, observacao;

    @ManyToOne(cascade = CascadeType.MERGE)
    private Pessoa pessoa;

    @Enumerated(EnumType.STRING)
    private SolicitacaoStatus solicitacaoStatus;

    @ManyToOne(cascade = CascadeType.REFRESH)
    private TipoSolicitacao tipoSolicitacao;

    public Solicitacao() {
    }

    public Solicitacao(int id, String dataAbertura, String dataSolicita, String horaEntrada, String horaSaida,
            String descricao, String observacao, Pessoa pessoa, SolicitacaoStatus solicitacaoStatus,
            TipoSolicitacao tipoSolicitacao) {
        this.id = id;
        this.dataAbertura = dataAbertura;
        this.dataSolicita = dataSolicita;
        this.horaEntrada = horaEntrada;
        this.horaSaida = horaSaida;
        this.descricao = descricao;
        this.observacao = observacao;
        this.pessoa = pessoa;
        this.solicitacaoStatus = solicitacaoStatus;
        this.tipoSolicitacao = tipoSolicitacao;
    }

    public String getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(String horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public String getHoraSaida() {
        return horaSaida;
    }

    public void setHoraSaida(String horaSaida) {
        this.horaSaida = horaSaida;
    }

    public String getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(String dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public String getDataSolicita() {
        return dataSolicita;
    }

    public void setDataSolicita(String dataSolicita) {
        this.dataSolicita = dataSolicita;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public SolicitacaoStatus getSolicitacaoStatus() {
        return solicitacaoStatus;
    }

    public void setSolicitacaoStatus(SolicitacaoStatus solicitacaoStatus) {
        this.solicitacaoStatus = solicitacaoStatus;
    }

    public TipoSolicitacao getTipoSolicitacao() {
        return tipoSolicitacao;
    }

    public void setTipoSolicitacao(TipoSolicitacao tipoSolicitacao) {
        this.tipoSolicitacao = tipoSolicitacao;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
