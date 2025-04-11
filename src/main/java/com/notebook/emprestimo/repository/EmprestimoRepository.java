package com.notebook.emprestimo.repository;

import com.notebook.emprestimo.model.Emprestimo;
import com.notebook.emprestimo.model.Notebook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {
    Optional<Emprestimo> findByNotebookAndDataDevolucaoIsNull(Notebook notebook);
    List<Emprestimo> findAllByDataRetiradaBetween(java.time.LocalDateTime inicio, java.time.LocalDateTime fim);
    Optional<Emprestimo> findByNotebookCodigoBarraAndDataDevolucaoIsNull(String codigoNotebook);
    List<Emprestimo> findByDataDevolucaoIsNull();
    List<Emprestimo> findByDataDevolucaoIsNotNull();
    long countByDataDevolucaoIsNull();
}
