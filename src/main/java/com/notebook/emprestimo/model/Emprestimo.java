package com.notebook.emprestimo.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Emprestimo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Aluno aluno;

    @ManyToOne
    private Notebook notebook;

    private LocalDateTime dataRetirada;

    private LocalDateTime dataDevolucao; // null até devolver

    // Se necessário, adicione um método auxiliar para o formato desejado de data
    public String getFormattedDataRetirada() {
        if (dataRetirada != null) {
            return dataRetirada.toLocalDate().toString(); // Formato básico, pode ser ajustado
        }
        return "Não disponível";
    }
    public Notebook getNotebook() {
        return this.notebook;  // Retorna o notebook associado ao empréstimo
    }

}
