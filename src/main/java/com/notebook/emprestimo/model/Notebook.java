package com.notebook.emprestimo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notebook {

    @Id
    private String codigoBarra; // Código de barras do notebook
    private boolean disponivel = true; // Indica se o notebook está disponível para empréstimo

    // Construtor apenas com código de barras
    public Notebook(String codigoBarra) {
        this.codigoBarra = codigoBarra;
        this.disponivel = true; // Por padrão, o notebook está disponível
    }
}
