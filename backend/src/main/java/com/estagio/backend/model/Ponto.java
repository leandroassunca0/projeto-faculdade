package com.estagio.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_ponto")
public class Ponto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ponto_id")
    private Long id;

    @Column(name = "inicio", nullable = false)
    private LocalDateTime inicio;

    @Column(name = "saida")
    private LocalDateTime saida;

    @Column(name = "justificativa", length = 255)
    private String justificativa;

    // Relacionamento: Vários pontos pertencem a uma Pessoa (Funcionário)
    @ManyToOne(optional = false)
    @JoinColumn(name = "funcionario_id", nullable = false)
    private Pessoa funcionario;

    @Column(name = "atualizado_em", nullable = false)
    private LocalDateTime atualizadoEm;

    @Column(name = "atualizado_por")
    private Long atualizadoPor;

    public Ponto() {
    }

    public Ponto(LocalDateTime inicio, LocalDateTime saida, String justificativa, Pessoa funcionario) {
        this.inicio = inicio;
        this.saida = saida;
        this.justificativa = justificativa;
        this.funcionario = funcionario;
    }

    @PrePersist
    public void prePersist() {
        this.atualizadoEm = LocalDateTime.now();
        if (this.inicio == null) {
            this.inicio = LocalDateTime.now();
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.atualizadoEm = LocalDateTime.now();
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getInicio() {
        return inicio;
    }

    public void setInicio(LocalDateTime inicio) {
        this.inicio = inicio;
    }

    public LocalDateTime getSaida() {
        return saida;
    }

    public void setSaida(LocalDateTime saida) {
        this.saida = saida;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }

    public Pessoa getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Pessoa funcionario) {
        this.funcionario = funcionario;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public void setAtualizadoEm(LocalDateTime atualizadoEm) {
        this.atualizadoEm = atualizadoEm;
    }

    public Long getAtualizadoPor() {
        return atualizadoPor;
    }

    public void setAtualizadoPor(Long atualizadoPor) {
        this.atualizadoPor = atualizadoPor;
    }
}
