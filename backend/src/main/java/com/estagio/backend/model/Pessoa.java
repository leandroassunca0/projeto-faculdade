package com.estagio.backend.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "tb_pessoas")
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pessoa_id")
    private Long id;

    @Column(name = "nome", nullable = false, length = 200)
    private String nome;

    @Column(name = "cpf", nullable = false, unique = true, length = 14)
    private String cpf;

    @Column(name = "nascimento", nullable = false)
    private LocalDate nascimento;

    @Column(name = "telefone", nullable = false, length = 20)
    private String telefone;

    // Relacionamento: Muitas pessoas podem ter o mesmo Tipo (Many to One)
    @ManyToOne(optional = false)
    @JoinColumn(name = "pessoa_tipo_id", nullable = false)
    private PessoaTipo pessoaTipo;

    @Column(name = "atualizado_por")
    private Long atualizadoPor;

    public Pessoa() {
    }

    public Pessoa(String nome, String cpf, LocalDate nascimento, String telefone, PessoaTipo pessoaTipo) {
        this.nome = nome;
        this.cpf = cpf;
        this.nascimento = nascimento;
        this.telefone = telefone;
        this.pessoaTipo = pessoaTipo;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getNascimento() {
        return nascimento;
    }

    public void setNascimento(LocalDate nascimento) {
        this.nascimento = nascimento;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public PessoaTipo getPessoaTipo() {
        return pessoaTipo;
    }

    public void setPessoaTipo(PessoaTipo pessoaTipo) {
        this.pessoaTipo = pessoaTipo;
    }

    public Long getAtualizadoPor() {
        return atualizadoPor;
    }

    public void setAtualizadoPor(Long atualizadoPor) {
        this.atualizadoPor = atualizadoPor;
    }
}