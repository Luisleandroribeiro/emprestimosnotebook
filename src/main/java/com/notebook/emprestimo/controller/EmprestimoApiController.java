package com.notebook.emprestimo.controller;

import com.notebook.emprestimo.model.Emprestimo;
import com.notebook.emprestimo.service.AlunoService;
import com.notebook.emprestimo.service.EmprestimoService;
import com.notebook.emprestimo.service.NotebookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EmprestimoApiController {

    private final AlunoService alunoService;
    private final NotebookService notebookService;
    private final EmprestimoService emprestimoService;

    // POST /api/emprestimos
    @PostMapping("/emprestimos")
    public ResponseEntity<?> registrarEmprestimo(@RequestParam String qrCodeAluno, @RequestParam String codigoNotebook) {
        // Buscar ou criar aluno
        var aluno = alunoService.buscarOuCriarAluno(qrCodeAluno);

        // Buscar ou criar notebook
        var notebook = notebookService.buscarOuCriarNotebook(codigoNotebook);

        // Verificar se o notebook está disponível
        if (!notebook.isDisponivel()) {
            return ResponseEntity.badRequest().body("Notebook já está emprestado.");
        }

        // Registrar o empréstimo
        Emprestimo emprestimo = emprestimoService.registrarEmprestimo(aluno, notebook);
        notebookService.marcarComoIndisponivel(notebook);

        return ResponseEntity.ok(emprestimo);
    }

    // POST /api/devolucoes
    @PostMapping("/devolucoes")
    public ResponseEntity<?> registrarDevolucao(@RequestParam String codigoNotebook) {
        var notebookOpt = notebookService.buscarPorCodigoBarra(codigoNotebook);
        if (notebookOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Notebook não encontrado.");
        }

        var emprestimoOpt = emprestimoService.buscarEmprestimoAtivoPorNotebook(notebookOpt.get());
        if (emprestimoOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Nenhum empréstimo ativo encontrado para esse notebook.");
        }

        Emprestimo devolvido = emprestimoService.registrarDevolucao(emprestimoOpt.get());
        notebookService.marcarComoDisponivel(notebookOpt.get());

        return ResponseEntity.ok(devolvido);
    }

    // GET /api/emprestimos
    @GetMapping("/emprestimos")
    public ResponseEntity<List<Emprestimo>> listarEmprestimosPorPeriodo(
            @RequestParam String inicio, @RequestParam String fim) {
        // Converte as strings para LocalDateTime
        LocalDateTime inicioDateTime = LocalDateTime.parse(inicio);
        LocalDateTime fimDateTime = LocalDateTime.parse(fim);

        var lista = emprestimoService.buscarEmprestimosPorPeriodo(inicioDateTime, fimDateTime);
        return ResponseEntity.ok(lista);
    }
}
