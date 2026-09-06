package com.estagio.backend.model;

import jakarta.persistence.*;

    @Entity
    @Table(name = "tb_pessoa_tipo")
    public class PessoaTipo {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "pessoa_tipo_id")
        private Long id;

        @Column(name = "nome", nullable = false, unique = true, length = 200)
        private String nome;

        // Construtor padrão exigido pelo JPA
        public PessoaTipo() {
        }

        // Construtor auxiliar para facilitar a criação de objetos
        public PessoaTipo(String nome) {
            this.nome = nome;
        }

        // Getters e Setters (permitem ler e alterar os dados com segurança)
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
    }

