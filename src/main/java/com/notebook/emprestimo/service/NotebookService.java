package com.notebook.emprestimo.service;

import com.notebook.emprestimo.model.Notebook;
import com.notebook.emprestimo.repository.NotebookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotebookService {

    private final NotebookRepository notebookRepository;

    // Busca um notebook pelo código de barras
    public Optional<Notebook> buscarPorCodigoBarra(String codigoBarra) {
        return notebookRepository.findByCodigoBarra(codigoBarra);
    }

    // Método para criar um notebook, se necessário
    public Notebook buscarOuCriarNotebook(String codigoBarra) {
        Optional<Notebook> notebookOpt = buscarPorCodigoBarra(codigoBarra);
        if (notebookOpt.isEmpty()) {
            // Se o notebook não existir, cria ele automaticamente
            Notebook novoNotebook = new Notebook();
            novoNotebook.setCodigoBarra(codigoBarra);
            return notebookRepository.save(novoNotebook); // Salva o novo notebook no banco de dados
        }
        return notebookOpt.get(); // Retorna o notebook já existente
    }

    // Marcar como indisponível
    public void marcarComoIndisponivel(Notebook notebook) {
        notebook.setDisponivel(false);
        notebookRepository.save(notebook);
    }

    // Marcar como disponível
    public void marcarComoDisponivel(Notebook notebook) {
        notebook.setDisponivel(true);
        notebookRepository.save(notebook);
    }

    // Método salvar para simplificar o código no controller
    public Notebook salvar(Notebook notebook) {
        return notebookRepository.save(notebook);
    }
    public void removerTodosNotebooks() {
        notebookRepository.deleteAll(); // Remove todos os notebooks
    }
}
