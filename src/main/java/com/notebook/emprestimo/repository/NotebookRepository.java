package com.notebook.emprestimo.repository;

import com.notebook.emprestimo.model.Notebook;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface NotebookRepository extends JpaRepository<Notebook, Long> {

    // Método para buscar notebook pelo código de barras
    Optional<Notebook> findByCodigoBarra(String codigoBarra);
}
