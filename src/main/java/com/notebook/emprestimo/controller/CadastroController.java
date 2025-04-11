package com.notebook.emprestimo.controller;

import com.notebook.emprestimo.model.Aluno;
import com.notebook.emprestimo.model.Notebook;
import com.notebook.emprestimo.service.AlunoService;
import com.notebook.emprestimo.service.NotebookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CadastroController {

    private final AlunoService alunoService;
    private final NotebookService notebookService;

    @PostMapping("/alunos")
    public ResponseEntity<Aluno> cadastrarAluno(@RequestBody Aluno aluno) {
        return ResponseEntity.ok(alunoService.salvar(aluno)); // Chama o método salvar de AlunoService
    }

    @PostMapping("/notebooks")
    public ResponseEntity<Notebook> cadastrarNotebook(@RequestBody Notebook notebook) {
        notebook.setDisponivel(true); // Define como disponível ao criar
        return ResponseEntity.ok(notebookService.salvar(notebook)); // Chama o método salvar de NotebookService
    }
}
