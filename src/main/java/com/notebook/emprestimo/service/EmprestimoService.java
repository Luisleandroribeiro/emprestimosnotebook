package com.notebook.emprestimo.service;

import com.notebook.emprestimo.model.Aluno;
import com.notebook.emprestimo.model.Emprestimo;
import com.notebook.emprestimo.model.Notebook;
import com.notebook.emprestimo.repository.EmprestimoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmprestimoService {

    private final EmprestimoRepository emprestimoRepository;
    private final NotebookService notebookService;

    public Emprestimo registrarEmprestimo(Aluno aluno, Notebook notebook) {
        Emprestimo emprestimo = Emprestimo.builder()
                .aluno(aluno)
                .notebook(notebook)
                .dataRetirada(LocalDateTime.now())
                .build();
        return emprestimoRepository.save(emprestimo);
    }

    public Optional<Emprestimo> buscarEmprestimoAtivoPorNotebook(Notebook notebook) {
        return emprestimoRepository.findByNotebookAndDataDevolucaoIsNull(notebook);
    }

    // Buscar empréstimo ativo por código do notebook
    public Optional<Emprestimo> buscarEmprestimoAtivoPorNotebookCodigo(String codigoNotebook) {
        return emprestimoRepository.findByNotebookCodigoBarraAndDataDevolucaoIsNull(codigoNotebook);
    }

    public Emprestimo registrarDevolucao(Emprestimo emprestimo) {
        emprestimo.setDataDevolucao(LocalDateTime.now());
        return emprestimoRepository.save(emprestimo);
    }
    public List<Emprestimo> buscarEmprestimosPorStatus(boolean devolvido) {
        if (devolvido) {
            return emprestimoRepository.findByDataDevolucaoIsNotNull(); // Retorna todos os empréstimos devolvidos
        } else {
            return emprestimoRepository.findByDataDevolucaoIsNull(); // Retorna todos os empréstimos em aberto
        }
    }
    public long contarTotalEmprestimos() {
        return emprestimoRepository.count(); // Conta todos os empréstimos registrados
    }

    // Contar os empréstimos pendentes de devolução
    public long contarEmprestimosPendentes() {
        return emprestimoRepository.countByDataDevolucaoIsNull(); // Conta os empréstimos não devolvidos
    }

    public List<Emprestimo> buscarEmprestimosPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return emprestimoRepository.findAllByDataRetiradaBetween(inicio, fim);
    }
    public List<Emprestimo> buscarTodosEmprestimos() {
        return emprestimoRepository.findAll(); // Recupera todos os empréstimos do banco de dados
    }
    public void removerTodosEmprestimos() {
        emprestimoRepository.deleteAll();  // Remove todos os empréstimos do banco
    }

    // Método para remover todos os notebooks
    public void removerTodosNotebooks() {
        notebookService.removerTodosNotebooks();  // Chama o método do NotebookService
    }
}
